package org.minijax.validation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import jakarta.validation.ElementKind;
import jakarta.validation.Path.Node;
import jakarta.validation.Path.PropertyNode;

import org.junit.jupiter.api.Test;
import org.minijax.validation.MinijaxPath.MinijaxPropertyNode;

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