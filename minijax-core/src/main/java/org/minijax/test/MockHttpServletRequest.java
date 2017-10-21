package org.minijax.test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;
import javax.ws.rs.core.MultivaluedMap;

import org.minijax.util.IOUtils;

public class MockHttpServletRequest implements HttpServletRequest {
    private final MultivaluedMap<String, String> headers;
    private final String method;
    private final URI requestUri;
    private final Cookie[] cookies;
    private final MockServletInputStream inputStream;
    private Collection<Part> parts;

    public MockHttpServletRequest(final String method, final URI requestUri) {
        this(method, requestUri, null, null, null);
    }

    public MockHttpServletRequest(
            final String method,
            final URI requestUri,
            final MultivaluedMap<String, String> headers,
            final InputStream contentBody,
            final Cookie[] cookies) {

        this.headers = headers;
        this.cookies = cookies;
        this.method = method;
        this.requestUri = requestUri;

        if (contentBody != null) {
            inputStream = new MockServletInputStream(contentBody);
        } else {
            inputStream = null;
        }
    }

    @Override
    public String getHeader(final String name) {
        return headers == null ? null : headers.getFirst(name);
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getPathInfo() {
        return requestUri.getPath();
    }

    @Override
    public String getRequestURI() {
        return requestUri.getPath();
    }

    @Override
    public StringBuffer getRequestURL() {
        return new StringBuffer(requestUri.toString().split("\\?")[0]);
    }

    @Override
    public Cookie[] getCookies() {
        return cookies;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return inputStream;
    }

    @Override
    public Enumeration<String> getHeaders(final String name) {
        final Vector<String> values = new Vector<>();
        if (headers != null) {
            values.addAll(headers.get(name));
        }
        return values.elements();
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        final Vector<String> names = new Vector<>();
        if (headers != null) {
            names.addAll(headers.keySet());
        }
        return names.elements();
    }

    @Override
    public String getQueryString() {
        return requestUri.getQuery();
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        final Pattern pattern = Pattern.compile("name=\"(?<name>[^\"]+)\"\n\n(?<value>.+)");

        if (parts == null) {
            final String str = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            final int index = str.indexOf('\n') + 1;
            final String boundary = str.substring(0, index);
            final String content = str.substring(index);
            final String[] strParts = content.split(boundary, 0);
            parts = new ArrayList<>(strParts.length);

            for (final String strPart : strParts) {
                final Matcher matcher = pattern.matcher(strPart);
                if (matcher.find()) {
                    final String name= matcher.group("name");
                    final String value = matcher.group("value");
                    parts.add(new MockPart(name, value));
                }
            }
        }
        return parts;
    }

    @Override
    public Object getAttribute(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCharacterEncoding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCharacterEncoding(final String env) throws UnsupportedEncodingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getContentLength() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getContentLengthLong() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getContentType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getParameter(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Enumeration<String> getParameterNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getParameterValues(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getProtocol() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getScheme() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getServerName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getServerPort() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRemoteAddr() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRemoteHost() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAttribute(final String name, final Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAttribute(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Locale getLocale() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Enumeration<Locale> getLocales() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSecure() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RequestDispatcher getRequestDispatcher(final String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRealPath(final String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getRemotePort() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getLocalName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getLocalAddr() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLocalPort() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ServletContext getServletContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AsyncContext startAsync() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AsyncContext startAsync(final ServletRequest servletRequest, final ServletResponse servletResponse) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAsyncStarted() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAsyncSupported() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AsyncContext getAsyncContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DispatcherType getDispatcherType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAuthType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getDateHeader(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getIntHeader(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPathTranslated() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getContextPath() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRemoteUser() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isUserInRole(final String role) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Principal getUserPrincipal() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRequestedSessionId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getServletPath() {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpSession getSession(final boolean create) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpSession getSession() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String changeSessionId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean authenticate(final HttpServletResponse response) throws IOException, ServletException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void login(final String username, final String password) throws ServletException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void logout() throws ServletException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Part getPart(final String name) throws IOException, ServletException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(final Class<T> handlerClass) throws IOException, ServletException {
        throw new UnsupportedOperationException();
    }

    static class MockPart implements Part {
        private final String name;
        private final InputStream inputStream;

        public MockPart(final String name, final String value) {
            this.name = name;
            inputStream = new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return inputStream;
        }

        @Override
        public String getContentType() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getSubmittedFileName() {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getSize() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void write(final String fileName) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void delete() throws IOException {
            // No-op
        }

        @Override
        public String getHeader(final String name) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Collection<String> getHeaders(final String name) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Collection<String> getHeaderNames() {
            throw new UnsupportedOperationException();
        }
    }
}
