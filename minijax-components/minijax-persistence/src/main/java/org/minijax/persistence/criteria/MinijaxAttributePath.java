package org.minijax.persistence.criteria;

import java.util.Objects;

import jakarta.persistence.criteria.Path;

import org.minijax.persistence.metamodel.MinijaxAttribute;

public class MinijaxAttributePath<T> extends MinijaxPath<T> {
    private final MinijaxPath<?> parentPath;
    private final MinijaxAttribute<?, T> attribute;

    public MinijaxAttributePath(final MinijaxPath<?> parentPath, final MinijaxAttribute<?, T> attribute) {
        super(attribute.getJavaType());
        this.parentPath = Objects.requireNonNull(parentPath);
        this.attribute = Objects.requireNonNull(attribute);
    }

    @Override
    public Path<?> getParentPath() {
        return parentPath;
    }

    public MinijaxAttribute<?, T> getAttribute() {
        return attribute;
    }
}
