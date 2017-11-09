package org.minijax;

import static javax.ws.rs.core.MediaType.*;

import static org.junit.Assert.*;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

public class StringConversionTest extends MinijaxTest {

    static class CtorWidget {
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

    @BeforeClass
    public static void setUpStringConversionTest() {
        resetServer();
        register(StringConversionTest.class);
    }

    @Test
    public void testBoolean() {
        assertEquals("Received: true", target("/boolean?x=true").request().get(String.class));
    }

    @Test
    public void testByte() {
        assertEquals("Received: 32", target("/byte?x=32").request().get(String.class));
    }

    @Test
    public void testChar() {
        assertEquals("Received: c", target("/char?x=c").request().get(String.class));
    }

    @Test
    public void testDouble() {
        assertEquals("Received: 3.14", target("/double?x=3.14").request().get(String.class));
    }

    @Test
    public void testFloat() {
        assertEquals("Received: 2.71828", target("/float?x=2.71828").request().get(String.class));
    }

    @Test
    public void testInt() {
        assertEquals("Received: 123", target("/int?x=123").request().get(String.class));
    }

    @Test
    public void testLong() {
        assertEquals("Received: 123", target("/long?x=123").request().get(String.class));
    }

    @Test
    public void testShort() {
        assertEquals("Received: 123", target("/short?x=123").request().get(String.class));
    }

    @Test
    public void testString() {
        assertEquals("Received: hello world", target("/string?x=hello+world").request().get(String.class));
    }

    @Test
    public void testUuid() {
        assertEquals("Received: 123e4567-e89b-12d3-a456-426655440000", target("/uuid?x=123e4567-e89b-12d3-a456-426655440000").request().get(String.class));
    }

    @Test
    public void testCtor() {
        assertEquals("Received: hello world", target("/ctor?x=hello+world").request().get(String.class));
    }

    @Test
    public void testValueOf() {
        assertEquals("Received: hello world", target("/valueof?x=hello+world").request().get(String.class));
    }

    @Test
    public void testFail() {
        assertEquals(500, target("/fail?x=hello+world").request().get().getStatus());
    }
}
