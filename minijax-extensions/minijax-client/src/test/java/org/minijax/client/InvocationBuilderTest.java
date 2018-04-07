package org.minijax.client;

import static javax.ws.rs.HttpMethod.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.junit.Before;
import org.junit.Test;

public class InvocationBuilderTest {
    private MinijaxClient client;

    @Before
    public void setUp() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello world".getBytes(StandardCharsets.UTF_8));

        final Header contentType = new BasicHeader("Content-Type", "text/plain");

        final HttpEntity httpEntity = mock(HttpEntity.class);
        when(httpEntity.getContentType()).thenReturn(contentType);
        when(httpEntity.getContent()).thenReturn(inputStream);

        final CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
        when(httpResponse.getEntity()).thenReturn(httpEntity);

        final CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
        when(httpClient.execute(any())).thenReturn(httpResponse);

        client = new MinijaxClient(httpClient);
    }

    public MinijaxClientWebTarget target(final String uri) {
        return client.target(uri);
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
        assertNotNull(target("/").request().build(null));
    }

    @Test
    public void testBuild2() {
        assertNotNull(target("/").request().build(null, null));
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
        assertEquals("text/plain, text/html", target("/").request().accept("text/plain, text/html").getHttpRequest().getLastHeader("Accept").getValue());
    }

    @Test
    public void testAccept2() {
        assertEquals("text/plain, text/html", target("/").request().accept(MediaType.TEXT_PLAIN_TYPE, MediaType.TEXT_HTML_TYPE).getHttpRequest().getLastHeader("Accept").getValue());
    }

    @Test
    public void testAcceptLanguage1() {
        assertEquals("en-US, en-GB", target("/").request().acceptLanguage("en-US", "en-GB").getHttpRequest().getLastHeader("Accept-Language").getValue());
    }

    @Test
    public void testAcceptLanguage2() {
        assertEquals("en-US, en-GB", target("/").request().acceptLanguage(Locale.US, Locale.UK).getHttpRequest().getLastHeader("Accept-Language").getValue());
    }

    @Test
    public void testAcceptEncoding() {
        assertEquals("gzip", target("/").request().acceptEncoding("gzip").getHttpRequest().getLastHeader("Accept-Encoding").getValue());
    }

    @Test
    public void testCookie1() {
        assertEquals("a=\"b\"", target("/").request().cookie(new Cookie("a", "b")).getHttpRequest().getLastHeader("Cookie").getValue());
    }

    @Test
    public void testCookie2() {
        assertEquals("a=\"b\"", target("/").request().cookie("a", "b").getHttpRequest().getLastHeader("Cookie").getValue());
    }

    @Test
    public void testCacheControl() {
        assertEquals("public", target("/").request().cacheControl(CacheControl.valueOf("public")).getHttpRequest().getLastHeader("Cache-Control").getValue());
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
