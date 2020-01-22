package org.minijax.persistence.criteria;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.persistence.Tuple;
import javax.persistence.criteria.CollectionJoin;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Predicate.BooleanOperator;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.SetJoin;
import javax.persistence.criteria.Subquery;

import org.minijax.persistence.MinijaxCompoundSelection;
import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.criteria.MinijaxComparison.ComparisonType;

public class MinijaxCriteriaBuilder implements javax.persistence.criteria.CriteriaBuilder {
    private final MinijaxEntityManager em;

    public MinijaxCriteriaBuilder(final MinijaxEntityManager em) {
        this.em = em;
    }

    public MinijaxEntityManager getEntityManager() {
        return em;
    }

    @Override
    public <T> MinijaxCriteriaQuery<T> createQuery(final Class<T> resultClass) {
        return new MinijaxCriteriaQuery<>(em, resultClass);
    }

    @Override
    public MinijaxCriteriaQuery<Object> createQuery() {
        return new MinijaxCriteriaQuery<>(em, Object.class);
    }

    @Override
    public MinijaxCriteriaQuery<Tuple> createTupleQuery() {
        return new MinijaxCriteriaQuery<>(em, Tuple.class);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked", "java:S1221" }) // Ignore Sonar warning about confusing "equal" method
    public MinijaxComparison equal(final Expression<?> x, final Object y) {
        return new MinijaxComparison(ComparisonType.EQUALS, (MinijaxExpression<?>) x, MinijaxExpression.ofLiteral(y));
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked", "java:S1221" }) // Ignore Sonar warning about confusing "equal" method
    public MinijaxComparison equal(final Expression<?> x, final Expression<?> y) {
        return new MinijaxComparison(ComparisonType.EQUALS, (MinijaxExpression<?>) x, (MinijaxExpression<?>) y);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> MinijaxIn<T> in(final Expression<? extends T> expression) {
        return new MinijaxIn<>((MinijaxExpression<T>) expression);
    }

    @Override
    public MinijaxConjunction and(final Expression<Boolean> x, final Expression<Boolean> y) {
        return new MinijaxConjunction(BooleanOperator.AND, (MinijaxPredicate) x, (MinijaxPredicate) y);
    }

    @Override
    public MinijaxConjunction and(final Predicate... restrictions) {
        return new MinijaxConjunction(BooleanOperator.AND, (MinijaxPredicate[]) restrictions);
    }

    @Override
    public MinijaxConjunction or(final Expression<Boolean> x, final Expression<Boolean> y) {
        return new MinijaxConjunction(BooleanOperator.OR, (MinijaxPredicate) x, (MinijaxPredicate) y);
    }

    @Override
    public MinijaxConjunction or(final Predicate... restrictions) {
        return new MinijaxConjunction(BooleanOperator.OR, (MinijaxPredicate[]) restrictions);
    }

    @Override
    public MinijaxOrder asc(final Expression<?> x) {
        return new MinijaxOrder((MinijaxExpression<?>) x, true);
    }

    @Override
    public MinijaxOrder desc(final Expression<?> x) {
        return new MinijaxOrder((MinijaxExpression<?>) x, false);
    }

    /*
     * Unsupported
     */

    @Override
    public <T> CriteriaUpdate<T> createCriteriaUpdate(final Class<T> targetEntity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> CriteriaDelete<T> createCriteriaDelete(final Class<T> targetEntity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> CompoundSelection<Y> construct(final Class<Y> resultClass, final Selection<?>... selections) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CompoundSelection<Tuple> tuple(final Selection<?>... selections) {
        return new MinijaxCompoundSelection<>(Tuple.class, Arrays.asList(selections));
    }

    @Override
    public CompoundSelection<Object[]> array(final Selection<?>... selections) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <N extends Number> Expression<Double> avg(final Expression<N> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <N extends Number> Expression<N> sum(final Expression<N> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Long> sumAsLong(final Expression<Integer> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Double> sumAsDouble(final Expression<Float> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <N extends Number> Expression<N> max(final Expression<N> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <N extends Number> Expression<N> min(final Expression<N> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X extends Comparable<? super X>> Expression<X> greatest(final Expression<X> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X extends Comparable<? super X>> Expression<X> least(final Expression<X> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Long> count(final Expression<?> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Long> countDistinct(final Expression<?> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate exists(final Subquery<?> subquery) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> Expression<Y> all(final Subquery<Y> subquery) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> Expression<Y> some(final Subquery<Y> subquery) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> Expression<Y> any(final Subquery<Y> subquery) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate not(final Expression<Boolean> restriction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate conjunction() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate disjunction() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate isTrue(final Expression<Boolean> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate isFalse(final Expression<Boolean> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate isNull(final Expression<?> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate isNotNull(final Expression<?> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate notEqual(final Expression<?> x, final Expression<?> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate notEqual(final Expression<?> x, final Object y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate greaterThan(final Expression<? extends Y> x,
            final Expression<? extends Y> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate greaterThan(final Expression<? extends Y> x, final Y y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate greaterThanOrEqualTo(final Expression<? extends Y> x,
            final Expression<? extends Y> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate greaterThanOrEqualTo(final Expression<? extends Y> x, final Y y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate lessThan(final Expression<? extends Y> x, final Expression<? extends Y> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate lessThan(final Expression<? extends Y> x, final Y y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate lessThanOrEqualTo(final Expression<? extends Y> x,
            final Expression<? extends Y> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate lessThanOrEqualTo(final Expression<? extends Y> x, final Y y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate between(final Expression<? extends Y> v, final Expression<? extends Y> x,
            final Expression<? extends Y> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate between(final Expression<? extends Y> v, final Y x, final Y y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate gt(final Expression<? extends Number> x, final Expression<? extends Number> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate gt(final Expression<? extends Number> x, final Number y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate ge(final Expression<? extends Number> x, final Expression<? extends Number> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate ge(final Expression<? extends Number> x, final Number y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate lt(final Expression<? extends Number> x, final Expression<? extends Number> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate lt(final Expression<? extends Number> x, final Number y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate le(final Expression<? extends Number> x, final Expression<? extends Number> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate le(final Expression<? extends Number> x, final Number y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <N extends Number> Expression<N> neg(final Expression<N> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <N extends Number> Expression<N> abs(final Expression<N> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <N extends Number> Expression<N> sum(final Expression<? extends N> x, final Expression<? extends N> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <N extends Number> Expression<N> sum(final Expression<? extends N> x, final N y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <N extends Number> Expression<N> sum(final N x, final Expression<? extends N> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <N extends Number> Expression<N> prod(final Expression<? extends N> x, final Expression<? extends N> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <N extends Number> Expression<N> prod(final Expression<? extends N> x, final N y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <N extends Number> Expression<N> prod(final N x, final Expression<? extends N> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <N extends Number> Expression<N> diff(final Expression<? extends N> x, final Expression<? extends N> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <N extends Number> Expression<N> diff(final Expression<? extends N> x, final N y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <N extends Number> Expression<N> diff(final N x, final Expression<? extends N> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Number> quot(final Expression<? extends Number> x, final Expression<? extends Number> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Number> quot(final Expression<? extends Number> x, final Number y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Number> quot(final Number x, final Expression<? extends Number> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Integer> mod(final Expression<Integer> x, final Expression<Integer> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Integer> mod(final Expression<Integer> x, final Integer y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Integer> mod(final Integer x, final Expression<Integer> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Double> sqrt(final Expression<? extends Number> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Long> toLong(final Expression<? extends Number> number) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Integer> toInteger(final Expression<? extends Number> number) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Float> toFloat(final Expression<? extends Number> number) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Double> toDouble(final Expression<? extends Number> number) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<BigDecimal> toBigDecimal(final Expression<? extends Number> number) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<BigInteger> toBigInteger(final Expression<? extends Number> number) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<String> toString(final Expression<Character> character) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Expression<T> literal(final T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Expression<T> nullLiteral(final Class<T> resultClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> ParameterExpression<T> parameter(final Class<T> paramClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> ParameterExpression<T> parameter(final Class<T> paramClass, final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <C extends Collection<?>> Predicate isEmpty(final Expression<C> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <C extends Collection<?>> Predicate isNotEmpty(final Expression<C> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <C extends Collection<?>> Expression<Integer> size(final Expression<C> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <C extends Collection<?>> Expression<Integer> size(final C collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E, C extends Collection<E>> Predicate isMember(final Expression<E> elem, final Expression<C> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E, C extends Collection<E>> Predicate isMember(final E elem, final Expression<C> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E, C extends Collection<E>> Predicate isNotMember(final Expression<E> elem, final Expression<C> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E, C extends Collection<E>> Predicate isNotMember(final E elem, final Expression<C> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V, M extends Map<?, V>> Expression<Collection<V>> values(final M map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, M extends Map<K, ?>> Expression<Set<K>> keys(final M map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate like(final Expression<String> x, final Expression<String> pattern) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate like(final Expression<String> x, final String pattern) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate like(final Expression<String> x, final Expression<String> pattern, final Expression<Character> escapeChar) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate like(final Expression<String> x, final Expression<String> pattern, final char escapeChar) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate like(final Expression<String> x, final String pattern, final Expression<Character> escapeChar) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate like(final Expression<String> x, final String pattern, final char escapeChar) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate notLike(final Expression<String> x, final Expression<String> pattern) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate notLike(final Expression<String> x, final String pattern) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate notLike(final Expression<String> x, final Expression<String> pattern, final Expression<Character> escapeChar) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate notLike(final Expression<String> x, final Expression<String> pattern, final char escapeChar) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate notLike(final Expression<String> x, final String pattern, final Expression<Character> escapeChar) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate notLike(final Expression<String> x, final String pattern, final char escapeChar) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<String> concat(final Expression<String> x, final Expression<String> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<String> concat(final Expression<String> x, final String y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<String> concat(final String x, final Expression<String> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<String> substring(final Expression<String> x, final Expression<Integer> from) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<String> substring(final Expression<String> x, final int from) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<String> substring(final Expression<String> x, final Expression<Integer> from, final Expression<Integer> len) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<String> substring(final Expression<String> x, final int from, final int len) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<String> trim(final Expression<String> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<String> trim(final Trimspec ts, final Expression<String> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<String> trim(final Expression<Character> t, final Expression<String> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<String> trim(final Trimspec ts, final Expression<Character> t, final Expression<String> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<String> trim(final char t, final Expression<String> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<String> trim(final Trimspec ts, final char t, final Expression<String> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<String> lower(final Expression<String> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<String> upper(final Expression<String> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Integer> length(final Expression<String> x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Integer> locate(final Expression<String> x, final Expression<String> pattern) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Integer> locate(final Expression<String> x, final String pattern) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Integer> locate(final Expression<String> x, final Expression<String> pattern, final Expression<Integer> from) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Integer> locate(final Expression<String> x, final String pattern, final int from) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Date> currentDate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Timestamp> currentTimestamp() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression<Time> currentTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> Expression<Y> coalesce(final Expression<? extends Y> x, final Expression<? extends Y> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> Expression<Y> coalesce(final Expression<? extends Y> x, final Y y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> Expression<Y> nullif(final Expression<Y> x, final Expression<?> y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> Expression<Y> nullif(final Expression<Y> x, final Y y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Coalesce<T> coalesce() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <C, R> SimpleCase<C, R> selectCase(final Expression<? extends C> expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <R> Case<R> selectCase() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Expression<T> function(final String name, final Class<T> type, final Expression<?>... args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X, T, V extends T> Join<X, V> treat(final Join<X, T> join, final Class<V> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X, T, E extends T> CollectionJoin<X, E> treat(final CollectionJoin<X, T> join, final Class<E> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X, T, E extends T> SetJoin<X, E> treat(final SetJoin<X, T> join, final Class<E> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X, T, E extends T> ListJoin<X, E> treat(final ListJoin<X, T> join, final Class<E> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X, K, T, V extends T> MapJoin<X, K, V> treat(final MapJoin<X, K, T> join, final Class<V> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X, T extends X> Path<T> treat(final Path<X> path, final Class<T> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X, T extends X> Root<T> treat(final Root<X> root, final Class<T> type) {
        throw new UnsupportedOperationException();
    }
}
