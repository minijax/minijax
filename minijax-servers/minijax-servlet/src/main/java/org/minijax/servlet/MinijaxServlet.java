package org.minijax.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.minijax.Minijax;
import org.minijax.rs.MinijaxApplication;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinijaxServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(MinijaxServlet.class);
    private Minijax minijax;

    @Override
    public void init() throws ServletException {
        throw new ServletException("Missing init parameter \"jakarta.ws.rs.Application\"");
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void init(final ServletConfig config) throws ServletException {
        final String appClassName = config.getInitParameter("jakarta.ws.rs.Application");
        if (appClassName == null || appClassName.isEmpty()) {
            throw new ServletException("Missing init parameter \"jakarta.ws.rs.Application\"");
        }

        try {
            final Class appClass = Class.forName(appClassName);
            minijax = new Minijax().register(appClass);
        } catch (final ClassNotFoundException ex) {
            throw new ServletException(ex.getMessage(), ex);
        }
    }

    @Override
    protected void service(final HttpServletRequest servletRequest, final HttpServletResponse servletResponse) {
        final MinijaxApplication application = minijax.getDefaultApplication();

        try (final MinijaxRequestContext ctx = new MinijaxServletRequestContext(application, servletRequest)) {
            final Response response = application.handle(ctx);
            servletResponse.setStatus(response.getStatus());

            for (final Entry<String, List<Object>> entry : response.getHeaders().entrySet()) {
                final String name = entry.getKey();
                for (final Object value : entry.getValue()) {
                    servletResponse.addHeader(name, value.toString());
                }
            }

            final MediaType mediaType = response.getMediaType();
            if (mediaType != null) {
                servletResponse.setContentType(mediaType.toString());
            }

            EntityUtils.writeEntity(response.getEntity(), mediaType, ctx.getProviders(), servletResponse.getOutputStream());

        } catch (final IOException ex) {
            LOG.error("Unhandled exception: {}", ex.getMessage(), ex);
        }
    }
}
