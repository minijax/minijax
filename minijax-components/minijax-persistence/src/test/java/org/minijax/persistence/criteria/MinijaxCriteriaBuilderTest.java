package org.minijax.persistence.criteria;

import static org.junit.Assert.*;

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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.MinijaxEntityManagerFactory;
import org.minijax.persistence.MinijaxPersistenceProvider;
import org.minijax.persistence.dialect.AnsiSqlBuilder;
import org.minijax.persistence.testmodel.Message;
import org.minijax.persistence.testmodel.User;
import org.minijax.persistence.testmodel.Widget;

public class MinijaxCriteriaBuilderTest {
    private MinijaxEntityManagerFactory emf;
    private MinijaxEntityManager em;
    private MinijaxCriteriaBuilder cb;

    @Before
    public void setUp() {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        emf = provider.createEntityManagerFactory("testdb", null);
        em = emf.createEntityManager();
        cb = em.getCriteriaBuilder();
    }

    @After
    public void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    public void testCreateClassQuery() {
        final MinijaxCriteriaQuery<Widget> q = cb.createQuery(Widget.class);
        final MinijaxRoot<Widget> w = q.from(Widget.class);
        q.select(w);
        assertEquals(Widget.class, q.getResultType());
    }

    @Test
    public void testCreateObjectQuery() {
        final MinijaxCriteriaQuery<Object> q = cb.createQuery();
        final MinijaxRoot<Widget> w = q.from(Widget.class);
        q.select(w);
        assertEquals(Object.class, q.getResultType());
    }

    @Test
    public void testCreateTupleQuery() {
        final MinijaxCriteriaQuery<Tuple> q = cb.createTupleQuery();
        final MinijaxRoot<Widget> w = q.from(Widget.class);
        q.select(cb.tuple(w.get("id"), w.get("name")));
        assertEquals(Tuple.class, q.getSelection().getJavaType());
        assertEquals(Tuple.class, q.getResultType());
        assertTrue(q.getSelection().isCompoundSelection());
    }

