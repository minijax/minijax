package org.minijax.gplus;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.IOUtils;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusScopes;

/**
 * Google helper utilities.
 *
 * Based on:
 * https://github.com/google/google-api-java-client-samples
 * plus-preview-appengine-sample/src/main/java/com/google/api/services/samples/plus/Utils.java
 */
public class GooglePlusUtils {
    private static final Logger LOG = LoggerFactory.getLogger(GooglePlusUtils.class);
    private static final String APP_NAME = "Ajibot";
    private static final List<String> SCOPES = Collections.unmodifiableList(Arrays.asList(
            PlusScopes.PLUS_ME, PlusScopes.USERINFO_EMAIL, PlusScopes.USERINFO_PROFILE
        ));
    private static GoogleClientSecrets clientSecrets;
    private static JacksonFactory jsonFactory;
    private static DataStoreFactory dataStoreFactory;
    static HttpTransport httpTransport;


    GooglePlusUtils() {
        throw new UnsupportedOperationException();
    }


    /**
     * Returns a URL that can be used for login.
     *
     * Note the difference between "login" and "connect"!
     *
     * 1) Login - Just enough to authenticate the user
     * 2) Connect - Full access to email and calendar
     *
     * The OAuth handshake is more heavy weight for "connect", so we only do it when we have to.
     *
     * @param currentUrl
     * @return
     * @throws IOException
     */
    public static String getLoginUrl(final String currentUrl) throws IOException {
        final GoogleAuthorizationCodeFlow authFlow = new GoogleAuthorizationCodeFlow.Builder(
                        getHttpTransport(), getJsonFactory(), getClientSecrets(), SCOPES)
                .setDataStoreFactory(getDataStoreFactory())
                .build();
        return authFlow.newAuthorizationUrl()
                .setRedirectUri(getRedirectUrl(currentUrl))
                .setState(currentUrl)
                .build();
    }


    /**
     * Initializes a Google OAuth code flow.
     */
    public static GoogleAuthorizationCodeFlow initializeFlow() throws IOException {
        return new GoogleAuthorizationCodeFlow.Builder(getHttpTransport(), getJsonFactory(), getClientSecrets(), SCOPES)
                .setDataStoreFactory(getDataStoreFactory())
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .build();
    }


    /**
     * Returns the Google authentication URL to "connect".
     *
     * Note the difference between "login" and "connect"!
     *
     * 1) Login - Just enough to authenticate the user
     * 2) Connect - Full access to email and calendar
     *
     * The OAuth handshake is more heavy weight for "connect", so we only do it when we have to.
     *
     * @param currentUrl The current request URL (to determine appropriate redirect URL).
     * @return The Google authentication URL.
     * @throws IOException
     */
    public static String getConnectUrl(final String currentUrl) throws IOException {
        final GoogleAuthorizationCodeFlow authFlow = initializeFlow();
        return authFlow.newAuthorizationUrl()
                .setRedirectUri(getRedirectUrl(currentUrl))
                .setState(currentUrl)
                .build();
    }


    /**
     * Returns the redirect URL.
     *
     * @param currentUrl The URL of the current request.
     * @return The redirect URL.
     */
    public static String getRedirectUrl(final String currentUrl) throws MalformedURLException {
        // See https://console.developers.google.com
        // Go to "Credentials" page
        // Go to "Authorized redirect URIs" section
        final URL url = new URL(currentUrl);
        return url.getProtocol() + "://" + url.getAuthority() + "/googlecallback";
    }


    /**
     * Removes the user credential from the StoredCredential DataStore,
     * and returns it as a serialized string.
     *
     * This is used to move a credential from ajibot-web to ajibot-worker.
     * Credentials cannot be used simultaneously in multiple processes because
     * it breaks the refresh mechanics.
     *
     * @param userId The user ID.
     * @return The serialized credential.
     * @throws IOException
     */
    public static String extractUserCredential(final UUID userId) throws IOException {
        final DataStore<StoredCredential> dataStore = getCredentialDataStore();

        final StoredCredential storedCredential = dataStore.get(userId.toString());
        if (storedCredential == null) {
            return null;
        }

        dataStore.delete(userId.toString());
        return Base64.getEncoder().encodeToString(IOUtils.serialize(storedCredential));
    }


