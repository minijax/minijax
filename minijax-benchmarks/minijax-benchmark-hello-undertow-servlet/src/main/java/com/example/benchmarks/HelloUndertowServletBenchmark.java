package com.example.benchmarks;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;

public class HelloUndertowServletBenchmark {

    public static class HelloServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;

        @Override
        protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
            resp.getWriter().println("Hello World");
        }
    }

    public static void main(final String[] args) throws Exception {
        final DeploymentInfo servletBuilder = Servlets.deployment()
                .setClassLoader(HelloUndertowServletBenchmark.class.getClassLoader())
                .setContextPath("/")
                .setDeploymentName("test.war")
                .addServlets(Servlets.servlet("HelloServlet", HelloServlet.class).addMapping("/*"));

        final DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
        manager.deploy();

        final PathHandler path = Handlers.path(Handlers.redirect("/"))
                .addPrefixPath("/", manager.start());

        Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(path)
                .build()
                .start();
    }
}
