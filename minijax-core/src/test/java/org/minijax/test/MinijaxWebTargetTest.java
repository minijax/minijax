package org.minijax.test;

import static org.junit.Assert.*;

import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

public class MinijaxWebTargetTest {

    @Test
    public void testGetUriBuilder() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
        assertNotNull(t.getUriBuilder());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetConfiguration() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
        t.getConfiguration();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testProperty() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
        t.property(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister1() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
        t.register((Class<?>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister2() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
        t.register((Class<?>) null, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister3() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
        t.register((Class<?>) null, (Class<?>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister4() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
        t.register((Class<?>) null, (Map<Class<?>, Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister5() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
        t.register((Object) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister6() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
        t.register((Object) null, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister7() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
        t.register((Object) null, (Class<?>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegister8() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
        t.register((Object) null, (Map<Class<?>, Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testResolveTemplate1() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
        t.resolveTemplate(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testResolveTemplate2() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
        t.resolveTemplate(null, null, false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testResolveTemplateFromEncoded() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
        t.resolveTemplateFromEncoded(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testResolveTemplates1() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
        t.resolveTemplates(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testResolveTemplates2() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
        t.resolveTemplates(null, false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testResolveTemplatesFromEncoded() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
        t.resolveTemplatesFromEncoded(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMatrixParam() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
        t.matrixParam(null, (Object[]) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRequest1() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
        t.request((String[]) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRequest2() {
        final MinijaxTestWebTarget t = new MinijaxTestWebTarget(null, null);
        t.request((MediaType[]) null);
    }
}
