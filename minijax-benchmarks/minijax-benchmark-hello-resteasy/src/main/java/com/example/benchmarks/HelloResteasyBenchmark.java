package com.example.benchmarks;

import java.util.Collections;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

public class HelloResteasyBenchmark extends Application {

    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public static class MyResource {

        @GET
        public String hello() {
            return "Hello World";
        }
    }

    @Override
    public Set<Object> getSingletons() {
        return Collections.singleton(new MyResource());
    }

    public static void main(final String[] args) throws Exception {
        final Server server = new Server(8080);
        final ServletContextHandler context = new ServletContextHandler(server, "/");
        final ServletHolder restEasyServlet = new ServletHolder(new HttpServletDispatcher());
        restEasyServlet.setInitParameter("resteasy.servlet.mapping.prefix", "/");
        restEasyServlet.setInitParameter("javax.ws.rs.Application", "com.example.benchmarks.HelloResteasyBenchmark");
        context.addServlet(restEasyServlet, "/*");
        server.start();
        server.join();
    }
}
