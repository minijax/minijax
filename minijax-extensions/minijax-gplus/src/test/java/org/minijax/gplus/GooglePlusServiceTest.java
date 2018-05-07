package org.minijax.gplus;

import static javax.ws.rs.HttpMethod.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.ws.rs.client.Client;

import org.junit.Before;
import org.junit.Test;
import org.minijax.MinijaxProperties;
import org.minijax.MinijaxRequestContext;
import org.minijax.db.PersistenceFeature;
import org.minijax.s3.MockUploadService;
import org.minijax.s3.UploadService;
import org.minijax.security.SecurityFeature;
import org.minijax.test.MinijaxTest;

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
            assertNotNull(ctx.get(GooglePlusService.class).getLoginUrl());
        }
    }


    @Test
    public void testNullProfile() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            assertNull(ctx.get(GooglePlusService.class).getProfile());
        }
    }
}
