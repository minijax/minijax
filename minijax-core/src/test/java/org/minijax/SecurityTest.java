package org.minijax;

import static org.junit.Assert.*;

import java.security.Principal;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

public class SecurityTest extends MinijaxTest {

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

    @BeforeClass
    public static void setUpSecurityTest() {
        resetServer();
        register(SecurityTest.class);
        register(SecurityTest.Security.class);
    }

    @Test
    public void testHello() {
        assertEquals("Hello world!", target("/").request().get(String.class));
    }

    @Test
    public void testSecretUnauthorized() {
        assertEquals(401, target("/secret").request().get().getStatus());
    }

    @Test
    public void testSecretAllowed() {
        assertEquals(
                "Top secret!",
                target("/secret").request().header("Authorization", "bob").get(String.class));
    }

    @Test
    public void testAdminUnauthorized() {
        assertEquals(401, target("/admin").request().get().getStatus());
    }

    @Test
    public void testAdminForbidden() {
        assertEquals(403, target("/admin").request().header("Authorization", "bob").get().getStatus());
    }

    @Test
    public void testAdminAllowed() {
        assertEquals(
                "Admins only!",
                target("/admin").request().header("Authorization", "alice").get(String.class));
    }
}
