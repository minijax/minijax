package org.minijax.client;

import static jakarta.ws.rs.HttpMethod.*;
import static jakarta.ws.rs.core.MediaType.*;
import static org.junit.Assert.*;
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
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;

import org.junit.Before;
import org.junit.Test;
import org.minijax.rs.util.CacheControlUtils;

public class InvocationBuilderTest {
    private MinijaxClient client;

    @Before
    public void setUp() throws IOException, InterruptedException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello world".getBytes(StandardCharsets.UTF_8));

        final HttpHeaders httpHeaders = HttpHeaders.of(Map.of("Content-Type", Arrays.asList("text/plain")), (x, y) -> true);

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
    public void testHead() {
        assertNotNull(target("/").request().head());
    }

    @Test
    public void testOptions() {
        assertNotNull(target("/").request().options());
    }

    @Test
    public void testOptionsClass() {
        assertNotNull(target("/").request().options(String.class));
    }

    @Test
    public void testOptionsGenericType() {
        assertNotNull(target("/").request().options(new GenericType<String>() {}));
    }

    @Test
    public void testGet() {
        assertNotNull(target("/").request().get());
    }

    @Test
    public void testGetClass() {
        assertNotNull(target("/").request().get(String.class));
    }

    @Test
    public void testGetGenericType() {
        assertNotNull(target("/").request().get(new GenericType<String>() {}));
    }

    @Test
    public void testDelete() {
        assertNotNull(target("/").request().delete());
    }

    @Test
    public void testDeleteClass() {
        assertNotNull(target("/").request().delete(String.class));
    }

    @Test
    public void testDeleteGenericType() {
        assertNotNull(target("/").request().delete(new GenericType<String>() {}));
    }

    @Test
    public void testPost() {
        assertNotNull(target("/").request().post(null));
    }

    @Test
    public void testPostClass() {
        assertNotNull(target("/").request().post(null, String.class));
    }

    @Test
    public void testPostGenericType() {
        assertNotNull(target("/").request().post(null, new GenericType<String>() {}));
    }

    @Test
    public void testPut() {
        assertNotNull(target("/").request().put(null));
    }

    @Test
    public void testPutClass() {
        assertNotNull(target("/").request().put(null, String.class));
    }

    @Test
    public void testPutGenericType() {
        assertNotNull(target("/").request().put(null, new GenericType<String>() {}));
    }

    @Test
    public void testTrace() {
        assertNotNull(target("/").request().trace());
    }

    @Test
    public void testTraceClass() {
        assertNotNull(target("/").request().trace(String.class));
    }

    @Test
    public void testTraceGenericType() {
        assertNotNull(target("/").request().trace(new GenericType<String>() {}));
    }

    @Test
    public void testBuild() {
        assertNotNull(target("/").request().build("GET"));
    }

    @Test
    public void testBuild2() {
        assertNotNull(target("/").request().build("GET", null));
    }

    @Test
    public void testBuildGet() {
        assertNotNull(target("/").request().buildGet());
    }

    @Test
    public void testBuildDelete() {
        assertNotNull(target("/").request().buildDelete());
    }

    @Test
    public void testBuildPost() {
        assertNotNull(target("/").request().buildPost(null));
    }

    @Test
    public void testBuildPut() {
        assertNotNull(target("/").request().buildPut(null));
    }

    @Test
    public void testMethod() {
        assertNotNull(target("/").request().method(GET));
    }

    @Test
    public void testMethodClass() {
        assertNotNull(target("/").request().method(GET, String.class));
    }

    @Test
    public void testMethodGenericType() {
        assertNotNull(target("/").request().method(GET, new GenericType<String>() {}));
    }

    @Test
    public void testMethodEntity() {
        assertNotNull(target("/").request().method(POST, Entity.text("Hello")));
    }

    @Test
    public void testMethodEntityClass() {
        assertNotNull(target("/").request().method(POST, Entity.text("Hello"), String.class));
    }

    @Test
    public void testMethodEntityGenericType() {
        assertNotNull(target("/").request().method(POST, Entity.text("Hello"), new GenericType<String>() {}));
    }

    @Test
    public void testHeader() {
        assertNotNull(target("/").request().header("foo", "bar"));
    }

    @Test
    public void testHeaders() {
        final MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        headers.add("foo", "bar");
        assertNotNull(target("/").request().headers(headers));
    }

    @Test
    public void testAccept1() {
        assertEquals("text/plain, text/html", target("/").request().accept("text/plain, text/html").getHttpRequest().build().headers().firstValue("Accept").get());
    }

    @Test
    public void testAccept2() {
        assertEquals("text/plain, text/html", target("/").request().accept(TEXT_PLAIN_TYPE, TEXT_HTML_TYPE).getHttpRequest().build().headers().firstValue("Accept").get());
    }

    @Test
    public void testAcceptLanguage1() {
        assertEquals("en-US, en-GB", target("/").request().acceptLanguage("en-US", "en-GB").getHttpRequest().build().headers().firstValue("Accept-Language").get());
    }

    @Test
    public void testAcceptLanguage2() {
        assertEquals("en-US, en-GB", target("/").request().acceptLanguage(Locale.US, Locale.UK).getHttpRequest().build().headers().firstValue("Accept-Language").get());
    }

    @Test
    public void testAcceptEncoding() {
        assertEquals("gzip", target("/").request().acceptEncoding("gzip").getHttpRequest().build().headers().firstValue("Accept-Encoding").get());
    }

    @Test
    public void testCookie1() {
        assertEquals("a=\"b\"", target("/").request().cookie(new Cookie("a", "b")).getHttpRequest().build().headers().firstValue("Cookie").get());
    }

    @Test
    public void testCookie2() {
        assertEquals("a=\"b\"", target("/").request().cookie("a", "b").getHttpRequest().build().headers().firstValue("Cookie").get());
    }

    @Test
    public void testCacheControl() {
        assertEquals("public", target("/").request().cacheControl(CacheControlUtils.fromString("public")).getHttpRequest().build().headers().firstValue("Cache-Control").get());
    }

    /*
     * Unsupported
     */

    @Test(expected = UnsupportedOperationException.class)
    public void testProperty() {
        target("/").request().property(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAsync() {
        target("/").request().async();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRx() {
        target("/").request().rx();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRxClass() {
        target("/").request().rx(null);
    }
}
