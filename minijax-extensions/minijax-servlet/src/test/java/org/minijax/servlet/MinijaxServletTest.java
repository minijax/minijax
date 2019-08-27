package org.minijax.servlet;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

public class MinijaxServletTest {

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
        public Response getFoo(@QueryParam("k") String k) {
            return Response.ok("Hello world", MediaType.TEXT_PLAIN)
                    .header("X-Test-Header", "foo")
                    .build();
        }
    }

    @Test(expected = ServletException.class)
    public void testInit() throws ServletException {
        final MinijaxServlet servlet = new MinijaxServlet();
        servlet.init();
    }

    @Test(expected = ServletException.class)
    public void testInitNullParam() throws ServletException {
        final MinijaxServlet servlet = new MinijaxServlet();
        servlet.init(new TestConfig(null));
    }

    @Test(expected = ServletException.class)
    public void testInitEmptyParam() throws ServletException {
        final MinijaxServlet servlet = new MinijaxServlet();
        servlet.init(new TestConfig(""));
    }

    @Test(expected = ServletException.class)
    public void testInitNotFoundParam() throws ServletException {
        final MinijaxServlet servlet = new MinijaxServlet();
        servlet.init(new TestConfig("com.doesnotexist.Foo"));
    }

    @Test
    public void testInitParamSuccess() throws ServletException {
        final MinijaxServlet servlet = new MinijaxServlet();
        servlet.init(new TestConfig("org.minijax.servlet.MinijaxServletTest$TestApp"));
    }

    @Test
    public void testGet() throws ServletException, IOException {
        final StringBuffer url = new StringBuffer("https://www.example.com/foo");
        final ServletOutputStream outputStream = mock(ServletOutputStream.class);

        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURL()).thenReturn(url);

        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(outputStream);

        final MinijaxServlet servlet = new MinijaxServlet();
        servlet.init(new TestConfig("org.minijax.servlet.MinijaxServletTest$TestApp"));
        servlet.doGet(request, response);
    }

    @Test
    public void testGetQueryString() throws ServletException, IOException {
        final StringBuffer url = new StringBuffer("https://www.example.com/foo");
        final ServletOutputStream outputStream = mock(ServletOutputStream.class);

        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURL()).thenReturn(url);
        when(request.getQueryString()).thenReturn("k=v");

        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(outputStream);

        final MinijaxServlet servlet = new MinijaxServlet();
        servlet.init(new TestConfig("org.minijax.servlet.MinijaxServletTest$TestApp"));
        servlet.doGet(request, response);
    }

    public static class TestConfig implements ServletConfig {
        private final String param;

        public TestConfig(String param) {
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
        public String getInitParameter(String name) {
            return param;
        }

        @Override
        public Enumeration<String> getInitParameterNames() {
            return null;
        }
    }
}
