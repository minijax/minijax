package org.minijax.persistence.jpql;

import java.util.List;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;

import org.minijax.commons.MinijaxException;
import org.minijax.persistence.criteria.MinijaxCriteriaBuilder;
import org.minijax.persistence.criteria.MinijaxCriteriaQuery;
import org.minijax.persistence.criteria.MinijaxExpression;
import org.minijax.persistence.criteria.MinijaxNamedParameter;
import org.minijax.persistence.criteria.MinijaxNumberExpression;
import org.minijax.persistence.criteria.MinijaxOrder;
import org.minijax.persistence.criteria.MinijaxPath;
import org.minijax.persistence.criteria.MinijaxPositionalParameter;
import org.minijax.persistence.criteria.MinijaxPredicate;
import org.minijax.persistence.criteria.MinijaxRoot;
import org.minijax.persistence.criteria.MinijaxStringExpression;
import org.minijax.persistence.metamodel.MinijaxEntityType;

public class Parser<T> {
    private final MinijaxCriteriaBuilder cb;
    private final Class<T> resultType;
    private final List<Token> tokens;
    private MinijaxCriteriaQuery<T> query;
    private MinijaxRoot<T> root;
    private int index;

    public static <T> MinijaxCriteriaQuery<T> parse(final MinijaxCriteriaBuilder cb, final Class<T> resultType, final List<Token> tokens) {
        return new Parser<>(cb, resultType, tokens).parse();
    }

    private Parser(final MinijaxCriteriaBuilder cb, final Class<T> resultType, final List<Token> tokens) {
        this.cb = cb;
        this.resultType = resultType;
        this.tokens = tokens;
    }

    private boolean eof() {
        return index >= tokens.size();
    }

    private Token getCurr() {
        return tokens.get(index);
    }

    private Token consume(final TokenType expected) {
        final Token curr = getCurr();
        if (expected != null && curr.getTokenType() != expected) {
            throw new MinijaxException("Unexpected token: " + curr.getTokenType() + " (expected " + expected + ")");
        }
        index++;
        return curr;
    }

    private MinijaxCriteriaQuery<T> parse() {
        final Token curr = getCurr();
        switch (curr.getTokenType()) {
        case KEYWORD_SELECT:
            return parseSelect();

        case KEYWORD_UPDATE:
            return parseUpdate();

        case KEYWORD_DELETE:
            return parseDelete();

        default:
            throw new MinijaxException("Unexpected token: " + curr);
        }
    }

    private MinijaxCriteriaQuery<T> parseSelect() {
        consume(TokenType.KEYWORD_SELECT);

        query = cb.createQuery(resultType);

        // TODO: Parse selection instead of skipping
        while (getCurr().getTokenType() != TokenType.KEYWORD_FROM) {
            index++;
        }

        // TODO: Parse FROM and JOIN instead of skipping
        consume(TokenType.KEYWORD_FROM);

        // TODO: How to get entityType from string name?
        // May need to add extra methods to MinijaxMetamodel
        final MinijaxEntityType<T> resultEntityType = cb.getEntityManager().getMetamodel().entity(resultType);
        final String entityTypeName = consume(TokenType.SYMBOL).getValue();
        if (!resultEntityType.getName().equals(entityTypeName)) {
            throw new MinijaxException(
                    "Result type does not match entity type name (resultEntityType=" +
                    resultEntityType.getName() +
                    ", entityTypeName=" +
                    entityTypeName + ")");
        }
        root = query.from(resultType);

        final String entityTypeAlias = consume(TokenType.SYMBOL).getValue();
        root.alias(entityTypeAlias);

        while (!eof()) {
            final Token curr = getCurr();
            switch (curr.getTokenType()) {
            case KEYWORD_WHERE:
                parseWhere();
                break;

            case KEYWORD_ORDER:
                parseOrderBy();
                break;

            default:
                throw new MinijaxException("Unexpected token: " + curr);
            }
        }

        return query;
    }

    private MinijaxCriteriaQuery<T> parseUpdate() {
        return null;
    }

    private MinijaxCriteriaQuery<T> parseDelete() {
        return null;
    }

    private void parseWhere() {
        consume(TokenType.KEYWORD_WHERE);

        MinijaxPredicate predicate = parsePredicate();

        loop: while (!eof()) {
            final Token curr = getCurr();
            switch (curr.getTokenType()) {
            case KEYWORD_AND:
                consume(TokenType.KEYWORD_AND);
                predicate = cb.and(predicate, parsePredicate());
                break;

            case KEYWORD_OR:
                consume(TokenType.KEYWORD_OR);
                predicate = cb.or(predicate, parsePredicate());
                break;

            default:
                break loop;
            }
        }

        query.where(predicate);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private MinijaxPredicate parsePredicate() {
        final Token field = consume(TokenType.SYMBOL);
        final Path<?> fieldPath = root.get(field.getValue());

        final Token operator = consume(null);
        final MinijaxExpression<?> value = parseExpression();

        switch (operator.getTokenType()) {
        case EQUALS:
            return cb.equal(fieldPath, value);

        case KEYWORD_IN:
            return cb.in(fieldPath).value((Expression) value);

        default:
            throw new MinijaxException("Unexpected operator: " + operator);
        }
    }

    private void parseOrderBy() {
        consume(TokenType.KEYWORD_ORDER);
        consume(TokenType.KEYWORD_BY);

        final MinijaxPath<?> path = root.get(consume(TokenType.SYMBOL).getValue());

        final Token curr = getCurr();
        boolean ascending = true;

        if (curr.getTokenType() == TokenType.KEYWORD_ASC) {
            consume(TokenType.KEYWORD_ASC);
        } else if (curr.getTokenType() == TokenType.KEYWORD_DESC) {
            consume(TokenType.KEYWORD_DESC);
            ascending = false;
        }

        query.orderBy(new MinijaxOrder(path, ascending));
    }

    private MinijaxExpression<?> parseExpression() {
        final Token curr = consume(null);
        final String str = curr.getValue();

        switch (curr.getTokenType()) {
        case SYMBOL:
            return root.get(str);

        case STRING:
            return new MinijaxStringExpression(str);

        case NUMBER:
            final Number number = str.indexOf('.') > 0 ? Double.parseDouble(str) : Integer.parseInt(str);
            return new MinijaxNumberExpression(number);

        case NAMED_PARAMETER:
            return new MinijaxNamedParameter(str);

        case POSITIONAL_PARAMETER:
            return new MinijaxPositionalParameter(Integer.parseInt(str));

        default:
            throw new MinijaxException("Unexpected expression: " + curr);
        }
    }
}
