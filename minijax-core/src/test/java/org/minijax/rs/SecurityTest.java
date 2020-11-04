package org.minijax.rs;

import static org.junit.jupiter.api.Assertions.*;

import java.security.Principal;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;

class SecurityTest extends MinijaxTest {

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

    @BeforeAll
    public static void setUpSecurityTest() {
        resetServer();
        register(SecurityTest.class);
        register(SecurityTest.Security.class);
    }

    @Test
    void testHello() {
        assertEquals("Hello world!", target("/").request().get(String.class));
    }

    @Test
    void testSecretUnauthorized() {
        assertEquals(401, target("/secret").request().get().getStatus());
    }

    @Test
    void testSecretAllowed() {
        assertEquals(
                "Top secret!",
                target("/secret").request().header("Authorization", "bob").get(String.class));
    }

    @Test
    void testAdminUnauthorized() {
        assertEquals(401, target("/admin").request().get().getStatus());
    }

    @Test
    void testAdminForbidden() {
        assertEquals(403, target("/admin").request().header("Authorization", "bob").get().getStatus());
    }

    @Test
    void testAdminAllowed() {
        assertEquals(
                "Admins only!",
                target("/admin").request().header("Authorization", "alice").get(String.class));
    }
}
