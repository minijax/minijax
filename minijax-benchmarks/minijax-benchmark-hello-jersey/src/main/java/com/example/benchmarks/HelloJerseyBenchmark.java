package com.example.benchmarks;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class HelloJerseyBenchmark {

    @GET
    public static String hello() {
        return "Hello World";
    }

    public static void main(final String[] args) throws Exception {
        JettyHttpContainerFactory.createServer(
                UriBuilder.fromUri("http://localhost:8080/").build(),
                new ResourceConfig(HelloJerseyBenchmark.class));
    }
}
