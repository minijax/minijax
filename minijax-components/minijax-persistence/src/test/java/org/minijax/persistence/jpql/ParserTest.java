package org.minijax.persistence.jpql;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.MinijaxEntityManagerFactory;
import org.minijax.persistence.MinijaxPersistenceProvider;
import org.minijax.persistence.Widget;
import org.minijax.persistence.criteria.MinijaxCriteriaQuery;
import org.minijax.persistence.jpql.Parser;
import org.minijax.persistence.jpql.Tokenizer;

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
}
