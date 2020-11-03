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

public class MinijaxCriteriaBuilderTest {
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

    @Test
    public void testNot() {
        final MinijaxIn<String> inPredicate = new MinijaxIn<>(MinijaxExpression.ofLiteral("foo"));
        assertFalse(inPredicate.isNegated());
        cb.not(inPredicate);
        assertTrue(inPredicate.isNegated());
    }

    /*
     * Unsupported
     */

    @Test
    public void testCreateCriteriaUpdate() {
        assertThrows(UnsupportedOperationException.class, () -> cb.createCriteriaUpdate(null));
    }

    @Test
    public void testCreateCriteriaDelete() {
        assertThrows(UnsupportedOperationException.class, () -> cb.createCriteriaDelete(null));
    }

    @Test
    public void testConstruct() {
        assertThrows(UnsupportedOperationException.class, () -> cb.construct(null));
    }

    @Test
    public void testArray() {
        assertThrows(UnsupportedOperationException.class, () -> cb.array());
    }

    @Test
    public void testAvg() {
        assertThrows(UnsupportedOperationException.class, () -> cb.avg(null));
    }

    @Test
    public void testMax() {
        assertThrows(UnsupportedOperationException.class, () -> cb.max(null));
    }

    @Test
    public void testMin() {
        assertThrows(UnsupportedOperationException.class, () -> cb.min(null));
    }

    @Test
    public void testGreatest() {
        assertThrows(UnsupportedOperationException.class, () -> cb.greatest(null));
    }

    @Test
    public void testLeast() {
        assertThrows(UnsupportedOperationException.class, () -> cb.least(null));
    }

    @Test
    public void testCount() {
        assertThrows(UnsupportedOperationException.class, () -> cb.count(null));
    }

    @Test
    public void testCountDistinct() {
        assertThrows(UnsupportedOperationException.class, () -> cb.countDistinct(null));
    }

    @Test
    public void testExists() {
        assertThrows(UnsupportedOperationException.class, () -> cb.exists(null));
    }

    @Test
    public void testAll() {
        assertThrows(UnsupportedOperationException.class, () -> cb.all(null));
    }

    @Test
    public void testSome() {
        assertThrows(UnsupportedOperationException.class, () -> cb.some(null));
    }

    @Test
    public void testAny() {
        assertThrows(UnsupportedOperationException.class, () -> cb.any(null));
    }

    @Test
    public void testConjunction() {
        assertThrows(UnsupportedOperationException.class, () -> cb.conjunction());
    }

    @Test
    public void testDisjunction() {
        assertThrows(UnsupportedOperationException.class, () -> cb.disjunction());
    }

    @Test
    public void testIsTrue() {
        assertThrows(UnsupportedOperationException.class, () -> cb.isTrue(null));
    }

    @Test
    public void testIsFalse() {
        assertThrows(UnsupportedOperationException.class, () -> cb.isFalse(null));
    }

