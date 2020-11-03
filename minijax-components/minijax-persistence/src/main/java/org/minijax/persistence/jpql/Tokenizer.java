package org.minijax.persistence.jpql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.persistence.PersistenceException;

public class Tokenizer {
    private static final Map<String, TokenType> KEYWORDS;

    static {
        KEYWORDS = Map.ofEntries(
            Map.entry("SELECT", TokenType.KEYWORD_SELECT),
            Map.entry("UPDATE", TokenType.KEYWORD_UPDATE),
            Map.entry("DELETE", TokenType.KEYWORD_DELETE),
            Map.entry("AS", TokenType.KEYWORD_AS),
            Map.entry("FROM", TokenType.KEYWORD_FROM),
            Map.entry("LEFT", TokenType.KEYWORD_LEFT),
            Map.entry("RIGHT", TokenType.KEYWORD_RIGHT),
            Map.entry("INNER", TokenType.KEYWORD_INNER),
            Map.entry("OUTER", TokenType.KEYWORD_OUTER),
            Map.entry("JOIN", TokenType.KEYWORD_JOIN),
            Map.entry("ON", TokenType.KEYWORD_ON),
            Map.entry("WHERE", TokenType.KEYWORD_WHERE),
            Map.entry("IN", TokenType.KEYWORD_IN),
            Map.entry("IS", TokenType.KEYWORD_IS),
            Map.entry("NOT", TokenType.KEYWORD_NOT),
            Map.entry("NULL", TokenType.KEYWORD_NULL),
            Map.entry("LIKE", TokenType.KEYWORD_LIKE),
            Map.entry("LOWER", TokenType.KEYWORD_LOWER),
            Map.entry("UPPER", TokenType.KEYWORD_UPPER),
            Map.entry("AND", TokenType.KEYWORD_AND),
            Map.entry("OR", TokenType.KEYWORD_OR),
            Map.entry("ORDER", TokenType.KEYWORD_ORDER),
            Map.entry("BY", TokenType.KEYWORD_BY),
            Map.entry("ASC", TokenType.KEYWORD_ASC),
            Map.entry("DESC", TokenType.KEYWORD_DESC),
            Map.entry("GROUP", TokenType.KEYWORD_GROUP),
            Map.entry("HAVING", TokenType.KEYWORD_HAVING));
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
