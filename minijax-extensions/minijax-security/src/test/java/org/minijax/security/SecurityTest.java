package org.minijax.security;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import javax.persistence.NoResultException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.SecurityContext;

import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.minijax.MinijaxProperties;
import org.minijax.util.IdUtils;

public class SecurityTest {

    @Test
    public void testAnonymous() {
        final SecurityDao dao = mock(SecurityDao.class);
        final Configuration config = mock(Configuration.class);
        final Security<User> security = new Security<>(dao, config, null, null);
        assertFalse(security.isLoggedIn());
        assertNull(security.getCurrentUser());
        assertNull(security.getUserPrincipal());
        assertNull(security.getAuthenticationScheme());
        assertFalse(security.isUserInRole("user"));
        assertTrue(security.isSecure());
    }

    @Test(expected = NotAuthorizedException.class)
    public void testAnonymousRequireLogin() {
        final SecurityDao dao = mock(SecurityDao.class);
        final Configuration config = mock(Configuration.class);
        final Security<User> security = new Security<>(dao, config, null, null);
        security.requireLogin();
    }

    @Test(expected = NotAuthorizedException.class)
    public void testAnonymousValidateSession() {
        final SecurityDao dao = mock(SecurityDao.class);
        final Configuration config = mock(Configuration.class);
        final Security<User> security = new Security<>(dao, config, null, null);
        security.validateSession("foo");
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

        final Configuration config = mock(Configuration.class);
        final Security<User> security = new Security<>(dao, config, authorization, null);
        security.requireLogin();
        assertTrue(security.isLoggedIn());
        assertNotNull(security.getCurrentUser());
        assertEquals(user, security.getCurrentUser());
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
        assertNull(security.getCurrentUser());
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
        assertNull(security.getCurrentUser());
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

        final Configuration config = mock(Configuration.class);
        final Security<User> security = new Security<>(dao, config, null, cookie);
        security.requireLogin();
        security.validateSession(session.getId().toString());
        assertTrue(security.isLoggedIn());
        assertNotNull(security.getCurrentUser());
        assertEquals(user, security.getCurrentUser());
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
        assertNull(security.getCurrentUser());
    }

    @Test
    public void testCookieSessionNotFound() {
        final String cookie = IdUtils.create().toString();
        final SecurityDao dao = mock(SecurityDao.class);
        final Configuration config = mock(Configuration.class);
        final Security<User> security = new Security<>(dao, config, null, cookie);
        assertFalse(security.isLoggedIn());
        assertNull(security.getCurrentUser());
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
        assertNull(security.getCurrentUser());
    }

    @Test(expected = BadRequestException.class)
    public void testInvalidSessionToken() {
        final User user = new User();

        final UserSession session = new UserSession();
        session.setUser(user);

        final String cookie = session.getId().toString();

        final SecurityDao dao = mock(SecurityDao.class);
        when(dao.read(eq(UserSession.class), eq(session.getId()))).thenReturn(session);

        final Configuration config = mock(Configuration.class);
        final Security<User> security = new Security<>(dao, config, null, cookie);
        security.validateSession("not-the-right-token");
    }

    @Test
    public void testLogin() {
        final User user = new User();
        user.setPasswordHash(BCrypt.hashpw("testtest", BCrypt.gensalt()));

        final SecurityDao dao = mock(SecurityDao.class);
        when(dao.findUserByEmail(eq(User.class), eq("user@example.com"))).thenReturn(user);

        final Configuration config = mock(Configuration.class);
        when(config.getProperty(eq(MinijaxProperties.SECURITY_USER_CLASS))).thenReturn(User.class);

        final Security<User> security = new Security<>(dao, config, null, null);
        final NewCookie cookie = security.login("user@example.com", "testtest");
        assertNotNull(cookie);
    }

    @Test(expected = BadRequestException.class)
    public void testLoginUserNotFound() {
        final SecurityDao dao = mock(SecurityDao.class);
        when(dao.findUserByEmail(any(), any())).thenThrow(NoResultException.class);

        final Configuration config = mock(Configuration.class);
        when(config.getProperty(eq(MinijaxProperties.SECURITY_USER_CLASS))).thenReturn(User.class);

        final Security<User> security = new Security<>(dao, config, null, null);
        security.login("user@example.com", "testtest");
    }

    @Test(expected = BadRequestException.class)
    public void testLoginPasswordNotSet() {
        final User user = new User();

        final SecurityDao dao = mock(SecurityDao.class);
        when(dao.findUserByEmail(eq(User.class), eq("user@example.com"))).thenReturn(user);

        final Configuration config = mock(Configuration.class);
        when(config.getProperty(eq(MinijaxProperties.SECURITY_USER_CLASS))).thenReturn(User.class);

        final Security<User> security = new Security<>(dao, config, null, null);
        security.login("user@example.com", "testtest");
    }

    @Test(expected = BadRequestException.class)
    public void testLoginIncorrectPassword() {
        final User user = new User();
        user.setPasswordHash(BCrypt.hashpw("testtest", BCrypt.gensalt()));

        final SecurityDao dao = mock(SecurityDao.class);
        when(dao.findUserByEmail(eq(User.class), eq("user@example.com"))).thenReturn(user);

        final Configuration config = mock(Configuration.class);
        when(config.getProperty(eq(MinijaxProperties.SECURITY_USER_CLASS))).thenReturn(User.class);

        final Security<User> security = new Security<>(dao, config, null, null);
        security.login("user@example.com", "wrong-password");
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

        final Configuration config = mock(Configuration.class);
        final Security<User> security = new Security<>(dao, config, null, cookie);
        final NewCookie newCookie = security.logout();
        assertNotNull(newCookie);
        assertEquals("", newCookie.getValue());

        verify(dao).purge(eq(session));
    }
}
