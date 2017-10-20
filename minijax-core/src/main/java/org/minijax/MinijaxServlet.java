package org.minijax;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MinijaxServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final Minijax parent;

    public MinijaxServlet(final Minijax parent) {
        this.parent = parent;
    }

    @Override
    protected final void service(
            final HttpServletRequest request,
            final HttpServletResponse response)
                    throws IOException {
        final MinijaxRequestContext context = MinijaxRequestContext.getThreadLocal();
        if (context == null) {
            throw new IllegalStateException("Missing container request context");
        }
        parent.handle(context, response);
    }
}
