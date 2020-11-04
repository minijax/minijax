package org.minijax.rs.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;

class MinijaxWebTargetTest {

    @Test
    void testGetUriBuilder() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
        assertNotNull(t.getUriBuilder());
    }

    @Test
    void testGetConfiguration() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.getConfiguration();
        });
    }

    @Test
    void testProperty() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.property(null, null);
        });
    }

    @Test
    void testRegister1() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.register(null);
        });
    }

    @Test
    void testRegister2() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.register(null, 0);
        });
    }

    @Test
    void testRegister3() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.register(null, (Class<?>) null);
        });
    }

    @Test
    void testRegister4() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.register(null, (Map<Class<?>, Integer>) null);
        });
    }

    @Test
    void testRegister5() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.register((Object) null);
        });
    }

    @Test
    void testRegister6() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.register((Object) null, 0);
        });
    }

    @Test
    void testRegister7() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.register((Object) null, (Class<?>) null);
        });
    }

    @Test
    void testRegister8() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.register((Object) null, (Map<Class<?>, Integer>) null);
        });
    }

    @Test
    void testResolveTemplate1() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.resolveTemplate(null, null);
        });
    }

    @Test
    void testResolveTemplate2() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.resolveTemplate(null, null, false);
        });
    }

    @Test
    void testResolveTemplateFromEncoded() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.resolveTemplateFromEncoded(null, null);
        });
    }

    @Test
    void testResolveTemplates1() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.resolveTemplates(null);
        });
    }

    @Test
    void testResolveTemplates2() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.resolveTemplates(null, false);
        });
    }

    @Test
    void testResolveTemplatesFromEncoded() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.resolveTemplatesFromEncoded(null);
        });
    }

    @Test
    void testMatrixParam() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.matrixParam(null, (Object[]) null);
        });
    }

    @Test
    void testRequest1() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.request((String[]) null);
        });
    }

    @Test
    void testRequest2() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
            t.request((MediaType[]) null);
        });
    }
}
