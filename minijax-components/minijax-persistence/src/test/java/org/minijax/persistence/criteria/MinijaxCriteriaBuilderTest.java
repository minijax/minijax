package org.minijax.persistence.criteria;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.List;

import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CollectionJoin;
import jakarta.persistence.criteria.CriteriaBuilder.Trimspec;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.MapJoin;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.SetJoin;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.MinijaxEntityManagerFactory;
import org.minijax.persistence.MinijaxPersistenceProvider;
import org.minijax.persistence.dialect.AnsiSqlBuilder;
import org.minijax.persistence.testmodel.Message;
import org.minijax.persistence.testmodel.User;
import org.minijax.persistence.testmodel.Widget;

class MinijaxCriteriaBuilderTest {
    private MinijaxEntityManagerFactory emf;
    private MinijaxEntityManager em;
    private MinijaxCriteriaBuilder cb;

    @BeforeEach
    public void setUp() {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        emf = provider.createEntityManagerFactory("testdb", null);
        em = emf.createEntityManager();
        cb = em.getCriteriaBuilder();
    }

    @AfterEach
    public void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    void testCreateClassQuery() {
        final MinijaxCriteriaQuery<Widget> q = cb.createQuery(Widget.class);
        final MinijaxRoot<Widget> w = q.from(Widget.class);
        q.select(w);
        assertEquals(Widget.class, q.getResultType());
    }

    @Test
    void testCreateObjectQuery() {
        final MinijaxCriteriaQuery<Object> q = cb.createQuery();
        final MinijaxRoot<Widget> w = q.from(Widget.class);
        q.select(w);
        assertEquals(Object.class, q.getResultType());
    }

    @Test
    void testCreateTupleQuery() {
        final MinijaxCriteriaQuery<Tuple> q = cb.createTupleQuery();
        final MinijaxRoot<Widget> w = q.from(Widget.class);
        q.select(cb.tuple(w.get("id"), w.get("name")));
        assertEquals(Tuple.class, q.getSelection().getJavaType());
        assertEquals(Tuple.class, q.getResultType());
        assertTrue(q.getSelection().isCompoundSelection());
    }

    @Test
    void testIsNull() {
        final MinijaxCriteriaQuery<Widget> q = cb.createQuery(Widget.class);
        final MinijaxRoot<Widget> w = q.from(Widget.class);
        q.select(w);
        q.where(cb.isNull(w.get("name")));

        final AnsiSqlBuilder<Widget> sqlBuilder = new AnsiSqlBuilder<>(em, em.createQuery(q));
        sqlBuilder.buildSelect();
        assertEquals(
                "SELECT t0.ID, t0.NAME FROM WIDGET t0 WHERE t0.NAME IS NULL",
                sqlBuilder.getSql());
    }

    @Test
    void testIsNotNull() {
        final MinijaxCriteriaQuery<Widget> q = cb.createQuery(Widget.class);
        final MinijaxRoot<Widget> w = q.from(Widget.class);
        q.select(w);
        q.where(cb.isNotNull(w.get("name")));

        final AnsiSqlBuilder<Widget> sqlBuilder = new AnsiSqlBuilder<>(em, em.createQuery(q));
        sqlBuilder.buildSelect();
        assertEquals(
                "SELECT t0.ID, t0.NAME FROM WIDGET t0 WHERE t0.NAME IS NOT NULL",
                sqlBuilder.getSql());
    }

    @Test
    void testLike() {
        final MinijaxCriteriaQuery<Widget> q = cb.createQuery(Widget.class);
        final MinijaxRoot<Widget> w = q.from(Widget.class);
        q.select(w);
        q.where(cb.like(w.get("name"), "foo"));

        final AnsiSqlBuilder<Widget> sqlBuilder = new AnsiSqlBuilder<>(em, em.createQuery(q));
        sqlBuilder.buildSelect();
        assertEquals(
                "SELECT t0.ID, t0.NAME FROM WIDGET t0 WHERE t0.NAME LIKE 'foo'",
                sqlBuilder.getSql());
    }

