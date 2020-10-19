package org.minijax.persistence.criteria;

import java.util.Collection;
import java.util.Map;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.metamodel.Bindable;
import jakarta.persistence.metamodel.MapAttribute;
import jakarta.persistence.metamodel.PluralAttribute;
import jakarta.persistence.metamodel.SingularAttribute;

public abstract class MinijaxPath<T>
        extends MinijaxExpression<T>
        implements jakarta.persistence.criteria.Path<T> {

    protected MinijaxPath(final Class<T> javaType) {
        super(javaType);
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
    public <Y> MinijaxPath<Y> get(final String attributeName) {
        throw new UnsupportedOperationException();
    }
}
