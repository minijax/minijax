package org.minijax.servlet;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Test;

class MinijaxServletTest {

    @ApplicationPath("/")
    public static class TestApp extends Application {

        @Override
        public Map<String, Object> getProperties() {
            return new HashMap<>();
        }

        @Override
        public Set<Class<?>> getClasses() {
            return Collections.singleton(TestResource.class);
        }

        @Override
        public Set<Object> getSingletons() {
            return new HashSet<>();
        }
    }

    @Path("/foo")
    public static class TestResource {

        @GET
        public Response getFoo(@QueryParam("name") @DefaultValue("friend") final String name) {
            return Response.ok("Hello " + name, MediaType.TEXT_PLAIN)
                    .header("X-Test-Header", "foo")
                    .build();
        }

        @POST
        public Response handlePost(final String contentBody) {
            return Response.ok("You posted: " + contentBody, MediaType.TEXT_PLAIN).build();
        }
    }

    @Test
    void testInit() throws ServletException {
        assertThrows(ServletException.class, () -> {
        final MinijaxServlet servlet = new MinijaxServlet();
        servlet.init();
    });
    }

    @Test
    void testInitNullParam() throws ServletException {
        assertThrows(ServletException.class, () -> {
        final MinijaxServlet servlet = new MinijaxServlet();
        servlet.init(new TestConfig(null));
    });
    }

    @Test
    void testInitEmptyParam() throws ServletException {
        assertThrows(ServletException.class, () -> {
        final MinijaxServlet servlet = new MinijaxServlet();
        servlet.init(new TestConfig(""));
    });
    }

    @Test
    void testInitNotFoundParam() throws ServletException {
        assertThrows(ServletException.class, () -> {
        final MinijaxServlet servlet = new MinijaxServlet();
        servlet.init(new TestConfig("com.doesnotexist.Foo"));
    });
    }

    @Test
    void testInitParamSuccess() throws ServletException {
        final MinijaxServlet servlet = new MinijaxServlet();
        servlet.init(new TestConfig("org.minijax.servlet.MinijaxServletTest$TestApp"));
    }

    @Test
    void testGet() throws ServletException, IOException {
        final StringBuffer url = new StringBuffer("https://www.example.com/foo");
        final ServletOutputStream outputStream = mock(ServletOutputStream.class);

        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURL()).thenReturn(url);

        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(outputStream);

        final MinijaxServlet servlet = new MinijaxServlet();
        servlet.init(new TestConfig("org.minijax.servlet.MinijaxServletTest$TestApp"));
        servlet.service(request, response);

        verify(outputStream).write("Hello friend".getBytes());
    }

    @Test
    void testGetQueryString() throws ServletException, IOException {
        final StringBuffer url = new StringBuffer("https://www.example.com/foo");
        final ServletOutputStream outputStream = mock(ServletOutputStream.class);

        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURL()).thenReturn(url);
        when(request.getQueryString()).thenReturn("name=Cody");

        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(outputStream);

        final MinijaxServlet servlet = new MinijaxServlet();
        servlet.init(new TestConfig("org.minijax.servlet.MinijaxServletTest$TestApp"));
        servlet.service(request, response);

        verify(outputStream).write("Hello Cody".getBytes());
    }

    @Test
    void testPost() throws ServletException, IOException {
        final StringBuffer url = new StringBuffer("https://www.example.com/foo");
        final ServletInputStream inputStream = new MockServletInputStream("xyz");
        final ServletOutputStream outputStream = mock(ServletOutputStream.class);

        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURL()).thenReturn(url);
        when(request.getInputStream()).thenReturn(inputStream);

        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(outputStream);

        final MinijaxServlet servlet = new MinijaxServlet();
        servlet.init(new TestConfig("org.minijax.servlet.MinijaxServletTest$TestApp"));
        servlet.service(request, response);

        verify(outputStream).write("You posted: xyz".getBytes());
    }

    public static class TestConfig implements ServletConfig {
        private final String param;

        public TestConfig(final String param) {
            this.param = param;
        }

        @Override
        public String getServletName() {
            return null;
        }

        @Override
        public ServletContext getServletContext() {
            return null;
        }

        @Override
        public String getInitParameter(final String name) {
            return param;
        }

        @Override
        public Enumeration<String> getInitParameterNames() {
            return null;
        }
    }

    public static class MockServletInputStream extends ServletInputStream {
        private final InputStream inputStream;

        public MockServletInputStream(final String content) {
            this.inputStream = new ByteArrayInputStream(content.getBytes());
        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }

        @Override
        public boolean isFinished() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isReady() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setReadListener(final ReadListener readListener) {
            throw new UnsupportedOperationException();
        }
    }
}
