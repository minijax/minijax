package com.example.benchmarks;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class HelloJettyServletBenchmark {

    public static class HelloServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;

        @Override
        protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
            resp.getWriter().println("Hello World");
        }
    }

    public static void main(final String[] args) throws Exception {
        final ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        context.addServlet(HelloServlet.class, "/*");

        final Server server = new Server(8080);
        server.setHandler(context);
        server.start();
        server.join();
    }
}