    @Test
    void testLikeExpression() {
        final MinijaxCriteriaQuery<Widget> q = cb.createQuery(Widget.class);
        final MinijaxRoot<Widget> w = q.from(Widget.class);
        q.select(w);
        q.where(cb.like(w.get("name"), MinijaxExpression.ofLiteral("foo")));

        final AnsiSqlBuilder<Widget> sqlBuilder = new AnsiSqlBuilder<>(em, em.createQuery(q));
        sqlBuilder.buildSelect();
        assertEquals(
                "SELECT t0.ID, t0.NAME FROM WIDGET t0 WHERE t0.NAME LIKE 'foo'",
                sqlBuilder.getSql());
    }

    @Test
    void testAnd() {
        final MinijaxCriteriaQuery<Widget> q = cb.createQuery(Widget.class);
        final MinijaxRoot<Widget> w = q.from(Widget.class);
        q.select(w);
        q.where(cb.and(cb.like(w.get("name"), "fo"), cb.like(w.get("name"), "oo")));

        final AnsiSqlBuilder<Widget> sqlBuilder = new AnsiSqlBuilder<>(em, em.createQuery(q));
        sqlBuilder.buildSelect();
        assertEquals(
                "SELECT t0.ID, t0.NAME FROM WIDGET t0 WHERE t0.NAME LIKE 'fo' AND t0.NAME LIKE 'oo'",
                sqlBuilder.getSql());
    }

    @Test
    void testAnd2() {
        final MinijaxCriteriaQuery<Widget> q = cb.createQuery(Widget.class);
        final MinijaxRoot<Widget> w = q.from(Widget.class);
        q.select(w);

        final MinijaxPath<String> path = w.get("name");
        q.where(cb.and(cb.like(path, "b"), cb.like(path, "a"), cb.like(path, "z")));

        final AnsiSqlBuilder<Widget> sqlBuilder = new AnsiSqlBuilder<>(em, em.createQuery(q));
        sqlBuilder.buildSelect();
        assertEquals(
                "SELECT t0.ID, t0.NAME FROM WIDGET t0 WHERE t0.NAME LIKE 'b' AND t0.NAME LIKE 'a' AND t0.NAME LIKE 'z'",
                sqlBuilder.getSql());
    }

    @Test
    void testOr() {
        final MinijaxCriteriaQuery<Widget> q = cb.createQuery(Widget.class);
        final MinijaxRoot<Widget> w = q.from(Widget.class);
        q.select(w);
        q.where(cb.or(cb.like(w.get("name"), "fo"), cb.like(w.get("name"), "oo")));

        final AnsiSqlBuilder<Widget> sqlBuilder = new AnsiSqlBuilder<>(em, em.createQuery(q));
        sqlBuilder.buildSelect();
        assertEquals(
                "SELECT t0.ID, t0.NAME FROM WIDGET t0 WHERE t0.NAME LIKE 'fo' OR t0.NAME LIKE 'oo'",
                sqlBuilder.getSql());
    }

    @Test
    void testOr2() {
        final MinijaxCriteriaQuery<Widget> q = cb.createQuery(Widget.class);
        final MinijaxRoot<Widget> w = q.from(Widget.class);
        q.select(w);

        final MinijaxPath<String> path = w.get("name");
        q.where(cb.or(cb.like(path, "b"), cb.like(path, "a"), cb.like(path, "z")));

        final AnsiSqlBuilder<Widget> sqlBuilder = new AnsiSqlBuilder<>(em, em.createQuery(q));
        sqlBuilder.buildSelect();
        assertEquals(
                "SELECT t0.ID, t0.NAME FROM WIDGET t0 WHERE t0.NAME LIKE 'b' OR t0.NAME LIKE 'a' OR t0.NAME LIKE 'z'",
                sqlBuilder.getSql());
    }

