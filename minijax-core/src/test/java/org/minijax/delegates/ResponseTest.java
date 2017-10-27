package org.minijax.delegates;

import java.lang.annotation.Annotation;

import javax.ws.rs.core.GenericType;

import org.junit.Test;
import org.minijax.delegates.MinijaxResponse;
import org.minijax.delegates.MinijaxResponseBuilder;

public class ResponseTest {


    @Test(expected = UnsupportedOperationException.class)
    public void testBufferEntity() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.bufferEntity();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAllowedMethods() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.getAllowedMethods();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetEntityAnnotations() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.getEntityAnnotations();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetEntityClass() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.getEntityClass();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetEntityStream() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.getEntityStream();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetEntityType() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.getEntityType();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLink() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.getLink(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLinkBuilder() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.getLinkBuilder(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMetadata() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.getMetadata();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetStringHeaders() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.getStringHeaders();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testhasEntity() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.hasEntity();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testhasLink() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.hasLink(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReadEntity1() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.readEntity((Class<?>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReadEntity2() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.readEntity((Class<?>) null, (Annotation[]) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReadEntity3() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.readEntity((GenericType<?>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReadEntity4() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.readEntity((GenericType<?>) null, (Annotation[]) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetEntityStream() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.setEntityStream(null);
    }
}
