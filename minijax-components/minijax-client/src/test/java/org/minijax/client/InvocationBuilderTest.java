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
    private MinijaxClientInvocationBuilder builder;

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
        builder = target("/").request();
    }

    public MinijaxClientWebTarget target(final String uri) {
        return client.target("http://example.com" + uri);
    }

    @Test
    void testHead() {
        assertNotNull(builder.head());
    }

    @Test
    void testOptions() {
        assertNotNull(builder.options());
    }

    @Test
    void testOptionsClass() {
        assertNotNull(builder.options(String.class));
    }

    @Test
    void testOptionsGenericType() {
        assertNotNull(builder.options(new GenericType<String>() {}));
    }

    @Test
    void testGet() {
        assertNotNull(builder.get());
    }

    @Test
    void testGetClass() {
        assertNotNull(builder.get(String.class));
    }

    @Test
    void testGetGenericType() {
        assertNotNull(builder.get(new GenericType<String>() {}));
    }

    @Test
    void testDelete() {
        assertNotNull(builder.delete());
    }

    @Test
    void testDeleteClass() {
        assertNotNull(builder.delete(String.class));
    }

    @Test
    void testDeleteGenericType() {
        assertNotNull(builder.delete(new GenericType<String>() {}));
    }

    @Test
    void testPost() {
        assertNotNull(builder.post(null));
    }

    @Test
    void testPostClass() {
        assertNotNull(builder.post(null, String.class));
    }

    @Test
    void testPostGenericType() {
        assertNotNull(builder.post(null, new GenericType<String>() {}));
    }

    @Test
    void testPut() {
        assertNotNull(builder.put(null));
    }

    @Test
    void testPutClass() {
        assertNotNull(builder.put(null, String.class));
    }

    @Test
    void testPutGenericType() {
        assertNotNull(builder.put(null, new GenericType<String>() {}));
    }

    @Test
    void testTrace() {
        assertNotNull(builder.trace());
    }

    @Test
    void testTraceClass() {
        assertNotNull(builder.trace(String.class));
    }

    @Test
    void testTraceGenericType() {
        assertNotNull(builder.trace(new GenericType<String>() {}));
    }

    @Test
    void testBuild() {
        assertNotNull(builder.build("GET"));
    }

    @Test
    void testBuild2() {
        assertNotNull(builder.build("GET", null));
    }

    @Test
    void testBuildGet() {
        assertNotNull(builder.buildGet());
    }

    @Test
    void testBuildDelete() {
        assertNotNull(builder.buildDelete());
    }

    @Test
    void testBuildPost() {
        assertNotNull(builder.buildPost(null));
    }

    @Test
    void testBuildPut() {
        assertNotNull(builder.buildPut(null));
    }

    @Test
    void testMethod() {
        assertNotNull(builder.method(GET));
    }

    @Test
    void testMethodClass() {
        assertNotNull(builder.method(GET, String.class));
    }

    @Test
    void testMethodGenericType() {
        assertNotNull(builder.method(GET, new GenericType<String>() {}));
    }

    @Test
    void testMethodEntity() {
        assertNotNull(builder.method(POST, Entity.text("Hello")));
    }

    @Test
    void testMethodEntityClass() {
        assertNotNull(builder.method(POST, Entity.text("Hello"), String.class));
    }

    @Test
    void testMethodEntityGenericType() {
        assertNotNull(builder.method(POST, Entity.text("Hello"), new GenericType<String>() {}));
    }

    @Test
    void testHeader() {
        assertNotNull(builder.header("foo", "bar"));
    }

    @Test
    void testHeaders() {
        final MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        headers.add("foo", "bar");
        assertNotNull(builder.headers(headers));
    }

    @Test
    void testAccept1() {
        assertEquals("text/plain, text/html", builder.accept("text/plain, text/html").getHttpRequest().build().headers().firstValue("Accept").get());
    }

    @Test
    void testAccept2() {
        assertEquals("text/plain, text/html", builder.accept(TEXT_PLAIN_TYPE, TEXT_HTML_TYPE).getHttpRequest().build().headers().firstValue("Accept").get());
    }

    @Test
    void testAcceptLanguage1() {
        assertEquals("en-US, en-GB", builder.acceptLanguage("en-US", "en-GB").getHttpRequest().build().headers().firstValue("Accept-Language").get());
    }

    @Test
    void testAcceptLanguage2() {
        assertEquals("en-US, en-GB", builder.acceptLanguage(Locale.US, Locale.UK).getHttpRequest().build().headers().firstValue("Accept-Language").get());
    }

    @Test
    void testAcceptEncoding() {
        assertEquals("gzip", builder.acceptEncoding("gzip").getHttpRequest().build().headers().firstValue("Accept-Encoding").get());
    }

    @Test
    void testCookie1() {
        assertEquals("a=\"b\"", builder.cookie(new Cookie("a", "b")).getHttpRequest().build().headers().firstValue("Cookie").get());
    }

    @Test
    void testCookie2() {
        assertEquals("a=\"b\"", builder.cookie("a", "b").getHttpRequest().build().headers().firstValue("Cookie").get());
    }

    @Test
    void testCacheControl() {
        assertEquals("public", builder.cacheControl(CacheControlUtils.fromString("public")).getHttpRequest().build().headers().firstValue("Cache-Control").get());
    }

    /*
     * Unsupported
     */

    @Test
    void testProperty() {
        assertThrows(UnsupportedOperationException.class, () -> builder.property(null, null));
    }

    @Test
    void testAsync() {
        assertThrows(UnsupportedOperationException.class, () -> builder.async());
    }

    @Test
    void testRx() {
        assertThrows(UnsupportedOperationException.class, () -> builder.rx());
    }

    @Test
    void testRxClass() {
        assertThrows(UnsupportedOperationException.class, () -> builder.rx(null));
    }
}