    @Test
    void testLower() {
        final MinijaxCriteriaQuery<Widget> q = cb.createQuery(Widget.class);
        final MinijaxRoot<Widget> w = q.from(Widget.class);
        q.select(w);
        q.where(cb.equal(cb.lower(w.get("name")), "foo"));

        final AnsiSqlBuilder<Widget> sqlBuilder = new AnsiSqlBuilder<>(em, em.createQuery(q));
        sqlBuilder.buildSelect();
        assertEquals(
                "SELECT t0.ID, t0.NAME FROM WIDGET t0 WHERE LOWER(t0.NAME)='foo'",
                sqlBuilder.getSql());
    }

    @Test
    void testOrderBy() {
        final MinijaxCriteriaQuery<Widget> q = cb.createQuery(Widget.class);
        final MinijaxRoot<Widget> w = q.from(Widget.class);
        q.select(w);
        q.orderBy(cb.asc(w.get("name")));

        final AnsiSqlBuilder<Widget> sqlBuilder = new AnsiSqlBuilder<>(em, em.createQuery(q));
        sqlBuilder.buildSelect();
        assertEquals(
                "SELECT t0.ID, t0.NAME FROM WIDGET t0 ORDER BY t0.NAME",
                sqlBuilder.getSql());
    }

    @Test
    void testOrderByDesc() {
        final MinijaxCriteriaQuery<Widget> q = cb.createQuery(Widget.class);
        final MinijaxRoot<Widget> w = q.from(Widget.class);
        q.select(w);
        q.orderBy(cb.desc(w.get("name")));

        final AnsiSqlBuilder<Widget> sqlBuilder = new AnsiSqlBuilder<>(em, em.createQuery(q));
        sqlBuilder.buildSelect();
        assertEquals(
                "SELECT t0.ID, t0.NAME FROM WIDGET t0 ORDER BY t0.NAME DESC",
                sqlBuilder.getSql());
    }

    @Test
    void testNot() {
        final MinijaxIn<String> inPredicate = new MinijaxIn<>(MinijaxExpression.ofLiteral("foo"));
        assertFalse(inPredicate.isNegated());
        cb.not(inPredicate);
        assertTrue(inPredicate.isNegated());
    }

    /*
     * Unsupported
     */

    @Test
    void testCreateCriteriaUpdate() {
        assertThrows(UnsupportedOperationException.class, () -> cb.createCriteriaUpdate(null));
    }

    @Test
    void testCreateCriteriaDelete() {
        assertThrows(UnsupportedOperationException.class, () -> cb.createCriteriaDelete(null));
    }

    @Test
    void testConstruct() {
        assertThrows(UnsupportedOperationException.class, () -> cb.construct(null));
    }

    @Test
    void testArray() {
        assertThrows(UnsupportedOperationException.class, () -> cb.array());
    }

    @Test
    void testAvg() {
        assertThrows(UnsupportedOperationException.class, () -> cb.avg(null));
    }

    @Test
    void testMax() {
        assertThrows(UnsupportedOperationException.class, () -> cb.max(null));
    }

    @Test
    void testMin() {
        assertThrows(UnsupportedOperationException.class, () -> cb.min(null));
    }

    @Test
    void testGreatest() {
        assertThrows(UnsupportedOperationException.class, () -> cb.greatest(null));
    }

    @Test
    void testLeast() {
        assertThrows(UnsupportedOperationException.class, () -> cb.least(null));
    }

    @Test
    void testCount() {
        assertThrows(UnsupportedOperationException.class, () -> cb.count(null));
    }

    @Test
    void testCountDistinct() {
        assertThrows(UnsupportedOperationException.class, () -> cb.countDistinct(null));
    }

    @Test
    void testExists() {
        assertThrows(UnsupportedOperationException.class, () -> cb.exists(null));
    }

    @Test
    void testAll() {
        assertThrows(UnsupportedOperationException.class, () -> cb.all(null));
    }

    @Test
    void testSome() {
        assertThrows(UnsupportedOperationException.class, () -> cb.some(null));
    }

