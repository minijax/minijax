package org.minijax.rs.util;

import static jakarta.ws.rs.core.MediaType.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;

import org.junit.jupiter.api.Test;

@Consumes(APPLICATION_FORM_URLENCODED)
@Produces(TEXT_HTML)
class MediaTypeUtilsTest {

    @Test
    void testCtor() {
        assertThrows(UnsupportedOperationException.class, MediaTypeUtils::new);
    }

    @Test
    void testParseNullConsumes() {
        assertTrue(MediaTypeUtils.parseMediaTypes((Consumes) null).isEmpty());
    }

    @Test
    void testParseConsumes() {
        assertEquals(APPLICATION_FORM_URLENCODED_TYPE,
                MediaTypeUtils.parseMediaTypes(MediaTypeUtilsTest.class.getAnnotation(Consumes.class)).get(0));
    }

    @Test
    void testParseNullProduces() {
        assertTrue(MediaTypeUtils.parseMediaTypes((Produces) null).isEmpty());
    }

    @Test
    void testParseProduces() {
        assertEquals(TEXT_HTML_TYPE,
                MediaTypeUtils.parseMediaTypes(MediaTypeUtilsTest.class.getAnnotation(Produces.class)).get(0));
    }
}
