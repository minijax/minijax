package org.minijax.persistence.criteria;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Predicate.BooleanOperator;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.metamodel.EntityType;

import org.minijax.persistence.MinijaxSelection;
import org.minijax.persistence.metamodel.MinijaxEntityType;
import org.minijax.persistence.metamodel.MinijaxMetamodel;

public class MinijaxCriteriaQuery<T> implements jakarta.persistence.criteria.CriteriaQuery<T> {
    private final MinijaxMetamodel metamodel;
    private final Class<T> resultType;
    private final LinkedHashSet<MinijaxRoot<?>> roots;
    private MinijaxSelection<T> selection;
    private MinijaxPredicate where;
    private List<Order> orderBy;

    public MinijaxCriteriaQuery(final MinijaxMetamodel metamodel, final Class<T> resultType) {
        this.metamodel = metamodel;
        this.resultType = resultType;
        this.roots = new LinkedHashSet<>();
    }

    @Override
    public Class<T> getResultType() {
        return resultType;
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Set<Root<?>> getRoots() {
        return (Set) roots;
    }

    @Override
    @SuppressWarnings("unchecked")
    public MinijaxCriteriaQuery<T> select(final Selection<? extends T> selection) {
        this.selection = (MinijaxSelection<T>) selection;
        return this;
    }

    @Override
    public <X> MinijaxRoot<X> from(final Class<X> entityClass) {
        final MinijaxRoot<X> root = new MinijaxRoot<>(metamodel.entity(entityClass));
        roots.add(root);
        return root;
    }

    @Override
    public <X> MinijaxRoot<X> from(final EntityType<X> entity) {
        final MinijaxRoot<X> root = new MinijaxRoot<>((MinijaxEntityType<X>) entity);
        roots.add(root);
        return root;
    }

    @Override
    public MinijaxCriteriaQuery<T> where(final Expression<Boolean> restriction) {
        this.where = (MinijaxPredicate) restriction;
        return this;
    }

    @Override
    public MinijaxCriteriaQuery<T> where(final Predicate... restrictions) {
        this.where = new MinijaxConjunction(BooleanOperator.AND, (MinijaxPredicate[]) restrictions);
        return this;
    }

    @Override
    public MinijaxCriteriaQuery<T> orderBy(final Order... o) {
        this.orderBy = Arrays.asList(o);
        return this;
    }

    @Override
    public MinijaxCriteriaQuery<T> orderBy(final List<Order> o) {
        this.orderBy = o;
        return this;
    }

    @Override
    public MinijaxSelection<T> getSelection() {
        return selection;
    }

    @Override
    public MinijaxPredicate getRestriction() {
        return where;
    }

    @Override
    public List<Order> getOrderList() {
        return orderBy;
    }

    /*
     * Unsupported
     */

    @Override
    public List<Expression<?>> getGroupList() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate getGroupRestriction() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDistinct() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <U> Subquery<U> subquery(final Class<U> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CriteriaQuery<T> multiselect(final Selection<?>... selections) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CriteriaQuery<T> multiselect(final List<Selection<?>> selectionList) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CriteriaQuery<T> groupBy(final Expression<?>... grouping) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CriteriaQuery<T> groupBy(final List<Expression<?>> grouping) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CriteriaQuery<T> having(final Expression<Boolean> restriction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CriteriaQuery<T> having(final Predicate... restrictions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CriteriaQuery<T> distinct(final boolean distinct) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<ParameterExpression<?>> getParameters() {
        throw new UnsupportedOperationException();
    }
}
