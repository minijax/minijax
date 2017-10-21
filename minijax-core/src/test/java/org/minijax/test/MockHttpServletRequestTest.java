package org.minijax.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import javax.servlet.ServletException;

import org.junit.Test;
import org.minijax.test.MockHttpServletRequest.MockPart;

public class MockHttpServletRequestTest {

    @Test
    public void testPathInfo() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/hello"));
        assertEquals("/hello", r.getPathInfo());
        assertEquals("/hello", r.getRequestURI());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAttribute() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getAttribute(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAttributeNames() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getAttributeNames();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetCharacterEncoding() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getCharacterEncoding();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetCharacterEncoding() throws UnsupportedEncodingException {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.setCharacterEncoding(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetContentLength() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getContentLength();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetContentLengthLong() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getContentLengthLong();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetContentType() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getContentType();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetParameter() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getParameter(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetParameterNames() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getParameterNames();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetParameterValues() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getParameterValues(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetParameterMap() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getParameterMap();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetProtocol() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getProtocol();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetScheme() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getScheme();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetServerName() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getServerName();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetServerPort() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getServerPort();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetReader() throws IOException {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getReader();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetRemoteAddr() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getRemoteAddr();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetRemoteHost() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getRemoteHost();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAttribute() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.setAttribute(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAttribute() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.removeAttribute(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLocale() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getLocale();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLocales() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getLocales();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSecure() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.isSecure();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetRequestDispatcher() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getRequestDispatcher(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetRealPath() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getRealPath(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetRemotePort() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getRemotePort();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLocalName() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getLocalName();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLocalAddr() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getLocalAddr();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLocalPort() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getLocalPort();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetServletContext() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getServletContext();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testStartAsync() throws IllegalStateException {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.startAsync();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testStartAsync2() throws IllegalStateException {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.startAsync(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsAsyncStarted() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.isAsyncStarted();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsAsyncSupported() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.isAsyncSupported();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAsyncContext() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getAsyncContext();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDispatcherType() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getDispatcherType();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAuthType() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getAuthType();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDateHeader() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getDateHeader(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetIntHeader() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getIntHeader(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetPathTranslated() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getPathTranslated();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetContextPath() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getContextPath();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetRemoteUser() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getRemoteUser();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsUserInRole() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.isUserInRole(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetUserPrincipal() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getUserPrincipal();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetRequestedSessionId() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getRequestedSessionId();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetServletPath() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getServletPath();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSession() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getSession();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSession2() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getSession(false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChangeSessionId() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.changeSessionId();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsRequestedSessionIdValid() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.isRequestedSessionIdValid();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsRequestedSessionIdFromCookie() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.isRequestedSessionIdFromCookie();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsRequestedSessionIdFromURL() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.isRequestedSessionIdFromURL();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsRequestedSessionIdFromUrl() {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.isRequestedSessionIdFromUrl();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAuthenticate() throws IOException, ServletException {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.authenticate(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLogin() throws ServletException {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.login(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLogout() throws ServletException {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.logout();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetPart() throws IOException, ServletException {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.getPart(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUpgrade() throws IOException, ServletException {
        final MockHttpServletRequest r = new MockHttpServletRequest("GET", URI.create("/"));
        r.upgrade(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPartGetContentType() {
        final MockPart p = new MockPart("k", "v");
        p.getContentType();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPartGetSubmittedFileName() {
        final MockPart p = new MockPart("k", "v");
        p.getSubmittedFileName();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPartGetSize() {
        final MockPart p = new MockPart("k", "v");
        p.getSize();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPartWrite() throws IOException {
        final MockPart p = new MockPart("k", "v");
        p.write(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPartGetHeader() {
        final MockPart p = new MockPart("k", "v");
        p.getHeader(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPartGetHeaders() {
        final MockPart p = new MockPart("k", "v");
        p.getHeaders(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPartGetHeaderNames() {
        final MockPart p = new MockPart("k", "v");
        p.getHeaderNames();
    }
}
