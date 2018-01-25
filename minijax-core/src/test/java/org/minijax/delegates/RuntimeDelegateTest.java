package org.minijax.delegates;

import static org.junit.Assert.*;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.RuntimeDelegate;
import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

import org.junit.Test;
import org.minijax.uri.JerseyUriBuilder;

public class RuntimeDelegateTest {

    @Test
    public void testCreateUriBuilder() {
        final UriBuilder ub = RuntimeDelegate.getInstance().createUriBuilder();
        assertNotNull(ub);
        assertTrue(ub instanceof JerseyUriBuilder);
    }

    @Test
    public void testCreateResponseBuilder() {
        final ResponseBuilder rb = RuntimeDelegate.getInstance().createResponseBuilder();
        assertNotNull(rb);
    }

    @Test
    public void testCreateMediaTypeDelegate() {
        final HeaderDelegate<MediaType> d = RuntimeDelegate.getInstance().createHeaderDelegate(MediaType.class);
        assertNotNull(d);
        assertTrue(d instanceof MinijaxMediaTypeDelegate);
    }

    @Test
    public void testCreateCookieDelegate() {
        final HeaderDelegate<Cookie> d = RuntimeDelegate.getInstance().createHeaderDelegate(Cookie.class);
        assertNotNull(d);
        assertTrue(d instanceof MinijaxCookieDelegate);
    }

    @Test
    public void testCreateNewCookieDelegate() {
        final HeaderDelegate<NewCookie> d = RuntimeDelegate.getInstance().createHeaderDelegate(NewCookie.class);
        assertNotNull(d);
        assertTrue(d instanceof MinijaxNewCookieDelegate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUnknownDelegate() {
        RuntimeDelegate.getInstance().createHeaderDelegate(Object.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateVariantListBuilder() {
        RuntimeDelegate.getInstance().createVariantListBuilder();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateEndpoint() {
        RuntimeDelegate.getInstance().createEndpoint(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateLinkBuilder() {
        RuntimeDelegate.getInstance().createLinkBuilder();
    }
}
