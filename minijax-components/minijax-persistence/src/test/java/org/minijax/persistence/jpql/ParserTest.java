package org.minijax.persistence.jpql;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.MinijaxEntityManagerFactory;
import org.minijax.persistence.MinijaxPersistenceProvider;
import org.minijax.persistence.criteria.MinijaxCriteriaQuery;
import org.minijax.persistence.testmodel.Widget;

public class ParserTest {
    private MinijaxEntityManagerFactory emf;
    private MinijaxEntityManager em;

    @Before
    public void setUp() {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        emf = provider.createEntityManagerFactory("testdb", null);
        em = emf.createEntityManager();
    }

    @After
    public void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    public void testParseSelect() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w WHERE id = :id"));

        assertNotNull(query);
    }

    @Test
    public void testParseSelectConjunction() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w WHERE id = :id AND name = :name"));

        assertNotNull(query);
    }

    @Test
    public void testParseSelectDisjunction() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w WHERE id = :id OR name = :name"));

        assertNotNull(query);
    }

    @Test
    public void testParseEqualsNamedVariable() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w WHERE w.name = :names"));

        assertNotNull(query);
    }

    @Test
    public void testParseEqualsPositionalVariable() {
        final List<Widget> result = em
                .createQuery("SELECT w FROM Widget w WHERE w.name = ?1", Widget.class)
                .setParameter(1, "baz")
                .getResultList();

        assertNotNull(result);
    }

    @Test
    public void testParseEqualsNumber() {
        final List<Widget> result = em
                .createQuery("SELECT w FROM Widget w WHERE w.id = 123", Widget.class)
                .getResultList();

        assertNotNull(result);
    }

    @Test
    public void testParseEqualsDouble() {
        final List<Widget> result = em
                .createQuery("SELECT w FROM Widget w WHERE w.id = 1.0", Widget.class)
                .getResultList();

        assertNotNull(result);
    }

    @Test
    public void testParseIsNull() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w WHERE w.name IS NULL"));

        assertNotNull(query);
    }

    @Test
    public void testParseIsNotNull() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w WHERE w.name IS NOT NULL"));

        assertNotNull(query);
    }

    @Test
    public void testParseIn() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w WHERE w.name IN('foo', 'bar')"));

        assertNotNull(query);
    }

    @Test
    public void testParseInNamedVariable() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w WHERE w.name IN :names"));

        assertNotNull(query);
    }

    @Test
    public void testParseInPositionalVariable() {
        final List<Widget> result = em
                .createQuery("SELECT w FROM Widget w WHERE w.name IN ?1", Widget.class)
                .setParameter(1, Arrays.asList("foo", "bar"))
                .getResultList();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testParseLower() {
        final List<Widget> result = em
                .createQuery("SELECT w FROM Widget w WHERE LOWER(w.name) = 'foo'", Widget.class)
                .getResultList();

        assertNotNull(result);
    }

    @Test
    public void testParseLike() {
        final List<Widget> result = em
                .createQuery("SELECT w FROM Widget w WHERE LOWER(w.name) LIKE 'foo'", Widget.class)
                .getResultList();

        assertNotNull(result);
    }

    @Test
    public void testParseOrderBy() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w ORDER BY w.name"));

        assertNotNull(query);
    }

    @Test
    public void testParseOrderByAsc() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w ORDER BY w.name ASC"));

        assertNotNull(query);
    }

    @Test
    public void testParseOrderByDesc() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w ORDER BY w.name DESC"));

        assertNotNull(query);
    }
}
