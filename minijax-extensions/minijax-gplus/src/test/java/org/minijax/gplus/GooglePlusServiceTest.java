package org.minijax.gplus;

import static javax.ws.rs.HttpMethod.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.Before;
import org.junit.Test;
import org.minijax.MinijaxProperties;
import org.minijax.MinijaxRequestContext;
import org.minijax.MinijaxUriInfo;
import org.minijax.db.PersistenceFeature;
import org.minijax.s3.MockUploadService;
import org.minijax.s3.UploadService;
import org.minijax.security.SecurityFeature;
import org.minijax.test.MinijaxTest;
import org.minijax.test.MinijaxTestHttpHeaders;
import org.minijax.test.MinijaxTestRequestContext;
import org.minijax.util.UrlUtils;

public class GooglePlusServiceTest extends MinijaxTest {

    @Before
    public void setUp() {
        resetServer();
        register(PersistenceFeature.class);
        register(new SecurityFeature(User.class, Dao.class));
        register(GooglePlusCallback.class);
        register(mock(Client.class), Client.class);
        register(new MockUploadService(), UploadService.class);
        register(GooglePlusFeature.class);

        getServer().property(MinijaxProperties.GPLUS_APP_NAME, "Foo App");
    }


    @Test
    public void testAppName() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            assertEquals("Foo App", ctx.get(GooglePlusService.class).getAppName());
        }
    }


    @Test
    public void testLoginUrl() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext(GET, "http://example.com/path/to/test")) {
            final String loginUrl = ctx.get(GooglePlusService.class).getLoginUrl();
            assertNotNull(loginUrl);
        }
    }


    @Test
    public void testNullProfile() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            assertNull(ctx.get(GooglePlusService.class).getProfile());
        }
    }


    @Test
    public void testLoginUrlForwardedProtocol() throws IOException {
        final MinijaxTestHttpHeaders httpHeaders = new MinijaxTestHttpHeaders();
        httpHeaders.getRequestHeaders().add("X-Forwarded-Proto", "https");

        try (final MinijaxRequestContext ctx = new MinijaxTestRequestContext(
                getServer().getDefaultApplication(),
                GET,
                new MinijaxUriInfo(URI.create("http://example.com/path/to/test")),
                httpHeaders,
                null)) {

            final String loginUrl = ctx.get(GooglePlusService.class).getLoginUrl();
            assertNotNull(loginUrl);

            final URI loginUri = URI.create(loginUrl);
            final MultivaluedMap<String, String> queryParams = UrlUtils.urlDecodeMultivaluedParams(loginUri.getQuery());
            final String redirectUri = queryParams.getFirst("redirect_uri");
            assertNotNull(redirectUri);
            assertTrue(redirectUri.startsWith("https://"));
        }
    }
}
