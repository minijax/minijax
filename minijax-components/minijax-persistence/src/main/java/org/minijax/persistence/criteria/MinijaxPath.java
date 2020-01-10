package org.minijax.persistence.criteria;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

public class MinijaxPath<T> extends MinijaxExpression<T> implements javax.persistence.criteria.Path<T> {
    private final MinijaxRoot<?> root;
    private final String value;

    public MinijaxPath(final MinijaxRoot<?> root, final String value) {
        this.root = Objects.requireNonNull(root);
        this.value = Objects.requireNonNull(value);
    }

    public MinijaxRoot<?> getRoot() {
        return root;
    }

    public String getValue() {
        return value;
    }

    /*
     * Unsupported
     */

    @Override
    public Bindable<T> getModel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Path<?> getParentPath() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> Path<Y> get(final SingularAttribute<? super T, Y> attribute) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E, C extends Collection<E>> Expression<C> get(final PluralAttribute<T, C, E> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, V, M extends Map<K, V>> Expression<M> get(final MapAttribute<T, K, V> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Class<? extends T>> type() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> Path<Y> get(final String attributeName) {
        throw new UnsupportedOperationException();
    }
}
