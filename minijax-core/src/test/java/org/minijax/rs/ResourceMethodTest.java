package org.minijax.rs;

import static jakarta.ws.rs.HttpMethod.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import org.junit.Test;

public class ResourceMethodTest {

    @Path("/{id}")
    public static String x(@PathParam("id") final String id) {
        return "ok";
    }

    @Path("/x")
    public static String x() {
        return "ok";
    }

    @Test
    public void testResourceMethodSorting() throws Exception {
        final MinijaxResourceMethod m1 = new MinijaxResourceMethod(GET, ResourceMethodTest.class.getMethod("x", String.class), null);
        assertEquals(1, m1.literalLength);

        final MinijaxResourceMethod m2 = new MinijaxResourceMethod(GET, ResourceMethodTest.class.getMethod("x"), null);
        assertEquals(2, m2.literalLength);

        final List<MinijaxResourceMethod> list = new ArrayList<>(Arrays.asList(m1, m2));
        MinijaxResourceMethod.sortByLiteralLength(list);
        assertEquals(m2, list.get(0));
        assertEquals(m1, list.get(1));
    }
}
