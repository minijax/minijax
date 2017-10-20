/*
 * AJIBOT CONFIDENTIAL
 * __________________
 *
 *  2017 Ajibot Inc
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Ajibot Inc and its suppliers, if any.
 * The intellectual and technical concepts contained herein
 * are proprietary to Ajibot Inc and its suppliers and may
 * be covered by U.S. and Foreign Patents, patents in process,
 * and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this
 * material is strictly forbidden unless prior written
 * permission is obtained from Ajibot Inc.
 */
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

    public MockHttpServletRequest(
            final MultivaluedMap<String, String> headers,
            final Cookie[] cookies,
            final String method,
            final URI requestUri,
            final InputStream contentBody) {

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
    public Object getAttribute(final String name) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(final String env) throws UnsupportedEncodingException {
    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public long getContentLengthLong() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public String getParameter(final String name) {
        return null;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(final String name) {
        return null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(final String name, final Object o) {
    }

    @Override
    public void removeAttribute(final String name) {
    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(final String path) {
        return null;
    }

    @Override
    public String getRealPath(final String path) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(final ServletRequest servletRequest, final ServletResponse servletResponse)
            throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }

    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public long getDateHeader(final String name) {
        return 0;
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
    public int getIntHeader(final String name) {
        return 0;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return null;
    }

    @Override
    public String getQueryString() {
        return requestUri.getQuery();
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(final String role) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return null;
    }

    @Override
    public String getServletPath() {
        return null;
    }

    @Override
    public HttpSession getSession(final boolean create) {
        return null;
    }

    @Override
    public HttpSession getSession() {
        return null;
    }

    @Override
    public String changeSessionId() {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    @Override
    public boolean authenticate(final HttpServletResponse response) throws IOException, ServletException {
        return false;
    }

    @Override
    public void login(final String username, final String password) throws ServletException {
    }

    @Override
    public void logout() throws ServletException {
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
    public Part getPart(final String name) throws IOException, ServletException {
        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(final Class<T> handlerClass) throws IOException, ServletException {
        return null;
    }

    private static class MockPart implements Part {
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
