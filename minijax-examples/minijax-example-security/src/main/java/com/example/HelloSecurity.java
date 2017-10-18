package com.example;

import java.security.Principal;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.minijax.Minijax;

public class HelloSecurity {

    public static class User implements Principal {
        public final String name;
        public final String role;

        public User(final String name, final String role) {
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
                .run(8080);
    }
}
