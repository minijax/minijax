package org.minijax;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class MinijaxServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final Minijax container;

    public MinijaxServlet(final Minijax container) {
        this.container = container;
    }

    @Override
    protected final void service(
            final HttpServletRequest request,
            final HttpServletResponse response)
                    throws IOException {
        container.handle(MinijaxRequestContext.getThreadLocal(), response);
    }
}
