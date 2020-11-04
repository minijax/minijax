package org.minijax.client;

import static jakarta.ws.rs.HttpMethod.*;
import static jakarta.ws.rs.core.MediaType.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.rs.util.CacheControlUtils;

class InvocationBuilderTest {
    private MinijaxClient client;

    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello world".getBytes(StandardCharsets.UTF_8));

        final HttpHeaders httpHeaders = HttpHeaders.of(Map.of("Content-Type", Collections.singletonList("text/plain")), (x, y) -> true);

        @SuppressWarnings("unchecked")
        final HttpResponse<InputStream> httpResponse = mock(HttpResponse.class);
        when(httpResponse.headers()).thenReturn(httpHeaders);
        when(httpResponse.body()).thenReturn(inputStream);

        final HttpClient httpClient = mock(HttpClient.class);
        when(httpClient.send(any(), same(BodyHandlers.ofInputStream()))).thenReturn(httpResponse);

        client = new MinijaxClient(httpClient);
    }

    public MinijaxClientWebTarget target(final String uri) {
        return client.target("http://example.com" + uri);
    }

    @Test
    void testHead() {
        assertNotNull(target("/").request().head());
    }

    @Test
    void testOptions() {
        assertNotNull(target("/").request().options());
    }

    @Test
    void testOptionsClass() {
        assertNotNull(target("/").request().options(String.class));
    }

    @Test
    void testOptionsGenericType() {
        assertNotNull(target("/").request().options(new GenericType<String>() {}));
    }

    @Test
    void testGet() {
        assertNotNull(target("/").request().get());
    }

    @Test
    void testGetClass() {
        assertNotNull(target("/").request().get(String.class));
    }

    @Test
    void testGetGenericType() {
        assertNotNull(target("/").request().get(new GenericType<String>() {}));
    }

    @Test
    void testDelete() {
        assertNotNull(target("/").request().delete());
    }

    @Test
    void testDeleteClass() {
        assertNotNull(target("/").request().delete(String.class));
    }

    @Test
    void testDeleteGenericType() {
        assertNotNull(target("/").request().delete(new GenericType<String>() {}));
    }

    @Test
    void testPost() {
        assertNotNull(target("/").request().post(null));
    }

    @Test
    void testPostClass() {
        assertNotNull(target("/").request().post(null, String.class));
    }

    @Test
    void testPostGenericType() {
        assertNotNull(target("/").request().post(null, new GenericType<String>() {}));
    }

    @Test
    void testPut() {
        assertNotNull(target("/").request().put(null));
    }

    @Test
    void testPutClass() {
        assertNotNull(target("/").request().put(null, String.class));
    }

    @Test
    void testPutGenericType() {
        assertNotNull(target("/").request().put(null, new GenericType<String>() {}));
    }

    @Test
    void testTrace() {
        assertNotNull(target("/").request().trace());
    }

    @Test
    void testTraceClass() {
        assertNotNull(target("/").request().trace(String.class));
    }

    @Test
    void testTraceGenericType() {
        assertNotNull(target("/").request().trace(new GenericType<String>() {}));
    }

    @Test
    void testBuild() {
        assertNotNull(target("/").request().build("GET"));
    }

    @Test
    void testBuild2() {
        assertNotNull(target("/").request().build("GET", null));
    }

    @Test
    void testBuildGet() {
        assertNotNull(target("/").request().buildGet());
    }

    @Test
    void testBuildDelete() {
        assertNotNull(target("/").request().buildDelete());
    }

    @Test
    void testBuildPost() {
        assertNotNull(target("/").request().buildPost(null));
    }

    @Test
    void testBuildPut() {
        assertNotNull(target("/").request().buildPut(null));
    }

    @Test
    void testMethod() {
        assertNotNull(target("/").request().method(GET));
    }

    @Test
    void testMethodClass() {
        assertNotNull(target("/").request().method(GET, String.class));
    }

    @Test
    void testMethodGenericType() {
        assertNotNull(target("/").request().method(GET, new GenericType<String>() {}));
    }

    @Test
    void testMethodEntity() {
        assertNotNull(target("/").request().method(POST, Entity.text("Hello")));
    }

    @Test
    void testMethodEntityClass() {
        assertNotNull(target("/").request().method(POST, Entity.text("Hello"), String.class));
    }

    @Test
    void testMethodEntityGenericType() {
        assertNotNull(target("/").request().method(POST, Entity.text("Hello"), new GenericType<String>() {}));
    }

    @Test
    void testHeader() {
        assertNotNull(target("/").request().header("foo", "bar"));
    }

    @Test
    void testHeaders() {
        final MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        headers.add("foo", "bar");
        assertNotNull(target("/").request().headers(headers));
    }

    @Test
    void testAccept1() {
        assertEquals("text/plain, text/html", target("/").request().accept("text/plain, text/html").getHttpRequest().build().headers().firstValue("Accept").get());
    }

    @Test
    void testAccept2() {
        assertEquals("text/plain, text/html", target("/").request().accept(TEXT_PLAIN_TYPE, TEXT_HTML_TYPE).getHttpRequest().build().headers().firstValue("Accept").get());
    }

    @Test
    void testAcceptLanguage1() {
        assertEquals("en-US, en-GB", target("/").request().acceptLanguage("en-US", "en-GB").getHttpRequest().build().headers().firstValue("Accept-Language").get());
    }

    @Test
    void testAcceptLanguage2() {
        assertEquals("en-US, en-GB", target("/").request().acceptLanguage(Locale.US, Locale.UK).getHttpRequest().build().headers().firstValue("Accept-Language").get());
    }

    @Test
    void testAcceptEncoding() {
        assertEquals("gzip", target("/").request().acceptEncoding("gzip").getHttpRequest().build().headers().firstValue("Accept-Encoding").get());
    }

    @Test
    void testCookie1() {
        assertEquals("a=\"b\"", target("/").request().cookie(new Cookie("a", "b")).getHttpRequest().build().headers().firstValue("Cookie").get());
    }

    @Test
    void testCookie2() {
        assertEquals("a=\"b\"", target("/").request().cookie("a", "b").getHttpRequest().build().headers().firstValue("Cookie").get());
    }

    @Test
    void testCacheControl() {
        assertEquals("public", target("/").request().cacheControl(CacheControlUtils.fromString("public")).getHttpRequest().build().headers().firstValue("Cache-Control").get());
    }

    /*
     * Unsupported
     */

    @Test
    void testProperty() {
        assertThrows(UnsupportedOperationException.class, () -> target("/").request().property(null, null));
    }

    @Test
    void testAsync() {
        assertThrows(UnsupportedOperationException.class, () -> target("/").request().async());
    }

    @Test
    void testRx() {
        assertThrows(UnsupportedOperationException.class, () -> target("/").request().rx());
    }

    @Test
    void testRxClass() {
        assertThrows(UnsupportedOperationException.class, () -> target("/").request().rx(null));
    }
}
