package org.minijax;

public class MinijaxProperties {
    public static final String PERSISTENCE_UNIT_NAME = "org.minijax.data.persistenceUnitName";
    public static final String DB_DRIVER = "org.minijax.data.driver";
    public static final String DB_URL = "org.minijax.data.url";
    public static final String DB_USERNAME = "org.minijax.data.username";
    public static final String DB_PASSWORD = "org.minijax.data.password"; // NOSONAR
    public static final String DB_REFERENCE_URL = "org.minijax.data.referenceUrl";

    public static final String SSL_KEY_STORE_PATH = "org.minijax.ssl.keyStorePath";
    public static final String SSL_KEY_STORE_PASSWORD = "org.minijax.ssl.keyStorePassword";
    public static final String SSL_KEY_MANAGER_PASSWORD = "org.minijax.ssl.keyManagerPassword";

    MinijaxProperties() {
        throw new UnsupportedOperationException();
    }
}
