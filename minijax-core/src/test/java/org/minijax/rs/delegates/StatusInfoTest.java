package org.minijax.rs.delegates;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.core.Response.Status;

import org.junit.jupiter.api.Test;

class StatusInfoTest {

    @Test
    void testDefault() {
        final MinijaxStatusInfo i = new MinijaxStatusInfo();
        assertEquals(200, i.getStatusCode());
        assertEquals(Status.Family.SUCCESSFUL, i.getFamily());
        assertEquals("OK", i.getReasonPhrase());
    }

    @Test
    void testCopyCtor() {
        final MinijaxStatusInfo i = new MinijaxStatusInfo(Status.NOT_FOUND);
        assertEquals(404, i.getStatusCode());
        assertEquals(Status.Family.CLIENT_ERROR, i.getFamily());
        assertEquals("Not Found", i.getReasonPhrase());
    }

    @Test
    void testSetters() {
        final MinijaxStatusInfo i = new MinijaxStatusInfo();
        i.setStatusCode(101);
        i.setReasonPhrase("Switching Protocols");
        assertEquals(101, i.getStatusCode());
        assertEquals(Status.Family.INFORMATIONAL, i.getFamily());
        assertEquals("Switching Protocols", i.getReasonPhrase());
    }

    @Test
    void testSetNull() {
        final MinijaxStatusInfo i = new MinijaxStatusInfo();
        i.setStatusInfo(null);
        assertEquals(200, i.getStatusCode());
        assertEquals(Status.Family.SUCCESSFUL, i.getFamily());
        assertEquals("OK", i.getReasonPhrase());
    }
}
