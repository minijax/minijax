package org.minijax.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class MockHttpServletResponse implements HttpServletResponse {
    private final List<Cookie> cookies;
    private final MultivaluedMap<String, String> headers;
    private final MockServletOutputStream outputStream;
    private int status;
    private final PrintWriter writer;

    public MockHttpServletResponse() {
        headers = new MultivaluedHashMap<>();
        outputStream = new MockServletOutputStream();
        writer = new PrintWriter(outputStream);
        cookies = new ArrayList<>();
        status = 200;
    }

    @Override
    public void addCookie(final Cookie cookie) {
        cookies.add(cookie);
    }

    @Override
    public void addHeader(final String name, final String value) {
        headers.add(name, value);
    }

    @Override
    public void addIntHeader(final String name, final int value) {
        addHeader(name, Integer.toString(value));
    }

    @Override
    public boolean containsHeader(final String name) {
        return headers.containsKey(name);
    }

    @Override
    public String getContentType() {
        return getHeader("Content-Type");
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    @Override
    public String getHeader(final String name) {
        return headers.getFirst(name);
    }

    @Override
    public Collection<String> getHeaderNames() {
        return Collections.unmodifiableCollection(headers.keySet());
    }

    @Override
    public Collection<String> getHeaders(final String name) {
        return Collections.unmodifiableCollection(headers.get(name));
    }

    public String getOutput() throws IOException {
        writer.flush();
        return outputStream.getValue();
    }

    @Override
    public MockServletOutputStream getOutputStream() throws IOException {
        return outputStream;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return writer;
    }

    @Override
    public void setContentLength(final int len) {
        setIntHeader("Content-Length", len);
    }

    @Override
    public void setContentLengthLong(final long len) {
        setContentLength((int) len);
    }

    @Override
    public void setContentType(final String type) {
        setHeader("Content-Type", type);
    }

    @Override
    public void setHeader(final String name, final String value) {
        headers.putSingle(name, value);
    }

    @Override
    public void setIntHeader(final String name, final int value) {
        setHeader(name, Integer.toString(value));
    }

    @Override
    public void setStatus(final int sc) {
        status = sc;
    }

    @Override
    public void setStatus(final int sc, final String sm) {
        status = sc;
    }

    @Override
    public String getCharacterEncoding() {
        final MediaType mediaType = getMediaType();
        return mediaType == null ? null : mediaType.getParameters().get(MediaType.CHARSET_PARAMETER);
    }

    @Override
    public void setCharacterEncoding(final String charset) {
        final String type;
        final String subType;
        final Map<String, String> parameters;

        final MediaType prevMediaType = getMediaType();
        if (prevMediaType != null) {
            type = prevMediaType.getType();
            subType = prevMediaType.getSubtype();
            parameters = new HashMap<>(prevMediaType.getParameters());
        } else {
            type = "text";
            subType = "html";
            parameters = new HashMap<>();
        }

        parameters.put(MediaType.CHARSET_PARAMETER, charset);
        setContentType(new MediaType(type, subType, parameters).toString());
    }

    private MediaType getMediaType() {
        final List<String> contentType = headers.get("Content-Type");
        if (contentType == null || contentType.isEmpty()) {
            return null;
        }
        return MediaType.valueOf(contentType.get(0));
    }

    @Override
    public void setBufferSize(final int size) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getBufferSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void flushBuffer() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetBuffer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCommitted() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLocale(final Locale loc) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Locale getLocale() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String encodeURL(final String url) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String encodeRedirectURL(final String url) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String encodeUrl(final String url) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String encodeRedirectUrl(final String url) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendError(final int sc, final String msg) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendError(final int sc) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendRedirect(final String location) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDateHeader(final String name, final long date) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addDateHeader(final String name, final long date) {
        throw new UnsupportedOperationException();
    }
}