    @Test
    void testAny() {
        assertThrows(UnsupportedOperationException.class, () -> cb.any(null));
    }

    @Test
    void testConjunction() {
        assertThrows(UnsupportedOperationException.class, () -> cb.conjunction());
    }

    @Test
    void testDisjunction() {
        assertThrows(UnsupportedOperationException.class, () -> cb.disjunction());
    }

    @Test
    void testIsTrue() {
        assertThrows(UnsupportedOperationException.class, () -> cb.isTrue(null));
    }

    @Test
    void testIsFalse() {
        assertThrows(UnsupportedOperationException.class, () -> cb.isFalse(null));
    }

    @Test
    void testNotEqual1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.notEqual(null, null));
    }

    @Test
    void testNotEqual2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.notEqual(null, (Object) null));
    }

    @Test
    void testGreaterThan1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.greaterThan(null, (Expression<Integer>) null));
    }

    @Test
    void testGreaterThan2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.greaterThan(null, (Integer) null));
    }

    @Test
    void testGreaterThanOrEqualTo1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.greaterThanOrEqualTo(null, (Expression<Integer>) null));
    }

    @Test
    void testGreaterThanOrEqualTo2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.greaterThanOrEqualTo(null, (Integer) null));
    }

    @Test
    void testLessThan1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.lessThan(null, (Expression<Integer>) null));
    }

    @Test
    void testLessThan2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.lessThan(null, (Integer) null));
    }

    @Test
    void testLessThanOrEqualTo1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.lessThanOrEqualTo(null, (Expression<Integer>) null));
    }

    @Test
    void testLessThanOrEqualTo() {
        assertThrows(UnsupportedOperationException.class, () -> cb.lessThanOrEqualTo(null, (Integer) null));
    }

    @Test
    void testBetween1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.between(null, null, (Expression<Integer>) null));
    }

    @Test
    void testBetween2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.between(null, null, (Integer) null));
    }

    @Test
    void testGt1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.gt(null, (Expression<Integer>) null));
    }

    @Test
    void testGt2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.gt(null, (Integer) null));
    }

    @Test
    void testGe1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.ge(null, (Expression<Integer>) null));
    }

    @Test
    void testGe2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.ge(null, (Integer) null));
    }

    @Test
    void testLt1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.lt(null, (Expression<Integer>) null));
    }

    @Test
    void testLt2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.lt(null, (Integer) null));
    }

    @Test
    void testLe1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.le(null, (Expression<Integer>) null));
    }

    @Test
    void testLe2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.le(null, (Integer) null));
    }

    @Test
    void testNeg() {
        assertThrows(UnsupportedOperationException.class, () -> cb.neg(null));
    }

    @Test
    void testAbs() {
        assertThrows(UnsupportedOperationException.class, () -> cb.abs(null));
    }

    @Test
    void testSum1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.sum((Expression<Integer>) null, (Expression<Integer>) null));
    }

    @Test
    void testSum2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.sum(null, (Integer) null));
    }

    @Test
    void testSum3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.sum((Integer) null, null));
    }

    @Test
    void testSum4() {
        assertThrows(UnsupportedOperationException.class, () -> cb.sum(null));
    }

    @Test
    void testSumAsLong() {
        assertThrows(UnsupportedOperationException.class, () -> cb.sumAsLong(null));
    }

    @Test
    void testSumAsDouble() {
        assertThrows(UnsupportedOperationException.class, () -> cb.sumAsDouble(null));
    }

    @Test
    void testProd1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.prod((Expression<Integer>) null, (Expression<Integer>) null));
    }

    @Test
    void testProd2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.prod(null, (Integer) null));
    }

    @Test
    void testProd3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.prod((Integer) null, null));
    }

    @Test
    void testDiff1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.diff((Expression<Integer>) null, (Expression<Integer>) null));
    }

    @Test
    void testDiff2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.diff(null, (Integer) null));
    }

    @Test
    void testDiff3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.diff((Integer) null, null));
    }

    @Test
    void testQuot1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.quot((Expression<Integer>) null, (Expression<Integer>) null));
    }

    @Test
    void testQuot2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.quot(null, (Integer) null));
    }

    @Test
    void testQuot3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.quot((Integer) null, null));
    }

    @Test
    void testMod1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.mod((Expression<Integer>) null, (Expression<Integer>) null));
    }

    @Test
    void testMod2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.mod(null, (Integer) null));
    }

    @Test
    void testMod3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.mod((Integer) null, null));
    }

    @Test
    void testSqrt() {
        assertThrows(UnsupportedOperationException.class, () -> cb.sqrt(null));
    }

    @Test
    void testToLong() {
        assertThrows(UnsupportedOperationException.class, () -> cb.toLong(null));
    }

    @Test
    void testToInteger() {
        assertThrows(UnsupportedOperationException.class, () -> cb.toInteger(null));
    }

    @Test
    void testToFloat() {
        assertThrows(UnsupportedOperationException.class, () -> cb.toFloat(null));
    }

    @Test
    void testToDouble() {
        assertThrows(UnsupportedOperationException.class, () -> cb.toDouble(null));
    }

    @Test
    void testToBigDecimal() {
        assertThrows(UnsupportedOperationException.class, () -> cb.toBigDecimal(null));
    }

    @Test
    void testToBigInteger() {
        assertThrows(UnsupportedOperationException.class, () -> cb.toBigInteger(null));
    }

    @Test
    void testToString() {
        assertThrows(UnsupportedOperationException.class, () -> cb.toString(null));
    }

    @Test
    void testLiteral() {
        assertThrows(UnsupportedOperationException.class, () -> cb.literal(null));
    }

    @Test
    void testNullLiteral() {
        assertThrows(UnsupportedOperationException.class, () -> cb.nullLiteral(null));
    }

    @Test
    void testParameter1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.parameter(null));
    }

    @Test
    void testParameter2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.parameter(null, null));
    }

    @Test
    void testIsEmpty() {
        assertThrows(UnsupportedOperationException.class, () -> cb.isEmpty(null));
    }

    @Test
    void testIsNotEmpty() {
        assertThrows(UnsupportedOperationException.class, () -> cb.isNotEmpty(null));
    }

    @Test
    void testSize1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.size((Expression<Collection<Object>>) null));
    }

    @Test
    void testSize2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.size((Collection<Object>) null));
    }

    @Test
    void testIsMember1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.isMember((Expression<Widget>) null, (Expression<List<Widget>>) null));
    }

    @Test
    void testIsMember2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.isMember((Widget) null, (Expression<List<Widget>>) null));
    }

    @Test
    public void isNotMember1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.isNotMember((Expression<Widget>) null, (Expression<List<Widget>>) null));
    }

    @Test
    public void isNotMember2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.isNotMember((Widget) null, (Expression<List<Widget>>) null));
    }

    @Test
    void testValues() {
        assertThrows(UnsupportedOperationException.class, () -> cb.values(null));
    }

    @Test
    void testKeys() {
        assertThrows(UnsupportedOperationException.class, () -> cb.keys(null));
    }

    @Test
    void testLike1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.like(null, (Expression<String>) null, null));
    }

    @Test
    void testLike2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.like(null, (Expression<String>) null, ' '));
    }

    @Test
    void testLike3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.like(null, (String) null, null));
    }

    @Test
    void testLike4() {
        assertThrows(UnsupportedOperationException.class, () -> cb.like(null, (String) null, ' '));
    }

    @Test
    void testNotLike1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.notLike(null, (Expression<String>) null));
    }

    @Test
    void testNotLike2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.notLike(null, (String) null));
    }

    @Test
    void testNotLike3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.notLike(null, (Expression<String>) null, null));
    }

    @Test
    void testNotLike4() {
        assertThrows(UnsupportedOperationException.class, () -> cb.notLike(null, (Expression<String>) null, ' '));
    }

    @Test
    void testNotLike5() {
        assertThrows(UnsupportedOperationException.class, () -> cb.notLike(null, (String) null, null));
    }

    @Test
    void testNotLike6() {
        assertThrows(UnsupportedOperationException.class, () -> cb.notLike(null, (String) null, ' '));
    }

    @Test
    void testConcat1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.concat((Expression<String>) null, (Expression<String>) null));
    }

    @Test
    void testConcat2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.concat(null, (String) null));
    }

    @Test
    void testConcat3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.concat((String) null, null));
    }

    @Test
    void testSubstring1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.substring(null, null));
    }

    @Test
    void testSubstring2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.substring(null, 0));
    }

    @Test
    void testSubstring3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.substring(null, null, null));
    }

    @Test
    void testSubstring4() {
        assertThrows(UnsupportedOperationException.class, () -> cb.substring(null, 0, 0));
    }

    @Test
    void testTrim1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.trim(null));
    }

    @Test
    void testTrim2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.trim((Trimspec) null, null));
    }

    @Test
    void testTrim3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.trim((Expression<Character>) null, null));
    }

    @Test
    void testTrim4() {
        assertThrows(UnsupportedOperationException.class, () -> cb.trim(null, null, null));
    }

    @Test
    void testTrim5() {
        assertThrows(UnsupportedOperationException.class, () -> cb.trim(' ', null));
    }

    @Test
    void testTrim6() {
        assertThrows(UnsupportedOperationException.class, () -> cb.trim(null, ' ', null));
    }

    @Test
    void testLength() {
        assertThrows(UnsupportedOperationException.class, () -> cb.length(null));
    }

    @Test
    void testLocate1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.locate(null, (Expression<String>) null));
    }

    @Test
    void testLocate2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.locate(null, (String) null));
    }

    @Test
    void testLocate3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.locate(null, null, null));
    }

    @Test
    void testLocate4() {
        assertThrows(UnsupportedOperationException.class, () -> cb.locate(null, null, 0));
    }

    @Test
    void testCurrentDate() {
        assertThrows(UnsupportedOperationException.class, () -> cb.currentDate());
    }

    @Test
    void testCurrentTimestamp() {
        assertThrows(UnsupportedOperationException.class, () -> cb.currentTimestamp());
    }

    @Test
    void testCurrentTime() {
        assertThrows(UnsupportedOperationException.class, () -> cb.currentTime());
    }

    @Test
    void testCoalesce1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.coalesce());
    }

    @Test
    void testCoalesce2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.coalesce(null, (Expression<Widget>) null));
    }

    @Test
    void testCoalesce3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.coalesce(null, (Widget) null));
    }

    @Test
    void testNullif1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.nullif((Expression<Widget>) null, (Expression<Widget>) null));
    }

    @Test
    void testNullif2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.nullif((Expression<Widget>) null, (Widget) null));
    }

    @Test
    void testSelectCase1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.selectCase(null));
    }

    @Test
    void testSelectCase2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.selectCase());
    }

    @Test
    void testFunction() {
        assertThrows(UnsupportedOperationException.class, () -> cb.function(null, null));
    }

    @Test
    void testTreat1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.treat((Join<User, Message>) null, null));
    }

    @Test
    void testTreat2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.treat((CollectionJoin<User, Message>) null, null));
    }

    @Test
    void testTreat3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.treat((SetJoin<User, Message>) null, null));
    }

    @Test
    void testTreat4() {
        assertThrows(UnsupportedOperationException.class, () -> cb.treat((ListJoin<User, Message>) null, null));
    }

    @Test
    void testTreat5() {
        assertThrows(UnsupportedOperationException.class, () -> cb.treat((MapJoin<String, User, Message>) null, null));
    }

    @Test
    void testTreat6() {
        assertThrows(UnsupportedOperationException.class, () -> cb.treat((Path<Object>) null, null));
    }

    @Test
    void testTreat7() {
        assertThrows(UnsupportedOperationException.class, () -> cb.treat((Root<Object>) null, null));
    }
}
