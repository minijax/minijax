package org.minijax.commons;

public class MinijaxProperties {
    public static final String PERSISTENCE_UNIT_NAME = "org.minijax.db.persistenceUnitName";
    public static final String DB_DRIVER = "jakarta.persistence.jdbc.driver";
    public static final String DB_URL = "jakarta.persistence.jdbc.url";
    public static final String DB_USERNAME = "jakarta.persistence.jdbc.user";
    public static final String DB_PASSWORD = "jakarta.persistence.jdbc.password"; // NOSONAR - not a password
    public static final String DB_REFERENCE_URL = "org.minijax.db.referenceUrl";

    public static final String SSL_KEY_STORE_PATH = "org.minijax.ssl.keyStorePath";
    public static final String SSL_KEY_STORE_PASSWORD = "org.minijax.ssl.keyStorePassword"; // NOSONAR - not a password
    public static final String SSL_KEY_MANAGER_PASSWORD = "org.minijax.ssl.keyManagerPassword"; // NOSONAR - not a password

    public static final String SECURITY_USER_CLASS = "org.minijax.security.userClass";

    MinijaxProperties() {
        throw new UnsupportedOperationException();
    }
}
