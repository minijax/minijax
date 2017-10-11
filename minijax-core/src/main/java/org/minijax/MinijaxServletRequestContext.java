package org.minijax;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.minijax.util.IOUtils;
import org.minijax.util.UrlUtils;

public class MinijaxServletRequestContext extends MinijaxRequestContext {
    private final HttpServletRequest request;
    private Map<String, Cookie> cookies;
    private MultivaluedHashMap<String, String> headers;
    private MinijaxForm form;


    public MinijaxServletRequestContext(final HttpServletRequest request) {
        super(UrlUtils.getFullRequestUrl(request));
        this.request = request;
    }


    /**
     * Get the request method.
     *
     * @return the request method.
     * @see javax.ws.rs.HttpMethod
     */
    @Override
    public String getMethod() {
        return request.getMethod();
    }


    /**
     * Get the mutable request headers multivalued map.
     *
     * @return mutable multivalued map of request headers.
     * @see #getHeaderString(String)
     */
    @Override
    public MultivaluedMap<String, String> getHeaders() {
        if (headers == null) {
            headers = new MultivaluedHashMap<>();

            final Enumeration<String> ne = request.getHeaderNames();
            while (ne.hasMoreElements()) {
                final String name = ne.nextElement();
                final Enumeration<String> ve = request.getHeaders(name);
                while (ve.hasMoreElements()) {
                    headers.add(name, ve.nextElement());
                }
            }
        }
        return headers;
    }


    /**
     * Get any cookies that accompanied the request.
     *
     * @return a read-only map of cookie name (String) to {@link Cookie}.
     */
    @Override
    public Map<String, Cookie> getCookies() {
        if (cookies == null) {
            cookies = new HashMap<>();

            if (request.getCookies() != null) {
                for (final javax.servlet.http.Cookie sc : request.getCookies()) {
                    cookies.put(
                            sc.getName(),
                            new Cookie(sc.getName(), sc.getValue(), sc.getPath(), sc.getDomain(), sc.getVersion()));
                }
            }
        }
        return cookies;
    }


    /**
     * Get the entity input stream. The JAX-RS runtime is responsible for
     * closing the input stream.
     *
     * @return entity input stream.
     */
    @Override
    public InputStream getEntityStream() {
        try {
            return request.getInputStream();
        } catch (final IOException ex) {
            throw new MinijaxException(ex);
        }
    }


    /**
     * Returns the entity as form content.
     *
     * @return The form.
     */
    @Override
    public MinijaxForm getForm() {
        if (form == null) {
            readForm();
        }
        return form;
    }


    /**
     * Reads the entity as form content.
     */
    private void readForm() {
        final MediaType contentType = getMediaType();

        if (contentType == null) {
            throw new BadRequestException("Missing content type");
        }

        try {
            if (contentType.isCompatible(MediaType.APPLICATION_FORM_URLENCODED_TYPE)) {
                form = new MinijaxUrlEncodedForm(IOUtils.toString(getEntityStream(), StandardCharsets.UTF_8));
            } else if (contentType.isCompatible(MediaType.MULTIPART_FORM_DATA_TYPE)) {
                form = new MinijaxMultipartForm(request.getParts());
            } else {
                throw new BadRequestException("Unsupported content type (" + contentType + ")");
            }

        } catch (final IOException | ServletException ex) {
            throw new WebApplicationException(ex.getMessage(), ex);
        }
    }


    @Override
    public void close() throws IOException {
        if (form != null) {
            form.close();
        }
    }
}
