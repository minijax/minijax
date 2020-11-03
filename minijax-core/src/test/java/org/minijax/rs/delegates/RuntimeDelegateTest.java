package org.minijax.rs.delegates;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.ext.RuntimeDelegate;
import jakarta.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

import org.junit.jupiter.api.Test;
import org.minijax.rs.uri.MinijaxUriBuilder;

public class RuntimeDelegateTest {

    @Test
    public void testCreateUriBuilder() {
        final UriBuilder uriBuilder = RuntimeDelegate.getInstance().createUriBuilder();
        assertNotNull(uriBuilder);
        assertTrue(uriBuilder instanceof MinijaxUriBuilder);
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

    @Test
    public void testCreateUnknownDelegate() {
        assertThrows(IllegalArgumentException.class, () -> RuntimeDelegate.getInstance().createHeaderDelegate(Object.class));
    }

    @Test
    public void testCreateVariantListBuilder() {
        assertThrows(UnsupportedOperationException.class, () -> RuntimeDelegate.getInstance().createVariantListBuilder());
    }

    @Test
    public void testCreateEndpoint() {
        assertThrows(UnsupportedOperationException.class, () -> RuntimeDelegate.getInstance().createEndpoint(null, null));
    }

    @Test
    public void testCreateLinkBuilder() {
        assertThrows(UnsupportedOperationException.class, () -> RuntimeDelegate.getInstance().createLinkBuilder());
    }
}
