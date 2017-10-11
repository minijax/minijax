package org.minijax.util;

import static org.junit.Assert.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.junit.Test;

@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.TEXT_HTML)
public class MediaTypeUtilsTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testCtor() {
        new MediaTypeUtils();
    }

    @Test
    public void testParseNullConsumes() {
        assertTrue(MediaTypeUtils.parseMediaTypes((Consumes) null).isEmpty());
    }

    @Test
    public void testParseConsumes() {
        assertEquals(
                MediaType.APPLICATION_FORM_URLENCODED_TYPE,
                MediaTypeUtils.parseMediaTypes(MediaTypeUtilsTest.class.getAnnotation(Consumes.class)).get(0));
    }

    @Test
    public void testParseNullProduces() {
        assertTrue(MediaTypeUtils.parseMediaTypes((Produces) null).isEmpty());
    }

    @Test
    public void testParseProduces() {
        assertEquals(
                MediaType.TEXT_HTML_TYPE,
                MediaTypeUtils.parseMediaTypes(MediaTypeUtilsTest.class.getAnnotation(Produces.class)).get(0));
    }
}
