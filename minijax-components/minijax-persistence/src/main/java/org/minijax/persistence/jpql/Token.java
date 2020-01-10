package org.minijax.persistence.jpql;

import java.util.Objects;

public class Token {
    private final TokenType tokenType;
    private final String value;
    private final int line;
    private final int column;

    public Token(final TokenType tokenType, final String value, final int line, final int column) {
        this.tokenType = Objects.requireNonNull(tokenType);
        this.value = Objects.requireNonNull(value);
        this.line = line;
        this.column = column;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Token)) {
            return false;
        }
        final Token other = (Token) obj;
        return tokenType.equals(other.tokenType) &&
                value.equals(other.value) &&
                column == other.column &&
                line == other.line;
    }

    @Override
    public String toString() {
        return "Token [tokenType=" + tokenType + ", value=" + value + ", line=" + line + ", column=" + column + "]";
    }
}
