package org.minijax.persistence.jpql;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.Expression;

import org.minijax.persistence.criteria.MinijaxCriteriaBuilder;
import org.minijax.persistence.criteria.MinijaxCriteriaQuery;
import org.minijax.persistence.criteria.MinijaxExpression;
import org.minijax.persistence.criteria.MinijaxListExpression;
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
            throw new PersistenceException("Unexpected token: " + curr.getTokenType() + " (expected " + expected + ")");
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
            throw new PersistenceException("Unexpected token: " + curr);
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
        final MinijaxEntityType<T> resultEntityType = cb.getMetamodel().entity(resultType);
        final String entityTypeName = consume(TokenType.SYMBOL).getValue();
        if (!resultEntityType.getName().equals(entityTypeName)) {
            throw new PersistenceException(
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
                throw new PersistenceException("Unexpected token: " + curr);
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

    private MinijaxPath<?> parsePath() {
        final Token field = consume(TokenType.SYMBOL);
        final String[] fieldParts = field.getValue().split("\\.");

        MinijaxPath<?> path = root;
        for (int i = 1; i < fieldParts.length; i++) {
            path = path.get(fieldParts[i]);
        }
        return path;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private MinijaxPredicate parsePredicate() {
        final MinijaxExpression<?> lhs = parseExpression();
        final Token operator = consume(null);

        switch (operator.getTokenType()) {
        case EQUALS:
            return cb.equal(lhs, parseExpression());

        case KEYWORD_IN:
            return cb.in(lhs).value((Expression) parseCollectionExpression());

        case KEYWORD_IS:
            if (getCurr().getTokenType() == TokenType.KEYWORD_NOT) {
                consume(TokenType.KEYWORD_NOT);
                consume(TokenType.KEYWORD_NULL);
                return lhs.isNotNull();
            }
            consume(TokenType.KEYWORD_NULL);
            return lhs.isNull();

        case KEYWORD_LIKE:
            return cb.like((MinijaxExpression<String>) lhs, (MinijaxExpression<String>) parseExpression());

        default:
            throw new PersistenceException("Unexpected operator: " + operator);
        }
    }

    private void parseOrderBy() {
        consume(TokenType.KEYWORD_ORDER);
        consume(TokenType.KEYWORD_BY);

        final MinijaxPath<?> path = parsePath();

        boolean ascending = true;

        if (index < tokens.size()) {
            final Token curr = getCurr();
            if (curr.getTokenType() == TokenType.KEYWORD_ASC) {
                consume(TokenType.KEYWORD_ASC);
            } else if (curr.getTokenType() == TokenType.KEYWORD_DESC) {
                consume(TokenType.KEYWORD_DESC);
                ascending = false;
            }
        }

        query.orderBy(new MinijaxOrder(path, ascending));
    }

    private MinijaxExpression<?> parseExpression() {
        final Token curr = tokens.get(index);
        final String str = curr.getValue();

        switch (curr.getTokenType()) {
        case SYMBOL:
            return parsePath();

        case STRING:
            consume(TokenType.STRING);
            return new MinijaxStringExpression(str);

        case NUMBER:
            consume(TokenType.NUMBER);
            final Number number = str.indexOf('.') >= 0 ? Double.parseDouble(str) : Integer.parseInt(str);
            return new MinijaxNumberExpression(number);

        case NAMED_PARAMETER:
            consume(TokenType.NAMED_PARAMETER);
            return new MinijaxNamedParameter<>(Object.class, str);

        case POSITIONAL_PARAMETER:
            consume(TokenType.POSITIONAL_PARAMETER);
            return new MinijaxPositionalParameter<>(Object.class, Integer.parseInt(str));

        case KEYWORD_LOWER:
            return parseLowerExpression();

        default:
            throw new PersistenceException("Unexpected expression: " + curr);
        }
    }

    private MinijaxExpression<?> parseCollectionExpression() {
        final Token curr = tokens.get(index);
        final String str = curr.getValue();

        switch (curr.getTokenType()) {
        case NAMED_PARAMETER:
            consume(TokenType.NAMED_PARAMETER);
            return new MinijaxNamedParameter<>(Object.class, str);

        case POSITIONAL_PARAMETER:
            consume(TokenType.POSITIONAL_PARAMETER);
            return new MinijaxPositionalParameter<>(Object.class, Integer.parseInt(str));

        case OPEN_PARENS:
            return parseParensCollectionExpression();

        default:
            throw new PersistenceException("Unexpected collection expression: " + curr);
        }
    }

    private MinijaxExpression<?> parseParensCollectionExpression() {
        consume(TokenType.OPEN_PARENS);

        final List<MinijaxExpression<?>> values = new ArrayList<>();

        while (getCurr().getTokenType() != TokenType.CLOSE_PARENS) {
            values.add(parseExpression());

            if (getCurr().getTokenType() == TokenType.COMMA) {
                consume(TokenType.COMMA);
            }
        }

        consume(TokenType.CLOSE_PARENS);
        return new MinijaxListExpression<>(values);
    }

    @SuppressWarnings("unchecked")
    private MinijaxExpression<String> parseLowerExpression() {
        consume(TokenType.KEYWORD_LOWER);
        consume(TokenType.OPEN_PARENS);
        final MinijaxExpression<String> arg = (MinijaxExpression<String>) parseExpression();
        consume(TokenType.CLOSE_PARENS);
        return cb.lower(arg);
    }
}