    /**
     * Inserts the user credential into the StoredCredential DataStore.
     *
     * This is used to move a credential from ajibot-web to ajibot-worker.
     * Credentials cannot be used simultaneously in multiple processes because
     * it breaks the refresh mechanics.
     *
     * @param userId The user ID.
     * @param encodedCredential The serialized credential.
     * @throws IOException
     */
    private static void insertUserCredential(final UUID userId, final String encodedCredential) throws IOException {
        final StoredCredential storedCredential = IOUtils.deserialize(Base64.getDecoder().decode(encodedCredential));
        getCredentialDataStore().set(userId.toString(), storedCredential);
    }


    /**
     * Returns the fully populated credential object for a user.
     *
     * @param user The user.
     * @return The credential object if available; null otherwise.
     */
    public static Credential getCredential(final GooglePlusUser user) throws IOException {
        if (user.getGoogleCredentials() == null) {
            return null;
        }

        // Check if we have stored credentials using the Authorization Flow.
        // Note that we only check if there are stored credentials, but not if they are still valid.
        // The user may have revoked authorization, in which case we would need to go through the
        // authorization flow again, which this implementation does not handle.
        final GoogleAuthorizationCodeFlow authFlow = GooglePlusUtils.initializeFlow();

        // Insert the user credentials from the database into the google flow
        GooglePlusUtils.insertUserCredential(user.getId(), user.getGoogleCredentials());

        return  authFlow.loadCredential(user.getId().toString());
    }


    /**
     * Returns a Google+ client.
     *
     * WARNING:  Only call this once per "session" (either page view or worker iteration).
     * This is a heavy operation.  The calendar object can be reused for as long as the
     * access token is valid.
     *
     * @param credential The user credential.
     * @return A Google+ client.
     */
    public static Plus getGooglePlus(final Credential credential) {
        return new Plus.Builder(getHttpTransport(), getJsonFactory(), credential)
                .setApplicationName(APP_NAME)
                .build();
    }


    /*
     * Private helpers
     */


    private static GoogleClientSecrets getClientSecrets() throws IOException {
        if (clientSecrets == null) {
            clientSecrets = GoogleClientSecrets.load(
                    getJsonFactory(),
                    new InputStreamReader(GooglePlusUtils.class.getResourceAsStream("/client_id.json")));
        }

        return clientSecrets;
    }


    /**
     * Returns a Google JSON Factory.
     */
    private static synchronized JsonFactory getJsonFactory() {
        if (jsonFactory == null) {
            jsonFactory = new JacksonFactory();
        }

        return jsonFactory;
    }


    /**
     * Returns a Google HTTP Transport.
     */
    public static synchronized HttpTransport getHttpTransport() {
        if (httpTransport == null) {
            try {
                httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            } catch (GeneralSecurityException | IOException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        }

        return httpTransport;
    }


    /**
     * Sets the Google HTTP Transport.
     *
     * For testing only.
     *
     * @param httpTransport The HTTP transport.
     */
    public static synchronized void setHttpTransport(final HttpTransport httpTransport) {
        GooglePlusUtils.httpTransport = httpTransport;
    }


    /**
     * Returns a Google DataStoreFactory.
     */
    private static synchronized DataStoreFactory getDataStoreFactory() {
        if (dataStoreFactory == null) {
            dataStoreFactory = new MemoryDataStoreFactory();
        }

        return dataStoreFactory;
    }


    /**
     * Returns the singleton StoredCredential DataStore.
     */
    private static DataStore<StoredCredential> getCredentialDataStore() throws IOException {
        return StoredCredential.getDefaultDataStore(getDataStoreFactory());
    }
}
