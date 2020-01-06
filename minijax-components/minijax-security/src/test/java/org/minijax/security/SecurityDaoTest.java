package org.minijax.security;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.core.Cookie;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.commons.IdUtils;
import org.minijax.dao.PersistenceFeature;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.test.MinijaxTest;

public class SecurityDaoTest extends MinijaxTest {

    @BeforeClass
    public static void setUpSecurityDaoTest() {
        register(PersistenceFeature.class);
        register(new SecurityFeature(User.class, Dao.class));
    }

    @Test
    public void testApiKeys() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final Dao dao = ctx.getResource(Dao.class);

            final User user = new User();
            user.setName("Alice");
            user.setEmail("apikeytest@example.com");
            user.setRoles("user");
            dao.create(user);

            final ApiKey k1 = new ApiKey();
            k1.setName("test1");
            k1.setValue("test1test1");
            k1.setUser(user);
            dao.create(k1);

            final ApiKey k2 = new ApiKey();
            k2.setName("test2");
            k2.setValue("test2test2");
            k2.setUser(user);
            dao.create(k2);

            final List<ApiKey> keys = dao.findApiKeysByUser(user);
            assertNotNull(keys);
            assertEquals(2, keys.size());

            final ApiKey k3 = dao.findApiKeyByValue(k1.getValue());
            assertNotNull(k3);
            assertEquals(k1, k3);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDeleteSessionsByUser() throws IOException {
        UserSession s1 = null;
        UserSession s2 = null;

        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final Dao dao = ctx.getResource(Dao.class);

            final User user = new User();
            user.setName("Alice");
            user.setEmail("deletesessions@example.com");
            user.setRoles("user");
            dao.create(user);

            final Security<User> security = ctx.getResource(Security.class);

            final Cookie c1 = security.loginAs(user);
            s1 = dao.read(UserSession.class, IdUtils.tryParse(c1.getValue()));
            assertNotNull(s1);
            assertEquals(user.getId(), s1.getUserId());

            final Cookie c2 = security.loginAs(user);
            s2 = dao.read(UserSession.class, IdUtils.tryParse(c2.getValue()));
            assertNotNull(s2);
            assertEquals(user.getId(), s2.getUserId());

            assertEquals(2, dao.readUserSessionsByUser(user.getId()).size());

            dao.deleteUserSessionsByUser(user.getId());
        }

        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final Dao dao = ctx.getResource(Dao.class);
            assertNull(dao.read(UserSession.class, s1.getId()));
            assertNull(dao.read(UserSession.class, s2.getId()));
        }
    }
}
