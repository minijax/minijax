package org.minijax.validator;

import static org.junit.Assert.*;

import java.util.Arrays;

import javax.validation.ElementKind;
import javax.validation.Path.Node;
import javax.validation.Path.PropertyNode;

import org.junit.Test;
import org.minijax.validator.MinijaxPath.MinijaxPropertyNode;

public class PathTest {

    @Test
    public void testSimplePath() {
        final MinijaxPath path = new MinijaxPath(Arrays.asList(new MinijaxPropertyNode(0, "foo")));
        assertEquals("foo", path.toString());

        for (final Node node : path) {
            assertTrue(node.isInIterable());
            assertNotNull(node.getName());
            assertNull(node.getKey());
            assertEquals(ElementKind.PROPERTY, node.getKind());
            assertEquals(node, node.as(PropertyNode.class));
            assertNull(node.as(PropertyNode.class).getContainerClass());
            assertEquals(0, (int) node.as(PropertyNode.class).getTypeArgumentIndex());
        }
    }

    @Test
    public void testNestedPath() {
        final MinijaxPath path = new MinijaxPath(Arrays.asList(
                new MinijaxPropertyNode(0, "foo"),
                new MinijaxPropertyNode(1, "bar")));

        assertEquals("foo.bar", path.toString());

        int i = 0;
        for (final Node node : path) {
            assertEquals(i++, (int) node.getIndex());
        }
    }
}