package org.minijax.persistence.criteria;

import java.util.Set;

import javax.persistence.criteria.CollectionJoin;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.SetJoin;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;

import org.minijax.commons.MinijaxException;
import org.minijax.persistence.metamodel.MinijaxAttribute;
import org.minijax.persistence.metamodel.MinijaxEntityType;

public class MinijaxRoot<T>
        extends MinijaxPath<T>
        implements javax.persistence.criteria.Root<T> {

    private final MinijaxEntityType<T> entityType;

    public MinijaxRoot(final MinijaxEntityType<T> entityType) {
        super(entityType.getJavaType());
        this.entityType = entityType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Y> MinijaxPath<Y> get(final String attributeName) {
        final MinijaxAttribute<T, Y> attribute = (MinijaxAttribute<T, Y>) entityType.getAttribute(attributeName);
        if (attribute == null) {
            throw new MinijaxException("Attribute \"" + attributeName + "\" not found for root \"" + entityType.getName() + "\"");
        }
        return new MinijaxAttributePath<>(this, attribute);
    }

    /*
     * Unsupported
     */

    @Override
    public Set<Join<T, ?>> getJoins() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCorrelated() {
        throw new UnsupportedOperationException();
    }

    @Override
    public From<T, T> getCorrelationParent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> Join<T, Y> join(final SingularAttribute<? super T, Y> attribute) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> Join<T, Y> join(final SingularAttribute<? super T, Y> attribute, final JoinType jt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> CollectionJoin<T, Y> join(final CollectionAttribute<? super T, Y> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> SetJoin<T, Y> join(final SetAttribute<? super T, Y> set) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> ListJoin<T, Y> join(final ListAttribute<? super T, Y> list) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, V> MapJoin<T, K, V> join(final MapAttribute<? super T, K, V> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> CollectionJoin<T, Y> join(final CollectionAttribute<? super T, Y> collection, final JoinType jt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> SetJoin<T, Y> join(final SetAttribute<? super T, Y> set, final JoinType jt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> ListJoin<T, Y> join(final ListAttribute<? super T, Y> list, final JoinType jt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, V> MapJoin<T, K, V> join(final MapAttribute<? super T, K, V> map, final JoinType jt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X, Y> Join<X, Y> join(final String attributeName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X, Y> CollectionJoin<X, Y> joinCollection(final String attributeName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X, Y> SetJoin<X, Y> joinSet(final String attributeName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X, Y> ListJoin<X, Y> joinList(final String attributeName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X, K, V> MapJoin<X, K, V> joinMap(final String attributeName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X, Y> Join<X, Y> join(final String attributeName, final JoinType jt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X, Y> CollectionJoin<X, Y> joinCollection(final String attributeName, final JoinType jt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X, Y> SetJoin<X, Y> joinSet(final String attributeName, final JoinType jt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X, Y> ListJoin<X, Y> joinList(final String attributeName, final JoinType jt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X, K, V> MapJoin<X, K, V> joinMap(final String attributeName, final JoinType jt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Fetch<T, ?>> getFetches() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> Fetch<T, Y> fetch(final SingularAttribute<? super T, Y> attribute) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> Fetch<T, Y> fetch(final SingularAttribute<? super T, Y> attribute, final JoinType jt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> Fetch<T, Y> fetch(final PluralAttribute<? super T, ?, Y> attribute) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> Fetch<T, Y> fetch(final PluralAttribute<? super T, ?, Y> attribute, final JoinType jt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X, Y> Fetch<X, Y> fetch(final String attributeName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X, Y> Fetch<X, Y> fetch(final String attributeName, final JoinType jt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityType<T> getModel() {
        throw new UnsupportedOperationException();
    }
}
