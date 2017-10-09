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
package org.minijax.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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
    public void addDateHeader(final String name, final long date) {
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
    public String encodeRedirectUrl(final String url) {
        return null;
    }

    @Override
    public String encodeRedirectURL(final String url) {
        return null;
    }

    @Override
    public String encodeUrl(final String url) {
        return null;
    }

    @Override
    public String encodeURL(final String url) {
        return null;
    }

    @Override
    public void flushBuffer() throws IOException {
    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
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

    @Override
    public Locale getLocale() {
        return null;
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
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {
    }

    @Override
    public void resetBuffer() {
    }

    @Override
    public void sendError(final int sc) throws IOException {
    }

    @Override
    public void sendError(final int sc, final String msg) throws IOException {
    }

    @Override
    public void sendRedirect(final String location) throws IOException {
    }

    @Override
    public void setBufferSize(final int size) {
    }

    @Override
    public void setCharacterEncoding(final String charset) {
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
    public void setDateHeader(final String name, final long date) {
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
    public void setLocale(final Locale loc) {
    }

    @Override
    public void setStatus(final int sc) {
        status = sc;
    }

    @Override
    public void setStatus(final int sc, final String sm) {
        status = sc;
    }
}
