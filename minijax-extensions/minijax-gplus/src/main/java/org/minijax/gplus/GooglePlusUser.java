package org.minijax.gplus;

import java.util.UUID;

public interface GooglePlusUser {

    UUID getId();

    String getGoogleCredentials();

    void setGoogleCredentials(final String googleCredentials);
}
