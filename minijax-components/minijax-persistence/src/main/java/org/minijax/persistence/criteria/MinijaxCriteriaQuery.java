package org.minijax.persistence.criteria;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Predicate.BooleanOperator;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.EntityType;

import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.metamodel.MinijaxEntityType;
import org.minijax.persistence.metamodel.MinijaxMetamodel;

public class MinijaxCriteriaQuery<T>
//        extends MinijaxBaseTypedQuery<T>
        implements javax.persistence.criteria.CriteriaQuery<T> {

    private final MinijaxEntityManager em;
    private final MinijaxMetamodel metamodel;
    private final Class<T> resultType;
    private final LinkedHashSet<MinijaxRoot<?>> roots;
//    private final Map<String, Object> namedParams;
//    private final Map<Integer, Object> positionalParams;
    private MinijaxPredicate where;
    private List<Order> orderBy;

    public MinijaxCriteriaQuery(final MinijaxEntityManager em, final Class<T> resultType) {
        this.em = em;
        this.metamodel = em.getMetamodel();
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
    public MinijaxCriteriaQuery<T> select(final Selection<? extends T> selection) {
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
    public MinijaxPredicate getRestriction() {
        return where;
    }

    @Override
    public List<Order> getOrderList() {
        return orderBy;
    }

//    @Override
//    public List<T> getResultList() {
//        return em.getDialect().getResultList(em, this);
//    }

    /*
     * Unsupported
     */

    @Override
    public Selection<T> getSelection() {
        throw new UnsupportedOperationException();
    }

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
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Set getParameters() {
        throw new UnsupportedOperationException();
    }
}
