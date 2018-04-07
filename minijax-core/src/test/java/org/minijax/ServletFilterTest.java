package org.minijax;

import static javax.ws.rs.HttpMethod.*;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.junit.Test;
import org.minijax.test.MockHttpServletRequest;
import org.minijax.test.MockHttpServletResponse;

public class ServletFilterTest {

    @Test
    public void testServletFilterMissingContext() throws ServletException, IOException {
        final Minijax minijax = new Minijax();
        final MinijaxApplication application = minijax.getDefaultApplication();

        final MinijaxServlet servlet = new MinijaxServlet(application);

        final MockFilterChain chain = new MockFilterChain(servlet);

        final MinijaxFilter filter = new MinijaxFilter(application);
        filter.init(null);

        final MockHttpServletRequest request = new MockHttpServletRequest(GET, URI.create("/"));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, chain);

        assertTrue(chain.success);
    }

    private static class MockFilterChain implements FilterChain {
        private final MinijaxServlet servlet;
        boolean success;

        MockFilterChain(final MinijaxServlet servlet) {
            this.servlet = servlet;
        }

        @Override
        public void doFilter(final ServletRequest request, final ServletResponse response) throws ServletException, IOException {
            servlet.service(request, response);
            success = true;
        }
    }
}