    @Test
    public void testNotEqual1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.notEqual(null, (Expression<Object>) null));
    }

    @Test
    public void testNotEqual2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.notEqual(null, (Object) null));
    }

    @Test
    public void testGreaterThan1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.greaterThan((Expression<Integer>) null, (Expression<Integer>) null));
    }

    @Test
    public void testGreaterThan2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.greaterThan((Expression<Integer>) null, (Integer) null));
    }

    @Test
    public void testGreaterThanOrEqualTo1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.greaterThanOrEqualTo((Expression<Integer>) null, (Expression<Integer>) null));
    }

    @Test
    public void testGreaterThanOrEqualTo2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.greaterThanOrEqualTo((Expression<Integer>) null, (Integer) null));
    }

    @Test
    public void testLessThan1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.lessThan((Expression<Integer>) null, (Expression<Integer>) null));
    }

    @Test
    public void testLessThan2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.lessThan((Expression<Integer>) null, (Integer) null));
    }

    @Test
    public void testLessThanOrEqualTo1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.lessThanOrEqualTo((Expression<Integer>) null, (Expression<Integer>) null));
    }

    @Test
    public void testLessThanOrEqualTo() {
        assertThrows(UnsupportedOperationException.class, () -> cb.lessThanOrEqualTo((Expression<Integer>) null, (Integer) null));
    }

    @Test
    public void testBetween1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.between((Expression<Integer>) null, (Expression<Integer>) null, (Expression<Integer>) null));
    }

    @Test
    public void testBetween2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.between((Expression<Integer>) null, (Integer) null, (Integer) null));
    }

    @Test
    public void testGt1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.gt((Expression<Integer>) null, (Expression<Integer>) null));
    }

    @Test
    public void testGt2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.gt((Expression<Integer>) null, (Integer) null));
    }

    @Test
    public void testGe1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.ge((Expression<Integer>) null, (Expression<Integer>) null));
    }

    @Test
    public void testGe2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.ge((Expression<Integer>) null, (Integer) null));
    }

    @Test
    public void testLt1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.lt((Expression<Integer>) null, (Expression<Integer>) null));
    }

    @Test
    public void testLt2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.lt((Expression<Integer>) null, (Integer) null));
    }

    @Test
    public void testLe1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.le((Expression<Integer>) null, (Expression<Integer>) null));
    }

    @Test
    public void testLe2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.le((Expression<Integer>) null, (Integer) null));
    }

    @Test
    public void testNeg() {
        assertThrows(UnsupportedOperationException.class, () -> cb.neg(null));
    }

    @Test
    public void testAbs() {
        assertThrows(UnsupportedOperationException.class, () -> cb.abs(null));
    }

    @Test
    public void testSum1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.sum((Expression<Integer>) null, (Expression<Integer>) null));
    }

    @Test
    public void testSum2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.sum((Expression<Integer>) null, (Integer) null));
    }

    @Test
    public void testSum3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.sum((Integer) null, (Expression<Integer>) null));
    }

    @Test
    public void testSum4() {
        assertThrows(UnsupportedOperationException.class, () -> cb.sum(null));
    }

    @Test
    public void testSumAsLong() {
        assertThrows(UnsupportedOperationException.class, () -> cb.sumAsLong(null));
    }

    @Test
    public void testSumAsDouble() {
        assertThrows(UnsupportedOperationException.class, () -> cb.sumAsDouble(null));
    }

    @Test
    public void testProd1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.prod((Expression<Integer>) null, (Expression<Integer>) null));
    }

    @Test
    public void testProd2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.prod((Expression<Integer>) null, (Integer) null));
    }

    @Test
    public void testProd3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.prod((Integer) null, (Expression<Integer>) null));
    }

    @Test
    public void testDiff1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.diff((Expression<Integer>) null, (Expression<Integer>) null));
    }

    @Test
    public void testDiff2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.diff((Expression<Integer>) null, (Integer) null));
    }

    @Test
    public void testDiff3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.diff((Integer) null, (Expression<Integer>) null));
    }

    @Test
    public void testQuot1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.quot((Expression<Integer>) null, (Expression<Integer>) null));
    }

    @Test
    public void testQuot2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.quot((Expression<Integer>) null, (Integer) null));
    }

    @Test
    public void testQuot3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.quot((Integer) null, (Expression<Integer>) null));
    }

    @Test
    public void testMod1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.mod((Expression<Integer>) null, (Expression<Integer>) null));
    }

    @Test
    public void testMod2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.mod((Expression<Integer>) null, (Integer) null));
    }

    @Test
    public void testMod3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.mod((Integer) null, (Expression<Integer>) null));
    }

    @Test
    public void testSqrt() {
        assertThrows(UnsupportedOperationException.class, () -> cb.sqrt(null));
    }

    @Test
    public void testToLong() {
        assertThrows(UnsupportedOperationException.class, () -> cb.toLong(null));
    }

    @Test
    public void testToInteger() {
        assertThrows(UnsupportedOperationException.class, () -> cb.toInteger(null));
    }

    @Test
    public void testToFloat() {
        assertThrows(UnsupportedOperationException.class, () -> cb.toFloat(null));
    }

    @Test
    public void testToDouble() {
        assertThrows(UnsupportedOperationException.class, () -> cb.toDouble(null));
    }

    @Test
    public void testToBigDecimal() {
        assertThrows(UnsupportedOperationException.class, () -> cb.toBigDecimal(null));
    }

    @Test
    public void testToBigInteger() {
        assertThrows(UnsupportedOperationException.class, () -> cb.toBigInteger(null));
    }

    @Test
    public void testToString() {
        assertThrows(UnsupportedOperationException.class, () -> cb.toString(null));
    }

    @Test
    public void testLiteral() {
        assertThrows(UnsupportedOperationException.class, () -> cb.literal(null));
    }

    @Test
    public void testNullLiteral() {
        assertThrows(UnsupportedOperationException.class, () -> cb.nullLiteral(null));
    }

    @Test
    public void testParameter1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.parameter(null));
    }

    @Test
    public void testParameter2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.parameter(null, null));
    }

    @Test
    public void testIsEmpty() {
        assertThrows(UnsupportedOperationException.class, () -> cb.isEmpty(null));
    }

    @Test
    public void testIsNotEmpty() {
        assertThrows(UnsupportedOperationException.class, () -> cb.isNotEmpty(null));
    }

    @Test
    public void testSize1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.size((Expression<Collection<Object>>) null));
    }

    @Test
    public void testSize2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.size((Collection<Object>) null));
    }

    @Test
    public void testIsMember1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.isMember((Expression<Widget>) null, (Expression<List<Widget>>) null));
    }

    @Test
    public void testIsMember2() {
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
    public void testValues() {
        assertThrows(UnsupportedOperationException.class, () -> cb.values(null));
    }

    @Test
    public void testKeys() {
        assertThrows(UnsupportedOperationException.class, () -> cb.keys(null));
    }

    @Test
    public void testLike1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.like((Expression<String>) null, (Expression<String>) null, (Expression<Character>) null));
    }

    @Test
    public void testLike2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.like((Expression<String>) null, (Expression<String>) null, ' '));
    }

    @Test
    public void testLike3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.like((Expression<String>) null, (String) null, (Expression<Character>) null));
    }

    @Test
    public void testLike4() {
        assertThrows(UnsupportedOperationException.class, () -> cb.like((Expression<String>) null, (String) null, ' '));
    }

    @Test
    public void testNotLike1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.notLike((Expression<String>) null, (Expression<String>) null));
    }

    @Test
    public void testNotLike2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.notLike((Expression<String>) null, (String) null));
    }

    @Test
    public void testNotLike3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.notLike((Expression<String>) null, (Expression<String>) null, (Expression<Character>) null));
    }

    @Test
    public void testNotLike4() {
        assertThrows(UnsupportedOperationException.class, () -> cb.notLike((Expression<String>) null, (Expression<String>) null, ' '));
    }

    @Test
    public void testNotLike5() {
        assertThrows(UnsupportedOperationException.class, () -> cb.notLike((Expression<String>) null, (String) null, (Expression<Character>) null));
    }

    @Test
    public void testNotLike6() {
        assertThrows(UnsupportedOperationException.class, () -> cb.notLike((Expression<String>) null, (String) null, ' '));
    }

    @Test
    public void testConcat1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.concat((Expression<String>) null, (Expression<String>) null));
    }

    @Test
    public void testConcat2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.concat((Expression<String>) null, (String) null));
    }

    @Test
    public void testConcat3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.concat((String) null, (Expression<String>) null));
    }

    @Test
    public void testSubstring1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.substring((Expression<String>) null, (Expression<Integer>) null));
    }

    @Test
    public void testSubstring2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.substring((Expression<String>) null, 0));
    }

    @Test
    public void testSubstring3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.substring((Expression<String>) null, (Expression<Integer>) null, (Expression<Integer>) null));
    }

    @Test
    public void testSubstring4() {
        assertThrows(UnsupportedOperationException.class, () -> cb.substring((Expression<String>) null, 0, 0));
    }

    @Test
    public void testTrim1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.trim(null));
    }

    @Test
    public void testTrim2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.trim((Trimspec) null, (Expression<String>) null));
    }

    @Test
    public void testTrim3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.trim((Expression<Character>) null, (Expression<String>) null));
    }

    @Test
    public void testTrim4() {
        assertThrows(UnsupportedOperationException.class, () -> cb.trim((Trimspec) null, (Expression<Character>) null, (Expression<String>) null));
    }

    @Test
    public void testTrim5() {
        assertThrows(UnsupportedOperationException.class, () -> cb.trim(' ', (Expression<String>) null));
    }

    @Test
    public void testTrim6() {
        assertThrows(UnsupportedOperationException.class, () -> cb.trim((Trimspec) null, ' ', (Expression<String>) null));
    }

    @Test
    public void testLength() {
        assertThrows(UnsupportedOperationException.class, () -> cb.length(null));
    }

    @Test
    public void testLocate1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.locate((Expression<String>) null, (Expression<String>) null));
    }

    @Test
    public void testLocate2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.locate((Expression<String>) null, (String) null));
    }

    @Test
    public void testLocate3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.locate((Expression<String>) null, (Expression<String>) null, (Expression<Integer>) null));
    }

    @Test
    public void testLocate4() {
        assertThrows(UnsupportedOperationException.class, () -> cb.locate((Expression<String>) null, (String) null, 0));
    }

    @Test
    public void testCurrentDate() {
        assertThrows(UnsupportedOperationException.class, () -> cb.currentDate());
    }

    @Test
    public void testCurrentTimestamp() {
        assertThrows(UnsupportedOperationException.class, () -> cb.currentTimestamp());
    }

    @Test
    public void testCurrentTime() {
        assertThrows(UnsupportedOperationException.class, () -> cb.currentTime());
    }

    @Test
    public void testCoalesce1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.coalesce());
    }

    @Test
    public void testCoalesce2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.coalesce((Expression<Widget>) null, (Expression<Widget>) null));
    }

    @Test
    public void testCoalesce3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.coalesce((Expression<Widget>) null, (Widget) null));
    }

    @Test
    public void testNullif1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.<Widget>nullif((Expression<Widget>) null, (Expression<?>) null));
    }

    @Test
    public void testNullif2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.<Widget>nullif((Expression<Widget>) null, (Widget) null));
    }

    @Test
    public void testSelectCase1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.selectCase(null));
    }

    @Test
    public void testSelectCase2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.selectCase());
    }

    @Test
    public void testFunction() {
        assertThrows(UnsupportedOperationException.class, () -> cb.function(null, null));
    }

    @Test
    public void testTreat1() {
        assertThrows(UnsupportedOperationException.class, () -> cb.treat((Join<User, Message>) null, (Class<Message>) null));
    }

    @Test
    public void testTreat2() {
        assertThrows(UnsupportedOperationException.class, () -> cb.treat((CollectionJoin<User, Message>) null, (Class<Message>) null));
    }

    @Test
    public void testTreat3() {
        assertThrows(UnsupportedOperationException.class, () -> cb.treat((SetJoin<User, Message>) null, (Class<Message>) null));
    }

    @Test
    public void testTreat4() {
        assertThrows(UnsupportedOperationException.class, () -> cb.treat((ListJoin<User, Message>) null, (Class<Message>) null));
    }

    @Test
    public void testTreat5() {
        assertThrows(UnsupportedOperationException.class, () -> cb.treat((MapJoin<String, User, Message>) null, (Class<Message>) null));
    }

    @Test
    public void testTreat6() {
        assertThrows(UnsupportedOperationException.class, () -> cb.treat((Path<Object>) null, null));
    }

    @Test
    public void testTreat7() {
        assertThrows(UnsupportedOperationException.class, () -> cb.treat((Root<Object>) null, null));
    }
}
