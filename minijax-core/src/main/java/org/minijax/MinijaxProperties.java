package org.minijax;

public class MinijaxProperties {
    public static final String PERSISTENCE_UNIT_NAME = "org.minijax.db.persistenceUnitName";
    public static final String DB_DRIVER = "javax.persistence.jdbc.driver";
    public static final String DB_URL = "javax.persistence.jdbc.url";
    public static final String DB_USERNAME = "javax.persistence.jdbc.user";
    public static final String DB_PASSWORD = "javax.persistence.jdbc.password"; // NOSONAR - not a password
    public static final String DB_REFERENCE_URL = "org.minijax.db.referenceUrl";

    public static final String HOST = "org.minijax.host";
    public static final String PORT = "org.minijax.port";

    public static final String SSL_KEY_STORE_PATH = "org.minijax.ssl.keyStorePath";
    public static final String SSL_KEY_STORE_PASSWORD = "org.minijax.ssl.keyStorePassword"; // NOSONAR - not a password
    public static final String SSL_KEY_MANAGER_PASSWORD = "org.minijax.ssl.keyManagerPassword"; // NOSONAR - not a password

    public static final String SECURITY_USER_CLASS = "org.minijax.security.userClass";

    public static final String GPLUS_APP_NAME = "org.minijax.gplus.appName";

    MinijaxProperties() {
        throw new UnsupportedOperationException();
    }
}
