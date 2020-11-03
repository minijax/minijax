package org.minijax.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Configuration;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.SecurityContext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.commons.IdUtils;
import org.minijax.commons.MinijaxProperties;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.test.MinijaxTest;

public class SecurityTest extends MinijaxTest {
    private MinijaxRequestContext context;

    @BeforeEach
    public void setUp() {
        context = createRequestContext();
    }

    @AfterEach
    public void tearDown() throws IOException {
        context.close();
    }

    @Test
    public void testAnonymous() {
        final SecurityDao dao = mock(SecurityDao.class);
        final Configuration config = mock(Configuration.class);
        final Security<User> security = new Security<>(dao, config, null, null);
        assertFalse(security.isLoggedIn());
        assertNull(security.getUserPrincipal());
        assertNull(security.getUserPrincipal());
        assertNull(security.getAuthenticationScheme());
        assertFalse(security.isUserInRole("user"));
        assertTrue(security.isSecure());
    }

    @Test
    public void testAnonymousRequireLogin() {
        assertThrows(NotAuthorizedException.class, () -> {
        final SecurityDao dao = mock(SecurityDao.class);
        final Configuration config = mock(Configuration.class);
        final Security<User> security = new Security<>(dao, config, null, null);
        security.requireLogin();
    });
    }

    @Test
    public void testAnonymousValidateSession() {
        assertThrows(NotAuthorizedException.class, () -> {
        final SecurityDao dao = mock(SecurityDao.class);
        final Configuration config = mock(Configuration.class);
        final Security<User> security = new Security<>(dao, config, null, null);
        security.validateSession("foo");
    });
    }

    @Test
    public void testApiKey() {
        final User user = new User();

        final ApiKey apiKey = new ApiKey();
        apiKey.setValue("xyz");
        apiKey.setUser(user);

        final String authorization = AuthUtils.create(apiKey.getValue(), "");

        final SecurityDao dao = mock(SecurityDao.class);
        when(dao.findApiKeyByValue(eq("xyz"))).thenReturn(apiKey);
        when(dao.read(eq(User.class), eq(user.getId()))).thenReturn(user);

        final Configuration config = mock(Configuration.class);
        when(config.getProperty(eq(MinijaxProperties.SECURITY_USER_CLASS))).thenReturn(User.class);

        final Security<User> security = new Security<>(dao, config, authorization, null);
        security.requireLogin();
        assertTrue(security.isLoggedIn());
        assertNotNull(security.getUserPrincipal());
        assertEquals(user, security.getUserPrincipal());
        assertEquals(user, security.getUserPrincipal());
        assertEquals(SecurityContext.BASIC_AUTH, security.getAuthenticationScheme());
    }

    @Test
    public void testApiKeyNotFound() {
        final String authorization = AuthUtils.create("xyz", "");
        final SecurityDao dao = mock(SecurityDao.class);
        final Configuration config = mock(Configuration.class);
        final Security<User> security = new Security<>(dao, config, authorization, null);
        assertFalse(security.isLoggedIn());
        assertNull(security.getUserPrincipal());
    }

    @Test
    public void testApiKeyDeleted() {
        final User user = new User();

        final ApiKey apiKey = new ApiKey();
        apiKey.setValue("xyz");
        apiKey.setUser(user);
        apiKey.setDeleted(true);

        final String authorization = AuthUtils.create(apiKey.getValue(), "");

        final SecurityDao dao = mock(SecurityDao.class);
        when(dao.findApiKeyByValue(eq("xyz"))).thenReturn(apiKey);

        final Configuration config = mock(Configuration.class);
        final Security<User> security = new Security<>(dao, config, authorization, null);
        assertFalse(security.isLoggedIn());
        assertNull(security.getUserPrincipal());
    }

    @Test
    public void testCookie() {
        final User user = new User();
        user.setRoles("admin");

        final UserSession session = new UserSession();
        session.setUser(user);

        final String cookie = session.getId().toString();

        final SecurityDao dao = mock(SecurityDao.class);
        when(dao.read(eq(UserSession.class), eq(session.getId()))).thenReturn(session);
        when(dao.read(eq(User.class), eq(user.getId()))).thenReturn(user);

        final Configuration config = mock(Configuration.class);
        when(config.getProperty(eq(MinijaxProperties.SECURITY_USER_CLASS))).thenReturn(User.class);

        final Security<User> security = new Security<>(dao, config, null, cookie);
        security.requireLogin();
        security.validateSession(session.getId().toString());
        assertTrue(security.isLoggedIn());
        assertNotNull(security.getUserPrincipal());
        assertEquals(user, security.getUserPrincipal());
        assertEquals(user, security.getUserPrincipal());
        assertEquals(SecurityContext.FORM_AUTH, security.getAuthenticationScheme());
        assertTrue(security.isUserInRole("admin"));
        assertFalse(security.isUserInRole("foo"));
    }

    @Test
    public void testCookieInvalidUuid() {
        final String cookie = "not-a-uuid";
        final SecurityDao dao = mock(SecurityDao.class);
        final Configuration config = mock(Configuration.class);
        final Security<User> security = new Security<>(dao, config, null, cookie);
        assertFalse(security.isLoggedIn());
        assertNull(security.getUserPrincipal());
    }

