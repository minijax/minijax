package org.minijax.test;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.http.Cookie;

import org.junit.Test;

public class MockHttpServletResponseTest {

    @Test
    public void testAddCookie() {
        final Cookie c = new Cookie("k", "v");

        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.addCookie(c);
        assertTrue(r.getCookies().contains(c));
    }

    @Test
    public void testAddHeader() {
        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.addHeader("k", "v");
        assertEquals("v", r.getHeader("k"));
        assertTrue(r.containsHeader("k"));
    }

    @Test
    public void testAddIntHeader() {
        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.addIntHeader("k", 100);
        assertEquals(100, Integer.parseInt(r.getHeader("k")));
        assertTrue(r.containsHeader("k"));
    }

    @Test
    public void testGetSetContentType() {
        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.setContentType("text/plain");
        assertEquals("text/plain", r.getContentType());
    }

    @Test
    public void testGetSetStatus() {
        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.setStatus(404);
        assertEquals(404, r.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetCharacterEncoding() {
        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.getCharacterEncoding();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetCharacterEncoding() {
        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.setCharacterEncoding(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetBufferSize() {
        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.setBufferSize(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetBufferSize() {
        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.getBufferSize();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFlushBuffer() throws IOException {
        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.flushBuffer();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testResetBuffer() {
        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.resetBuffer();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsCommitted() {
        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.isCommitted();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReset() {
        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.reset();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetLocale() {
        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.setLocale(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLocale() {
        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.getLocale();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEncodeURL() {
        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.encodeURL(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEncodeRedirectURL() {
        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.encodeRedirectURL(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEncodeUrl() {
        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.encodeUrl(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEncodeRedirectUrl() {
        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.encodeRedirectUrl(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSendError1() throws IOException {
        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.sendError(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSendError2() throws IOException {
        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.sendError(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSendRedirect() throws IOException {
        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.sendRedirect(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetDateHeader() {
        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.setDateHeader(null, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddDateHeader() {
        final MockHttpServletResponse r = new MockHttpServletResponse();
        r.addDateHeader(null, 0);
    }
}
