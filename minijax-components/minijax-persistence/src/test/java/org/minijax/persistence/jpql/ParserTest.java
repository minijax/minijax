package org.minijax.persistence.jpql;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.MinijaxEntityManagerFactory;
import org.minijax.persistence.MinijaxPersistenceProvider;
import org.minijax.persistence.criteria.MinijaxCriteriaQuery;
import org.minijax.persistence.testmodel.Widget;

class ParserTest {
    private MinijaxEntityManagerFactory emf;
    private MinijaxEntityManager em;

    @BeforeEach
    public void setUp() {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        emf = provider.createEntityManagerFactory("testdb", null);
        em = emf.createEntityManager();
    }

    @AfterEach
    public void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    void testParseSelect() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w WHERE id = :id"));

        assertNotNull(query);
    }

    @Test
    void testParseSelectConjunction() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w WHERE id = :id AND name = :name"));

        assertNotNull(query);
    }

    @Test
    void testParseSelectDisjunction() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w WHERE id = :id OR name = :name"));

        assertNotNull(query);
    }

    @Test
    void testParseEqualsNamedVariable() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w WHERE w.name = :names"));

        assertNotNull(query);
    }

    @Test
    void testParseEqualsPositionalVariable() {
        final List<Widget> result = em
                .createQuery("SELECT w FROM Widget w WHERE w.name = ?1", Widget.class)
                .setParameter(1, "baz")
                .getResultList();

        assertNotNull(result);
    }

    @Test
    void testParseEqualsNumber() {
        final List<Widget> result = em
                .createQuery("SELECT w FROM Widget w WHERE w.id = 123", Widget.class)
                .getResultList();

        assertNotNull(result);
    }

    @Test
    void testParseEqualsDouble() {
        final List<Widget> result = em
                .createQuery("SELECT w FROM Widget w WHERE w.id = 1.0", Widget.class)
                .getResultList();

        assertNotNull(result);
    }

    @Test
    void testParseIsNull() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w WHERE w.name IS NULL"));

        assertNotNull(query);
    }

    @Test
    void testParseIsNotNull() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w WHERE w.name IS NOT NULL"));

        assertNotNull(query);
    }

    @Test
    void testParseIn() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w WHERE w.name IN('foo', 'bar')"));

        assertNotNull(query);
    }

    @Test
    void testParseInNamedVariable() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w WHERE w.name IN :names"));

        assertNotNull(query);
    }

    @Test
    void testParseInPositionalVariable() {
        final List<Widget> result = em
                .createQuery("SELECT w FROM Widget w WHERE w.name IN ?1", Widget.class)
                .setParameter(1, Arrays.asList("foo", "bar"))
                .getResultList();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testParseNotIn() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w WHERE w.name NOT IN('foo', 'bar')"));

        assertNotNull(query);
    }

    @Test
    void testParseNotInNamedVariable() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w WHERE w.name NOT IN :names"));

        assertNotNull(query);
    }

    @Test
    void testParseNotInPositionalVariable() {
        final List<Widget> result = em
                .createQuery("SELECT w FROM Widget w WHERE w.name NOT IN ?1", Widget.class)
                .setParameter(1, Arrays.asList("foo", "bar"))
                .getResultList();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testParseLower() {
        final List<Widget> result = em
                .createQuery("SELECT w FROM Widget w WHERE LOWER(w.name) = 'foo'", Widget.class)
                .getResultList();

        assertNotNull(result);
    }

    @Test
    void testParseLike() {
        final List<Widget> result = em
                .createQuery("SELECT w FROM Widget w WHERE LOWER(w.name) LIKE 'foo'", Widget.class)
                .getResultList();

        assertNotNull(result);
    }

    @Test
    void testParseOrderBy() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w ORDER BY w.name"));

        assertNotNull(query);
    }

    @Test
    void testParseOrderByAsc() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w ORDER BY w.name ASC"));

        assertNotNull(query);
    }

    @Test
    void testParseOrderByDesc() {
        final MinijaxCriteriaQuery<Widget> query = Parser.parse(
                em.getCriteriaBuilder(),
                Widget.class,
                Tokenizer.tokenize("SELECT w FROM Widget w ORDER BY w.name DESC"));

        assertNotNull(query);
    }
}
