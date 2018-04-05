package org.minijax.gplus;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.MinijaxRequestContext;
import org.minijax.db.PersistenceFeature;
import org.minijax.s3.MockUploadService;
import org.minijax.s3.UploadService;
import org.minijax.security.Security;
import org.minijax.security.SecurityFeature;
import org.minijax.test.MinijaxTest;

import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.json.Json;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;

public class GooglePlusCallbackTest extends MinijaxTest {
    public static User alice;
    public static Cookie aliceCookie;

    @BeforeClass
    public static void setUpGooglePlusCallbackTest() throws IOException {
        register(PersistenceFeature.class);
        register(new SecurityFeature(User.class, Dao.class));
        register(GooglePlusCallback.class);
        register(mock(Client.class), Client.class);
        register(new MockUploadService(), UploadService.class);

        try (final MinijaxRequestContext ctx = createRequestContext()) {
            alice = new User();
            alice.setName("Alice Smith");
            alice.setHandle("alice");
            alice.setEmail("alice@example.com");
            alice.setPassword("alicepwd");
            alice.setRoles("user");
            ctx.get(Dao.class).create(alice);

            aliceCookie = ctx.get(Security.class).loginAs(alice);
        }
    }


    @Test
    public void testAnonymousGet() throws Exception {
        final Response response = target("/googlecallback").request().get();
        assertNotNull(response);
        assertEquals(303, response.getStatus());
        assertEquals("/docs/google-error", response.getHeaderString("Location"));
    }


    @Test
    public void testBadCode() throws Exception {
        GooglePlusUtils.setHttpTransport(createTransport(false, false, "", ""));

        final Response response = target("http://localhost:8080/googlecallback?code=foo").request().get();
        assertNotNull(response);
        assertEquals(303, response.getStatus());
        assertEquals("/docs/google-error", response.getHeaderString("Location"));
    }


    @Test
    public void testSignin() throws Exception {
        GooglePlusUtils.setHttpTransport(createTransport(true, false, "Alice", "alice@example.com"));

        final Response response = target("http://localhost:8080/googlecallback?code=foo").request().get();
        assertNotNull(response);
        assertEquals(303, response.getStatus());
        assertEquals("/", response.getHeaderString("Location"));
        assertTrue(response.getCookies().containsKey("a"));
    }


    @Test
    public void testConnectWithExisting() throws Exception {
        GooglePlusUtils.setHttpTransport(createTransport(true, true, "Alice", "alice@example.com"));

        final Response response = target("http://localhost:8080/googlecallback?code=foo").request().cookie(aliceCookie).get();
        assertNotNull(response);
        assertEquals(303, response.getStatus());
        assertEquals("/", response.getHeaderString("Location"));
        assertFalse(response.getCookies().containsKey("a"));
    }


    @Test
    public void testUnknownRedirectToBeta() throws Exception {
        GooglePlusUtils.setHttpTransport(createTransport(true, false, "Unknown", "unknown@example.com"));

        final Response response = target("http://localhost:8080/googlecallback?code=foo").request().get();
        assertNotNull(response);
        assertEquals(303, response.getStatus());
        assertEquals("/docs/beta", response.getHeaderString("Location"));
    }


    @Test
    public void testSuccessRedirect() throws Exception {
        GooglePlusUtils.setHttpTransport(createTransport(true, false, "Alice", "alice@example.com"));

        final Response response = target("http://localhost:8080/googlecallback?code=foo&state=/users/reshma").request().get();
        assertNotNull(response);
        assertEquals(303, response.getStatus());
        assertEquals("/users/reshma", response.getHeaderString("Location"));
    }


    private static MockHttpTransport createTransport(final boolean success, final boolean refresh, final String name, final String email) {
        return new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(final String method, final String url) throws IOException {
                return new MockLowLevelHttpRequest() {
                    @Override
                    public LowLevelHttpResponse execute() throws IOException {
                        if (method.equals("POST") && url.equals("https://accounts.google.com/o/oauth2/token")) {
                            final String content;
                            if (success && refresh) {
                                content = "{\"success\":true,\"refresh_token\":\"foo\"}";
                            } else if (success) {
                                content = "{\"success\":true}";
                            } else {
                                content = "{}";
                            }

                            final MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
                            response.setStatusCode(200);
                            response.setContentType(Json.MEDIA_TYPE);
                            response.setContent(content);
                            return response;
                        }

                        if (method.equals("GET") && url.equals("https://www.googleapis.com/plus/v1/people/me")) {
                            final String content;
                            if (success) {
                                content = "{" +
                                        "\"kind\":\"plus#person\"," +
                                        "\"id\":\"108398230704423301333\"," +
                                        "\"displayName\":\"" + name + "\"," +
                                        "\"url\":\"https://plus.google.com/108398230704423301333\"," +
                                        "\"emails\":[{\"value\":\"" + email + "\",\"type\":\"account\"}]," +
                                        "\"image\":{\"url\":\"https://www.example.com/profile.jpg\"}" +
                                        "}";
                            } else {
                                content = "{}";
                            }

                            final MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
                            response.setStatusCode(200);
                            response.setContentType(Json.MEDIA_TYPE);
                            response.setContent(content);
                            return response;
                        }

                        return null;
                    }
                };
            }
        };
    }
}
