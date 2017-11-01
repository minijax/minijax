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

class MinijaxFilter implements Filter {
    private final MinijaxApplication application;

    public MinijaxFilter(final MinijaxApplication container) {
        this.application = container;
    }

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

        // Constructor sets the ThreadLocal
        // Close method clears the ThreadLocal
        try (final MinijaxRequestContext context = new MinijaxRequestContext(application, request, response)) {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // Nothing to do
    }
}
