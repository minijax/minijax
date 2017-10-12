package org.minijax;

import static org.junit.Assert.*;

import javax.ws.rs.core.Response.Status;

import org.junit.Test;

public class StatusInfoTest {

    @Test
    public void testDefault() {
        final MinijaxStatusInfo i = new MinijaxStatusInfo();
        assertEquals(200, i.getStatusCode());
        assertEquals(Status.Family.SUCCESSFUL, i.getFamily());
        assertEquals("OK", i.getReasonPhrase());
    }

    @Test
    public void testCopyCtor() {
        final MinijaxStatusInfo i = new MinijaxStatusInfo(Status.NOT_FOUND);
        assertEquals(404, i.getStatusCode());
        assertEquals(Status.Family.CLIENT_ERROR, i.getFamily());
        assertEquals("Not Found", i.getReasonPhrase());
    }

    @Test
    public void testSetters() {
        final MinijaxStatusInfo i = new MinijaxStatusInfo();
        i.setStatusCode(101);
        i.setFamily(Status.Family.OTHER);
        i.setReasonPhrase("Switching Protocols");
        assertEquals(101, i.getStatusCode());
        assertEquals(Status.Family.OTHER, i.getFamily());
        assertEquals("Switching Protocols", i.getReasonPhrase());
    }

    @Test
    public void testSetNull() {
        final MinijaxStatusInfo i = new MinijaxStatusInfo();
        i.setStatusInfo(null);
        assertEquals(200, i.getStatusCode());
        assertEquals(Status.Family.SUCCESSFUL, i.getFamily());
        assertEquals("OK", i.getReasonPhrase());
    }
}
