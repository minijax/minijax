package org.minijax.validation;

import java.util.Iterator;
import java.util.List;

import javax.validation.ElementKind;
import javax.validation.Path;

public class MinijaxPath implements Path {
    private final List<Node> nodes;

    public MinijaxPath(final List<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }

    @Override
    public String toString() {
        final StringBuilder b = new StringBuilder();
        for (final Node node : nodes) {
            if (b.length() > 0 && node.getKind() == ElementKind.PROPERTY) {
                b.append('.');
            }
            b.append(node.toString());
        }
        return b.toString();
    }

    public abstract static class MinijaxNode implements Node {
        private final int index;
        private final String name;

        public MinijaxNode(final int index, final String name) {
            this.index = index;
            this.name = name;
        }

        @Override
        public Integer getIndex() {
            return index;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isInIterable() {
            return true;
        }

        @Override
        public Object getKey() {
            return null;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends Node> T as(final Class<T> nodeType) {
            return (T) this;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class MinijaxPropertyNode extends MinijaxNode implements PropertyNode {
        public MinijaxPropertyNode(final int index, final String name) {
            super(index, name);
        }

        @Override
        public ElementKind getKind() {
            return ElementKind.PROPERTY;
        }

        @Override
        public Class<?> getContainerClass() {
            return null;
        }

        @Override
        public Integer getTypeArgumentIndex() {
            return 0;
        }
    }

    public static class MinijaxContainerElementNode extends MinijaxNode implements ContainerElementNode {
        public MinijaxContainerElementNode(final int index, final String name) {
            super(index, name);
        }

        @Override
        public ElementKind getKind() {
            return ElementKind.CONTAINER_ELEMENT;
        }

        @Override
        public Class<?> getContainerClass() {
            return null;
        }

        @Override
        public Integer getTypeArgumentIndex() {
            return 0;
        }
    }
}
