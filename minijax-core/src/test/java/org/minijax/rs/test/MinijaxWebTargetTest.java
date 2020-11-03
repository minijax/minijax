package org.minijax.rs.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;

public class MinijaxWebTargetTest {

    @Test
    public void testGetUriBuilder() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
        assertNotNull(t.getUriBuilder());
    }

    @Test
    public void testGetConfiguration() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.getConfiguration();
        });
    }

    @Test
    public void testProperty() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.property(null, null);
        });
    }

    @Test
    public void testRegister1() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.register((Class<?>) null);
        });
    }

    @Test
    public void testRegister2() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.register((Class<?>) null, 0);
        });
    }

    @Test
    public void testRegister3() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.register((Class<?>) null, (Class<?>) null);
        });
    }

    @Test
    public void testRegister4() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.register((Class<?>) null, (Map<Class<?>, Integer>) null);
        });
    }

    @Test
    public void testRegister5() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.register((Object) null);
        });
    }

    @Test
    public void testRegister6() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.register((Object) null, 0);
        });
    }

    @Test
    public void testRegister7() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.register((Object) null, (Class<?>) null);
        });
    }

    @Test
    public void testRegister8() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.register((Object) null, (Map<Class<?>, Integer>) null);
        });
    }

    @Test
    public void testResolveTemplate1() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.resolveTemplate(null, null);
        });
    }

    @Test
    public void testResolveTemplate2() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.resolveTemplate(null, null, false);
        });
    }

    @Test
    public void testResolveTemplateFromEncoded() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.resolveTemplateFromEncoded(null, null);
        });
    }

    @Test
    public void testResolveTemplates1() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.resolveTemplates(null);
        });
    }

    @Test
    public void testResolveTemplates2() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.resolveTemplates(null, false);
        });
    }

    @Test
    public void testResolveTemplatesFromEncoded() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.resolveTemplatesFromEncoded(null);
        });
    }

    @Test
    public void testMatrixParam() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.matrixParam(null, (Object[]) null);
        });
    }

    @Test
    public void testRequest1() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.request((String[]) null);
        });
    }

    @Test
    public void testRequest2() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.request((MediaType[]) null);
        });
    }
}
