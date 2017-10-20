package org.minijax;

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
    public void testServletFilter() throws ServletException, IOException {
        final Minijax minijax = new Minijax();

        final MinijaxFilter filter = new MinijaxFilter();
        filter.init(null);

        final MinijaxServlet servlet = new MinijaxServlet(minijax);

        final MockHttpServletRequest request = new MockHttpServletRequest(null, null, "GET", URI.create("/"), null);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockFilterChain chain = new MockFilterChain();
        filter.doFilter(request, response, chain);
        servlet.service(request, response);
        filter.destroy();
    }


    private static class MockFilterChain implements FilterChain {
        @Override
        public void doFilter(final ServletRequest request, final ServletResponse response) {
            // No-op
        }
    }
}
