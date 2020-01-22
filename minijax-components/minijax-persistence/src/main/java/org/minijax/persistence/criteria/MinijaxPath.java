package org.minijax.persistence.criteria;

import java.util.Collection;
import java.util.Map;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

public abstract class MinijaxPath<T>
        extends MinijaxExpression<T>
        implements javax.persistence.criteria.Path<T> {

    public MinijaxPath(final Class<T> javaType) {
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
