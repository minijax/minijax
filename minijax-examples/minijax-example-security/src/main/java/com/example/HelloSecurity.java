package com.example;

import java.security.Principal;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import org.minijax.Minijax;

public class HelloSecurity {

    static class User implements Principal {
        final String name;
        final String role;

        User(final String name, final String role) {
            this.name = name;
            this.role = role;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    @Provider
    public static class Security implements SecurityContext {
        private final User user;

        @Inject
        public Security(@HeaderParam("Authorization") final String a) {
            if ("alice".equals(a)) {
                user = new User("Alice", "admin");
            } else if ("bob".equals(a)) {
                user = new User("Bob", "user");
            } else {
                user = null;
            }
        }

        @Override
        public Principal getUserPrincipal() {
            return user;
        }

        @Override
        public boolean isUserInRole(final String role) {
            return user != null && (user.role.equals(role) || user.role.equals("admin"));
        }

        @Override
        public boolean isSecure() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getAuthenticationScheme() {
            throw new UnsupportedOperationException();
        }
    }

    @GET
    @Path("/")
    @PermitAll
    public static String hello() {
        return "Hello world!";
    }

    @GET
    @Path("/secret")
    @RolesAllowed("user")
    public static String secret() {
        return "Top secret!";
    }

    @GET
    @Path("/admin")
    @RolesAllowed("admin")
    public static String admin() {
        return "Admins only!";
    }

    public static void main(final String[] args) {
        new Minijax()
                .register(HelloSecurity.class)
                .register(HelloSecurity.Security.class)
                .start();
    }
}
