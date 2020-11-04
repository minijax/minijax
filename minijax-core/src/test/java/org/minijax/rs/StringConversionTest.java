package org.minijax.rs;

import static jakarta.ws.rs.core.MediaType.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

class StringConversionTest extends MinijaxTest {

    public static class CtorWidget {
        final String x;
        public CtorWidget(final String x) {
            this.x = x;
        }
    }

    public static class ValueOfWidget {
        String x;
        public static ValueOfWidget valueOf(final String x) {
            final ValueOfWidget result = new ValueOfWidget();
            result.x = x;
            return result;
        }
    }

    @GET @Path("/boolean") @Consumes(TEXT_PLAIN) @Produces(TEXT_PLAIN)
    public static String getBoolean(@QueryParam("x") final boolean x) {
        return "Received: " + x;
    }

    @GET @Path("/byte") @Consumes(TEXT_PLAIN) @Produces(TEXT_PLAIN)
    public static String getByte(@QueryParam("x") final byte x) {
        return "Received: " + x;
    }

    @GET @Path("/char") @Consumes(TEXT_PLAIN) @Produces(TEXT_PLAIN)
    public static String getChar(@QueryParam("x") final char x) {
        return "Received: " + x;
    }

    @GET @Path("/double") @Consumes(TEXT_PLAIN) @Produces(TEXT_PLAIN)
    public static String getDouble(@QueryParam("x") final double x) {
        return "Received: " + x;
    }

    @GET @Path("/float") @Consumes(TEXT_PLAIN) @Produces(TEXT_PLAIN)
    public static String getFloat(@QueryParam("x") final float x) {
        return "Received: " + x;
    }

    @GET @Path("/int") @Consumes(TEXT_PLAIN) @Produces(TEXT_PLAIN)
    public static String getInt(@QueryParam("x") final int x) {
        return "Received: " + x;
    }

    @GET @Path("/long") @Consumes(TEXT_PLAIN) @Produces(TEXT_PLAIN)
    public static String getLong(@QueryParam("x") final long x) {
        return "Received: " + x;
    }

    @GET @Path("/short") @Consumes(TEXT_PLAIN) @Produces(TEXT_PLAIN)
    public static String getShort(@QueryParam("x") final short x) {
        return "Received: " + x;
    }

    @GET @Path("/string") @Consumes(TEXT_PLAIN) @Produces(TEXT_PLAIN)
    public static String getString(@QueryParam("x") final String x) {
        return "Received: " + x;
    }

    @GET @Path("/uuid") @Consumes(TEXT_PLAIN) @Produces(TEXT_PLAIN)
    public static String getUuid(@QueryParam("x") final UUID x) {
        return "Received: " + x;
    }

    @GET @Path("/ctor") @Consumes(TEXT_PLAIN) @Produces(TEXT_PLAIN)
    public static String getCtorWidget(@QueryParam("x") final CtorWidget x) {
        return "Received: " + x.x;
    }

    @GET @Path("/valueof") @Consumes(TEXT_PLAIN) @Produces(TEXT_PLAIN)
    public static String getValueOfWidget(@QueryParam("x") final ValueOfWidget x) {
        return "Received: " + x.x;
    }

    @GET @Path("/fail") @Consumes(TEXT_PLAIN) @Produces(TEXT_PLAIN)
    public static String getFail(@QueryParam("x") final Object x) {
        return null;
    }

    @BeforeAll
    public static void setUpStringConversionTest() {
        resetServer();
        register(StringConversionTest.class);
    }

    @Test
    void testBoolean() {
        assertNotNull(getServer());
        assertEquals("Received: true", target("/boolean?x=true").request().get(String.class));
    }

    @Test
    void testByte() {
        assertEquals("Received: 32", target("/byte?x=32").request().get(String.class));
    }

    @Test
    void testChar() {
        assertEquals("Received: c", target("/char?x=c").request().get(String.class));
    }

    @Test
    void testDouble() {
        assertEquals("Received: 3.14", target("/double?x=3.14").request().get(String.class));
    }

    @Test
    void testFloat() {
        assertEquals("Received: 2.71828", target("/float?x=2.71828").request().get(String.class));
    }

    @Test
    void testInt() {
        assertEquals("Received: 123", target("/int?x=123").request().get(String.class));
    }

    @Test
    void testLong() {
        assertEquals("Received: 123", target("/long?x=123").request().get(String.class));
    }

    @Test
    void testShort() {
        assertEquals("Received: 123", target("/short?x=123").request().get(String.class));
    }

    @Test
    void testString() {
        assertEquals("Received: hello world", target("/string?x=hello+world").request().get(String.class));
    }

    @Test
    void testUuid() {
        assertEquals("Received: 123e4567-e89b-12d3-a456-426655440000", target("/uuid?x=123e4567-e89b-12d3-a456-426655440000").request().get(String.class));
    }

    @Test
    void testCtor() {
        assertEquals("Received: hello world", target("/ctor?x=hello+world").request().get(String.class));
    }

    @Test
    void testValueOf() {
        assertEquals("Received: hello world", target("/valueof?x=hello+world").request().get(String.class));
    }

    @Test
    void testFail() {
        assertEquals(404, target("/fail?x=hello+world").request().get().getStatus());
    }
}
