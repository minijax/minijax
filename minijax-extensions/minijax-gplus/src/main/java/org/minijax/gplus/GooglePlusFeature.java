package org.minijax.gplus;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;

public class GooglePlusFeature implements Feature {
    private static final Logger LOG = LoggerFactory.getLogger(GooglePlusFeature.class);

    @Override
    public boolean configure(final FeatureContext context) {
        try {
            final JsonFactory jsonFactory = new JacksonFactory();

            final GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                    jsonFactory,
                    new InputStreamReader(GooglePlusFeature.class.getResourceAsStream("/client_id.json")));

            context.register(jsonFactory, JsonFactory.class);
            context.register(clientSecrets, GoogleClientSecrets.class);
            context.register(new MemoryDataStoreFactory(), DataStoreFactory.class);
            context.register(GoogleNetHttpTransport.newTrustedTransport(), HttpTransport.class);
            context.register(GooglePlusCallback.class);
            return true;

        } catch (final GeneralSecurityException ex) {
            LOG.error(ex.getMessage(), ex);
            return false;

        } catch (final IOException ex) {
            LOG.error(ex.getMessage(), ex);
            return false;
        }
    }
}
