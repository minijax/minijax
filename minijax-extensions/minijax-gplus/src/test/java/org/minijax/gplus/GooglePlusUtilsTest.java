package org.minijax.gplus;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.minijax.util.IdUtils;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.HttpTransport;

@RunWith(MockitoJUnitRunner.class)
public class GooglePlusUtilsTest {

    @Before
    public void setUp() {
        GooglePlusUtils.setHttpTransport(mock(HttpTransport.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new GooglePlusUtils();
    }

    @Test
    public void testGetLoginUrl() throws IOException {
        final String loginUrl = GooglePlusUtils.getLoginUrl("http://localhost:8080");
        assertTrue(loginUrl.startsWith("https://accounts.google.com/o/oauth2/"));
    }

    @Test
    public void testGetConnectUrl() throws IOException {
        final String loginUrl = GooglePlusUtils.getConnectUrl("http://localhost:8080");
        assertTrue(loginUrl.startsWith("https://accounts.google.com/o/oauth2/"));
    }

    @Test
    public void testLocalhostRedirectUrl() throws MalformedURLException {
        assertEquals("http://localhost:8080/googlecallback", GooglePlusUtils.getRedirectUrl("http://localhost:8080"));
    }

    @Test
    public void testProdRedirectUrl() throws MalformedURLException {
        assertEquals("https://www.ajibot.com/googlecallback", GooglePlusUtils.getRedirectUrl("https://www.ajibot.com"));
    }

    @Test
    public void testStoredCredential() throws IOException {
        final UUID userId = IdUtils.create();

        final TokenResponse response = new TokenResponse();
        response.setAccessToken("myAccessToken");
        response.setRefreshToken("myRefreshToken");
        response.setExpiresInSeconds(3600L);

        GooglePlusUtils.initializeFlow().createAndStoreCredential(response, userId.toString());

        // First time we should get a credential string
        final String encodedCredential = GooglePlusUtils.extractUserCredential(userId);
        assertNotNull(encodedCredential);

        // Second time it should be null
        assertNull(GooglePlusUtils.extractUserCredential(userId));

        // Now we should be able to set it on the user
        final User u = new User();
        u.setId(userId);
        u.setGoogleCredentials(encodedCredential);

        // And now we should be able to get google services
        final Credential credential = GooglePlusUtils.getCredential(u);
        assertNotNull(GooglePlusUtils.getGooglePlus(credential));
    }

    @Test
    public void testNullCredentials() throws IOException {
        final User u = new User();
        assertNull(GooglePlusUtils.getCredential(u));
    }
}
