package org.minijax.persistence.jpql;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class TokenizerTest {

    @Test
    void testSelectByVariable() {
        final List<Token> tokens = Tokenizer.tokenize("SELECT w FROM Widget w WHERE id = :id");
        assertNotNull(tokens);
        assertEquals(9, tokens.size());

        final List<Token> expected = Arrays.asList(
                new Token(TokenType.KEYWORD_SELECT, "SELECT", 1, 1),
                new Token(TokenType.SYMBOL, "w", 1, 8),
                new Token(TokenType.KEYWORD_FROM, "FROM", 1, 10),
                new Token(TokenType.SYMBOL, "Widget", 1, 15),
                new Token(TokenType.SYMBOL, "w", 1, 22),
                new Token(TokenType.KEYWORD_WHERE, "WHERE", 1, 24),
                new Token(TokenType.SYMBOL, "id", 1, 30),
                new Token(TokenType.EQUALS, "=", 1, 33),
                new Token(TokenType.NAMED_PARAMETER, "id", 1, 35));

        assertEquals(expected, tokens);
    }

    @Test
    void testSelectByNumber() {
        final List<Token> tokens = Tokenizer.tokenize("SELECT w FROM Widget w WHERE id = 123");
        assertNotNull(tokens);
        assertEquals(9, tokens.size());

        final List<Token> expected = Arrays.asList(
                new Token(TokenType.KEYWORD_SELECT, "SELECT", 1, 1),
                new Token(TokenType.SYMBOL, "w", 1, 8),
                new Token(TokenType.KEYWORD_FROM, "FROM", 1, 10),
                new Token(TokenType.SYMBOL, "Widget", 1, 15),
                new Token(TokenType.SYMBOL, "w", 1, 22),
                new Token(TokenType.KEYWORD_WHERE, "WHERE", 1, 24),
                new Token(TokenType.SYMBOL, "id", 1, 30),
                new Token(TokenType.EQUALS, "=", 1, 33),
                new Token(TokenType.NUMBER, "123", 1, 35));

        assertEquals(expected, tokens);
    }

    @Test
    void testSelectByString() {
        final List<Token> tokens = Tokenizer.tokenize("SELECT w FROM Widget w WHERE id = 'xyz'");
        assertNotNull(tokens);
        assertEquals(9, tokens.size());

        final List<Token> expected = Arrays.asList(
                new Token(TokenType.KEYWORD_SELECT, "SELECT", 1, 1),
                new Token(TokenType.SYMBOL, "w", 1, 8),
                new Token(TokenType.KEYWORD_FROM, "FROM", 1, 10),
                new Token(TokenType.SYMBOL, "Widget", 1, 15),
                new Token(TokenType.SYMBOL, "w", 1, 22),
                new Token(TokenType.KEYWORD_WHERE, "WHERE", 1, 24),
                new Token(TokenType.SYMBOL, "id", 1, 30),
                new Token(TokenType.EQUALS, "=", 1, 33),
                new Token(TokenType.STRING, "xyz", 1, 35));

        assertEquals(expected, tokens);
    }
}