    @Test
    public void testIsNull() {
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
    public void testIsNotNull() {
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
    public void testLike() {
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
    public void testLikeExpression() {
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
    public void testAnd() {
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
    public void testAnd2() {
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
    public void testOr() {
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
    public void testOr2() {
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
    public void testLower() {
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
    public void testOrderBy() {
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
    public void testOrderByDesc() {
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

    /*
     * Unsupported
     */

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateCriteriaUpdate() {
        cb.createCriteriaUpdate(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateCriteriaDelete() {
        cb.createCriteriaDelete(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConstruct() {
        cb.construct(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testArray() {
        cb.array();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAvg() {
        cb.avg(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMax() {
        cb.max(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMin() {
        cb.min(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGreatest() {
        cb.greatest(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLeast() {
        cb.least(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCount() {
        cb.count(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCountDistinct() {
        cb.countDistinct(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExists() {
        cb.exists(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAll() {
        cb.all(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSome() {
        cb.some(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAny() {
        cb.any(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNot() {
        cb.not(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConjunction() {
        cb.conjunction();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDisjunction() {
        cb.disjunction();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsTrue() {
        cb.isTrue(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsFalse() {
        cb.isFalse(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNotEqual1() {
        cb.notEqual(null, (Expression<Object>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNotEqual2() {
        cb.notEqual(null, (Object) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGreaterThan1() {
        cb.greaterThan((Expression<Integer>) null, (Expression<Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGreaterThan2() {
        cb.greaterThan((Expression<Integer>) null, (Integer) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGreaterThanOrEqualTo1() {
        cb.greaterThanOrEqualTo((Expression<Integer>) null, (Expression<Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGreaterThanOrEqualTo2() {
        cb.greaterThanOrEqualTo((Expression<Integer>) null, (Integer) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLessThan1() {
        cb.lessThan((Expression<Integer>) null, (Expression<Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLessThan2() {
        cb.lessThan((Expression<Integer>) null, (Integer) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLessThanOrEqualTo1() {
        cb.lessThanOrEqualTo((Expression<Integer>) null, (Expression<Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLessThanOrEqualTo() {
        cb.lessThanOrEqualTo((Expression<Integer>) null, (Integer) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBetween1() {
        cb.between((Expression<Integer>) null, (Expression<Integer>) null, (Expression<Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBetween2() {
        cb.between((Expression<Integer>) null, (Integer) null, (Integer) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGt1() {
        cb.gt((Expression<Integer>) null, (Expression<Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGt2() {
        cb.gt((Expression<Integer>) null, (Integer) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGe1() {
        cb.ge((Expression<Integer>) null, (Expression<Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGe2() {
        cb.ge((Expression<Integer>) null, (Integer) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLt1() {
        cb.lt((Expression<Integer>) null, (Expression<Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLt2() {
        cb.lt((Expression<Integer>) null, (Integer) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLe1() {
        cb.le((Expression<Integer>) null, (Expression<Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLe2() {
        cb.le((Expression<Integer>) null, (Integer) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNeg() {
        cb.neg(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbs() {
        cb.abs(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSum1() {
        cb.sum((Expression<Integer>) null, (Expression<Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSum2() {
        cb.sum((Expression<Integer>) null, (Integer) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSum3() {
        cb.sum((Integer) null, (Expression<Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSum4() {
        cb.sum(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSumAsLong() {
        cb.sumAsLong(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSumAsDouble() {
        cb.sumAsDouble(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testProd1() {
        cb.prod((Expression<Integer>) null, (Expression<Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testProd2() {
        cb.prod((Expression<Integer>) null, (Integer) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testProd3() {
        cb.prod((Integer) null, (Expression<Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDiff1() {
        cb.diff((Expression<Integer>) null, (Expression<Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDiff2() {
        cb.diff((Expression<Integer>) null, (Integer) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDiff3() {
        cb.diff((Integer) null, (Expression<Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testQuot1() {
        cb.quot((Expression<Integer>) null, (Expression<Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testQuot2() {
        cb.quot((Expression<Integer>) null, (Integer) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testQuot3() {
        cb.quot((Integer) null, (Expression<Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMod1() {
        cb.mod((Expression<Integer>) null, (Expression<Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMod2() {
        cb.mod((Expression<Integer>) null, (Integer) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMod3() {
        cb.mod((Integer) null, (Expression<Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSqrt() {
        cb.sqrt(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testToLong() {
        cb.toLong(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testToInteger() {
        cb.toInteger(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testToFloat() {
        cb.toFloat(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testToDouble() {
        cb.toDouble(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testToBigDecimal() {
        cb.toBigDecimal(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testToBigInteger() {
        cb.toBigInteger(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testToString() {
        cb.toString(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLiteral() {
        cb.literal(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNullLiteral() {
        cb.nullLiteral(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testParameter1() {
        cb.parameter(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testParameter2() {
        cb.parameter(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsEmpty() {        cb.isEmpty(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsNotEmpty() {        cb.isNotEmpty(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSize1() {        cb.size((Expression<Collection<Object>>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSize2() {        cb.size((Collection<Object>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsMember1() {
        cb.isMember((Expression<Widget>) null, (Expression<List<Widget>>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsMember2() {
        cb.isMember((Widget) null, (Expression<List<Widget>>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void isNotMember1() {
        cb.isNotMember((Expression<Widget>) null, (Expression<List<Widget>>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void isNotMember2() {
        cb.isNotMember((Widget) null, (Expression<List<Widget>>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValues() {
        cb.values(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKeys() {
        cb.keys(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLike1() {
        cb.like((Expression<String>) null, (Expression<String>) null, (Expression<Character>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLike2() {
        cb.like((Expression<String>) null, (Expression<String>) null, ' ');
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLike3() {
        cb.like((Expression<String>) null, (String) null, (Expression<Character>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLike4() {
        cb.like((Expression<String>) null, (String) null, ' ');
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNotLike1() {
        cb.notLike((Expression<String>) null, (Expression<String>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNotLike2() {
        cb.notLike((Expression<String>) null, (String) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNotLike3() {
        cb.notLike((Expression<String>) null, (Expression<String>) null, (Expression<Character>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNotLike4() {
        cb.notLike((Expression<String>) null, (Expression<String>) null, ' ');
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNotLike5() {
        cb.notLike((Expression<String>) null, (String) null, (Expression<Character>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNotLike6() {
        cb.notLike((Expression<String>) null, (String) null, ' ');
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConcat1() {
        cb.concat((Expression<String>) null, (Expression<String>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConcat2() {
        cb.concat((Expression<String>) null, (String) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConcat3() {
        cb.concat((String) null, (Expression<String>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubstring1() {
        cb.substring((Expression<String>) null, (Expression<Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubstring2() {
        cb.substring((Expression<String>) null, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubstring3() {
        cb.substring((Expression<String>) null, (Expression<Integer>) null, (Expression<Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubstring4() {
        cb.substring((Expression<String>) null, 0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTrim1() {
        cb.trim(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTrim2() {
        cb.trim((Trimspec) null, (Expression<String>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTrim3() {
        cb.trim((Expression<Character>) null, (Expression<String>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTrim4() {
        cb.trim((Trimspec) null, (Expression<Character>) null, (Expression<String>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTrim5() {
        cb.trim(' ', (Expression<String>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTrim6() {
        cb.trim((Trimspec) null, ' ', (Expression<String>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLength() {
        cb.length(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLocate1() {
        cb.locate((Expression<String>) null, (Expression<String>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLocate2() {
        cb.locate((Expression<String>) null, (String) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLocate3() {
        cb.locate((Expression<String>) null, (Expression<String>) null, (Expression<Integer>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLocate4() {
        cb.locate((Expression<String>) null, (String) null, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCurrentDate() {
        cb.currentDate();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCurrentTimestamp() {
        cb.currentTimestamp();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCurrentTime() {
        cb.currentTime();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCoalesce1() {
        cb.coalesce();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCoalesce2() {
        cb.coalesce((Expression<Widget>) null, (Expression<Widget>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCoalesce3() {
        cb.coalesce((Expression<Widget>) null, (Widget) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNullif1() {
        cb.<Widget>nullif((Expression<Widget>) null, (Expression<?>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNullif2() {
        cb.<Widget>nullif((Expression<Widget>) null, (Widget) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSelectCase1() {
        cb.selectCase(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSelectCase2() {
        cb.selectCase();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFunction() {
        cb.function(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTreat1() {
        cb.treat((Join<User, Message>) null, (Class<Message>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTreat2() {
        cb.treat((CollectionJoin<User, Message>) null, (Class<Message>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTreat3() {
        cb.treat((SetJoin<User, Message>) null, (Class<Message>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTreat4() {
        cb.treat((ListJoin<User, Message>) null, (Class<Message>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTreat5() {
        cb.treat((MapJoin<String, User, Message>) null, (Class<Message>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTreat6() {
        cb.treat((Path<Object>) null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTreat7() {
        cb.treat((Root<Object>) null, null);
    }
}
