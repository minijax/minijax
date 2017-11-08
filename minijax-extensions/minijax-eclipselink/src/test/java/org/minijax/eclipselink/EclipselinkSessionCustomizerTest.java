package org.minijax.eclipselink;

import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.persistence.sessions.DatabaseLogin;
import org.eclipse.persistence.sessions.Session;
import org.junit.Test;
import org.minijax.MinijaxProperties;
import org.minijax.eclipselink.EclipselinkSessionCustomizer;

public class EclipselinkSessionCustomizerTest {

    @Test
    public void testCustomizeWithoutDb() {
        final Session session = mock(Session.class);
        final EclipselinkSessionCustomizer customizer = new EclipselinkSessionCustomizer();
        customizer.customize(session);
    }

    @Test
    public void testCustomizeWithDb() {
        final Map<String, Object> props = new HashMap<>();
        props.put(MinijaxProperties.DB_DRIVER, "org.h2.jdbcx.JdbcDataSource");
        props.put(MinijaxProperties.DB_URL, "jdbc:h2:mem:");

        final DatabaseLogin login = mock(DatabaseLogin.class);

        final Session session = mock(Session.class);
        when(session.getProperties()).thenReturn(props);
        when(session.getLogin()).thenReturn(login);

        final EclipselinkSessionCustomizer customizer = new EclipselinkSessionCustomizer();
        customizer.customize(session);
    }
}