    @Test
    public void testCookieSessionNotFound() {
        final String cookie = IdUtils.create().toString();
        final SecurityDao dao = mock(SecurityDao.class);
        final Configuration config = mock(Configuration.class);
        final Security<User> security = new Security<>(dao, config, null, cookie);
        assertFalse(security.isLoggedIn());
        assertNull(security.getUserPrincipal());
    }

    @Test
    public void testCookieInvalidSession() {
        final UserSession session = new UserSession();

        final String cookie = session.getId().toString();

        final SecurityDao dao = mock(SecurityDao.class);
        when(dao.read(eq(UserSession.class), eq(session.getId()))).thenReturn(session);

        final Configuration config = mock(Configuration.class);
        final Security<User> security = new Security<>(dao, config, null, cookie);
        assertFalse(security.isLoggedIn());
        assertNull(security.getUserPrincipal());
    }

    @Test
    public void testInvalidSessionToken() {
        assertThrows(BadRequestException.class, () -> {
        final User user = new User();

        final UserSession session = new UserSession();
        session.setUser(user);

        final String cookie = session.getId().toString();

        final SecurityDao dao = mock(SecurityDao.class);
        when(dao.read(eq(UserSession.class), eq(session.getId()))).thenReturn(session);
        when(dao.read(eq(User.class), eq(user.getId()))).thenReturn(user);

        final Configuration config = mock(Configuration.class);
        when(config.getProperty(eq(MinijaxProperties.SECURITY_USER_CLASS))).thenReturn(User.class);

        final Security<User> security = new Security<>(dao, config, null, cookie);
        security.validateSession("not-the-right-token");
    });
    }

    @Test
    public void testLogin() {
        final User user = new User();
        user.setPassword("testtest");

        final SecurityDao dao = mock(SecurityDao.class);
        when(dao.findUserByEmail(eq(User.class), eq("user@example.com"))).thenReturn(user);

        final Configuration config = mock(Configuration.class);
        when(config.getProperty(eq(MinijaxProperties.SECURITY_USER_CLASS))).thenReturn(User.class);

        final Security<User> security = new Security<>(dao, config, null, null);
        final LoginResult result = security.login("user@example.com", "testtest");
        final NewCookie cookie = result.getCookie();
        assertNotNull(cookie);
    }

    @Test
    public void testLoginUserNotFound() {
        final SecurityDao dao = mock(SecurityDao.class);
        when(dao.findUserByEmail(any(), any())).thenReturn(null);

        final Configuration config = mock(Configuration.class);
        when(config.getProperty(eq(MinijaxProperties.SECURITY_USER_CLASS))).thenReturn(User.class);

        final Security<User> security = new Security<>(dao, config, null, null);
        final LoginResult result = security.login("user@example.com", "testtest");
        assertEquals(LoginResult.Status.NOT_FOUND, result.getStatus());
    }

    @Test
    public void testLoginPasswordNotSet() {
        final User user = new User();

        final SecurityDao dao = mock(SecurityDao.class);
        when(dao.findUserByEmail(eq(User.class), eq("user@example.com"))).thenReturn(user);

        final Configuration config = mock(Configuration.class);
        when(config.getProperty(eq(MinijaxProperties.SECURITY_USER_CLASS))).thenReturn(User.class);

        final Security<User> security = new Security<>(dao, config, null, null);
        final LoginResult result = security.login("user@example.com", "testtest");
        assertEquals(LoginResult.Status.INVALID, result.getStatus());
    }

    @Test
    public void testLoginIncorrectPassword() {
        final User user = new User();
        user.setPassword("testtest");

        final SecurityDao dao = mock(SecurityDao.class);
        when(dao.findUserByEmail(eq(User.class), eq("user@example.com"))).thenReturn(user);

        final Configuration config = mock(Configuration.class);
        when(config.getProperty(eq(MinijaxProperties.SECURITY_USER_CLASS))).thenReturn(User.class);

        final Security<User> security = new Security<>(dao, config, null, null);
        final LoginResult result = security.login("user@example.com", "wrong-password");
        assertEquals(LoginResult.Status.INCORRECT, result.getStatus());
    }

    @Test
    public void testAnonymousLogout() {
        final SecurityDao dao = mock(SecurityDao.class);
        final Configuration config = mock(Configuration.class);
        final Security<User> security = new Security<>(dao, config, null, null);
        final NewCookie cookie = security.logout();
        assertNotNull(cookie);
        assertEquals("", cookie.getValue());
    }

    @Test
    public void testLogout() {
        final User user = new User();

        final UserSession session = new UserSession();
        session.setUser(user);

        final String cookie = session.getId().toString();

        final SecurityDao dao = mock(SecurityDao.class);
        when(dao.read(eq(UserSession.class), eq(session.getId()))).thenReturn(session);
        when(dao.read(eq(User.class), eq(user.getId()))).thenReturn(user);

        final Configuration config = mock(Configuration.class);
        when(config.getProperty(eq(MinijaxProperties.SECURITY_USER_CLASS))).thenReturn(User.class);

        final Security<User> security = new Security<>(dao, config, null, cookie);
        final NewCookie newCookie = security.logout();
        assertNotNull(newCookie);
        assertEquals("", newCookie.getValue());

        verify(dao).purge(eq(session));
    }
}
