package com.example.benchmarks;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

public class HelloTomcatBenchmark {

    public static class HelloServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;

        @Override
        protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
            resp.getWriter().println("Hello World");
        }
    }

    public static void main(final String[] args) throws Exception {
        final Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        final Context ctx = tomcat.addContext("", new File(".").getAbsolutePath());
        Tomcat.addServlet(ctx, "Embedded", new HelloServlet());
        ctx.addServletMappingDecoded("/*", "Embedded");

        tomcat.start();
        tomcat.getServer().await();
    }
}
