package com.example.benchmarks;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class HelloJettyBenchmark {

    public static class Handler extends AbstractHandler {
        @Override
        public void handle(
                final String target,
                final Request baseRequest,
                final HttpServletRequest request,
                final HttpServletResponse response)
                        throws IOException, ServletException {
            response.setContentType("text/plain;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
            response.getWriter().println("Hello World");
        }
    }

    public static void main(final String[] args) throws Exception {
        final Server server = new Server(8080);
        server.setHandler(new Handler());
        server.start();
        server.join();
    }
}
