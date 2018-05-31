package com.example;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ext.Provider;

import org.minijax.Minijax;

public class HelloInjection {

    public interface MyService {

        public String shout(String str);
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
                .packages("com.example")
                .start();
    }
}
