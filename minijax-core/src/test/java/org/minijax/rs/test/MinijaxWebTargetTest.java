package org.minijax.rs.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;
import org.minijax.Minijax;

class MinijaxWebTargetTest {

    @Test
    void testGetUriBuilder() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null);
        assertNotNull(t.getUriBuilder());
    }

    @Test
    void testGetConfiguration() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(new Minijax());
        assertNotNull(t.getConfiguration());
    }

    @Test
    void testProperty() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null);
        assertThrows(UnsupportedOperationException.class, () -> t.property(null, null));
    }

    @Test
    void testRegister1() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null);
        assertThrows(UnsupportedOperationException.class, () -> t.register(null));
    }

    @Test
    void testRegister2() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null);
        assertThrows(UnsupportedOperationException.class, () -> t.register(null, 0));
    }

    @Test
    void testRegister3() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null);
        assertThrows(UnsupportedOperationException.class, () -> t.register(null, (Class<?>) null));
    }

    @Test
    void testRegister4() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null);
        assertThrows(UnsupportedOperationException.class, () -> t.register(null, (Map<Class<?>, Integer>) null));
    }

    @Test
    void testRegister5() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null);
        assertThrows(UnsupportedOperationException.class, () -> t.register((Object) null));
    }

    @Test
    void testRegister6() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null);
        assertThrows(UnsupportedOperationException.class, () -> t.register((Object) null, 0));
    }

    @Test
    void testRegister7() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null);
        assertThrows(UnsupportedOperationException.class, () -> t.register((Object) null, (Class<?>) null));
    }

    @Test
    void testRegister8() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null);
        assertThrows(UnsupportedOperationException.class, () -> t.register((Object) null, (Map<Class<?>, Integer>) null));
    }

    @Test
    void testResolveTemplate1() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null);
        assertThrows(UnsupportedOperationException.class, () -> t.resolveTemplate(null, null));
    }

    @Test
    void testResolveTemplate2() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null);
        assertThrows(UnsupportedOperationException.class, () -> t.resolveTemplate(null, null, false));
    }

    @Test
    void testResolveTemplateFromEncoded() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null);
        assertThrows(UnsupportedOperationException.class, () -> t.resolveTemplateFromEncoded(null, null));
    }

    @Test
    void testResolveTemplates1() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null);
        assertThrows(UnsupportedOperationException.class, () -> t.resolveTemplates(null));
    }

    @Test
    void testResolveTemplates2() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null);
        assertThrows(UnsupportedOperationException.class, () -> t.resolveTemplates(null, false));
    }

    @Test
    void testResolveTemplatesFromEncoded() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null);
        assertThrows(UnsupportedOperationException.class, () -> t.resolveTemplatesFromEncoded(null));
    }

    @Test
    void testMatrixParam() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null);
        assertThrows(UnsupportedOperationException.class, () -> t.matrixParam(null, (Object[]) null));
    }

    @Test
    void testRequest1() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null);
        assertThrows(UnsupportedOperationException.class, () -> t.request((String[]) null));
    }

    @Test
    void testRequest2() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null);
        assertThrows(UnsupportedOperationException.class, () -> t.request((MediaType[]) null));
    }
}
