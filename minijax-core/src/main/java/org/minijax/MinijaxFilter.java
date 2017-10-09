package org.minijax;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MinijaxFilter implements Filter {

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        // Nothing to do
    }

    @Override
    public void doFilter(
            final ServletRequest servletRequest,
            final ServletResponse servletResponse,
            final FilterChain chain)
                    throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        try (final MinijaxRequestContext context = new MinijaxServletRequestContext(request)) {
            request.setAttribute("org.minijax.MinijaxContainerRequestContext", context);
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // Nothing to do
    }
}
