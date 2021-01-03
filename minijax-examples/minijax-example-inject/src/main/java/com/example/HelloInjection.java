package com.example;

import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.ext.Provider;

import org.minijax.Minijax;

public class HelloInjection {

    public interface MyService {

        String shout(String str);
    }

    @Provider
    public class MyServiceImpl implements MyService {

        @Override
        public String shout(final String str) {
            return str.toUpperCase();
        }
    }

    @Path("/")
    public class MyResource {

        @Inject
        private MyService service;

        @GET
        public String get(@QueryParam("name") @DefaultValue("friend") final String name) {
            return "Hello " + service.shout(name);
        }
    }

    public static void main(final String[] args) {
        new Minijax()
                .bind(MyServiceImpl.class, MyService.class)
                .register(MyResource.class)
                .start();
    }
}
