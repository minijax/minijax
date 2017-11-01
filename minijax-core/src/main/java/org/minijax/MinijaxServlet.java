package org.minijax;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class MinijaxServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final MinijaxApplication application;

    public MinijaxServlet(final MinijaxApplication container) {
        this.application = container;
    }

    @Override
    protected final void service(
            final HttpServletRequest request,
            final HttpServletResponse response)
                    throws IOException {
        application.handle(MinijaxRequestContext.getThreadLocal(), response);
    }
}
