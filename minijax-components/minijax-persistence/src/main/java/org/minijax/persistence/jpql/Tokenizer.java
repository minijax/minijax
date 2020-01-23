package org.minijax.persistence.jpql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceException;

public class Tokenizer {
    private static final Map<String, TokenType> KEYWORDS;

    static {
        final Map<String, TokenType> keywords = new HashMap<>();
        keywords.put("SELECT", TokenType.KEYWORD_SELECT);
        keywords.put("UPDATE", TokenType.KEYWORD_UPDATE);
        keywords.put("DELETE", TokenType.KEYWORD_DELETE);
        keywords.put("AS", TokenType.KEYWORD_AS);
        keywords.put("FROM", TokenType.KEYWORD_FROM);
        keywords.put("LEFT", TokenType.KEYWORD_LEFT);
        keywords.put("RIGHT", TokenType.KEYWORD_RIGHT);
        keywords.put("INNER", TokenType.KEYWORD_INNER);
        keywords.put("OUTER", TokenType.KEYWORD_OUTER);
        keywords.put("JOIN", TokenType.KEYWORD_JOIN);
        keywords.put("ON", TokenType.KEYWORD_ON);
        keywords.put("WHERE", TokenType.KEYWORD_WHERE);
        keywords.put("IN", TokenType.KEYWORD_IN);
        keywords.put("IS", TokenType.KEYWORD_IS);
        keywords.put("NOT", TokenType.KEYWORD_NOT);
        keywords.put("NULL", TokenType.KEYWORD_NULL);
        keywords.put("LIKE", TokenType.KEYWORD_LIKE);
        keywords.put("LOWER", TokenType.KEYWORD_LOWER);
        keywords.put("UPPER", TokenType.KEYWORD_UPPER);
        keywords.put("AND", TokenType.KEYWORD_AND);
        keywords.put("OR", TokenType.KEYWORD_OR);
        keywords.put("ORDER", TokenType.KEYWORD_ORDER);
        keywords.put("BY", TokenType.KEYWORD_BY);
        keywords.put("ASC", TokenType.KEYWORD_ASC);
        keywords.put("DESC", TokenType.KEYWORD_DESC);
        keywords.put("GROUP", TokenType.KEYWORD_GROUP);
        keywords.put("HAVING", TokenType.KEYWORD_HAVING);
        KEYWORDS = Collections.unmodifiableMap(keywords);
    }

    public static List<Token> tokenize(final String str) {
        return new Tokenizer(str).tokenize();
    }

    private final List<Token> result;
    private final String str;
    private final int len;
    private char c;
    private int index;
    private int line;
    private int column;

    private Tokenizer(final String str) {
        this.result = new ArrayList<>();
        this.str = str;
        this.len = str.length();
        this.index = 0;
        this.line = 1;
        this.column = 1;
    }

    private List<Token> tokenize() {
        while (index < len) {
            c = str.charAt(index);
            if (Character.isWhitespace(c)) {
                readWhitespace();
            } else if (Character.isAlphabetic(c)) {
                readSymbol();
            } else if (Character.isDigit(c)) {
                readNumber();
            } else if (c == '\'') {
                readString();
            } else if (c == ':') {
                readNamedParameter();
            } else if (c == '?') {
                readPositionalParameter();
            } else if (c == ',') {
                readSingle(TokenType.COMMA, ",");
            } else if (c == '=') {
                readSingle(TokenType.EQUALS, "=");
            } else if (c == '(') {
                readSingle(TokenType.OPEN_PARENS, "(");
            } else if (c == ')') {
                readSingle(TokenType.CLOSE_PARENS, ")");
            } else {
                throw new PersistenceException("Unknown token: c=" + c + ", line=" + line + ", column=" + column);
            }
        }
        return result;
    }

    private void readWhitespace() {
        while (c != '\0' && Character.isWhitespace(c)) {
            next();
        }
    }

    private void readSymbol() {
        final StringBuilder builder = new StringBuilder();
        final int startLine = line;
        final int startColumn = column;

        while (c != '\0' && (c == '.' || Character.isJavaIdentifierPart(c))) {
            builder.append(c);
            next();
        }

        final String symbol = builder.toString();
        final TokenType tokenType = KEYWORDS.getOrDefault(symbol, TokenType.SYMBOL);
        add(tokenType, symbol, startLine, startColumn);
    }

    private void readNumber() {
        final StringBuilder builder = new StringBuilder();
        final int startLine = line;
        final int startColumn = column;

        while (Character.isDigit(c) || c == '.') {
            builder.append(c);
            next();
        }

        add(TokenType.NUMBER, builder.toString(), startLine, startColumn);
    }

    private void readString() {
        final StringBuilder builder = new StringBuilder();
        final int startLine = line;
        final int startColumn = column;

        // Advance over the opening quote
        next();

        while (c != '\0' && c != '\'') {
            builder.append(c);
            next();
        }

        // Advance over the closing quote
        next();

        add(TokenType.STRING, builder.toString(), startLine, startColumn);
    }

    private void readNamedParameter() {
        final StringBuilder builder = new StringBuilder();
        final int startLine = line;
        final int startColumn = column;

        // Advance over the initial colon
        next();

        while (c != '\0' && Character.isJavaIdentifierPart(c)) {
            builder.append(c);
            next();
        }

        add(TokenType.NAMED_PARAMETER, builder.toString(), startLine, startColumn);
    }

    private void readPositionalParameter() {
        final StringBuilder builder = new StringBuilder();
        final int startLine = line;
        final int startColumn = column;

        // Advance over the initial question mark
        next();

        while (c != '\0' && Character.isDigit(c)) {
            builder.append(c);
            next();
        }

        add(TokenType.POSITIONAL_PARAMETER, builder.toString(), startLine, startColumn);
    }

    private void readSingle(final TokenType tokenType, final String value) {
        add(tokenType, value, line, column);
        next();
    }

    private void next() {
        index++;
        c = index >= len ? '\0' : str.charAt(index);
        if (c == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }
    }

    private void add(final TokenType tokenType, final String value, final int line, final int column) {
        result.add(new Token(tokenType, value, line, column));
    }
}
