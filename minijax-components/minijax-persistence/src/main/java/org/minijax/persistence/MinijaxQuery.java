package org.minijax.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.minijax.persistence.criteria.MinijaxCriteriaQuery;

public class MinijaxQuery<T> extends MinijaxBaseTypedQuery<T> {
    private final MinijaxEntityManager em;
    private final MinijaxCriteriaQuery<T> criteriaQuery;
    private final Map<String, Object> namedParams;
    private final Map<Integer, Object> positionalParams;
    private int firstResult;
    private int maxResults;

    public MinijaxQuery(final MinijaxEntityManager em, final MinijaxCriteriaQuery<T> criteriaQuery) {
        this.em = Objects.requireNonNull(em);
        this.criteriaQuery = criteriaQuery;
        this.namedParams = new HashMap<>();
        this.positionalParams = new HashMap<>();
    }

    public MinijaxEntityManager getEm() {
        return em;
    }

    public MinijaxCriteriaQuery<T> getCriteriaQuery() {
        return criteriaQuery;
    }

    public Map<String, Object> getNamedParamters() {
        return this.namedParams;
    }

    public Map<Integer, Object> getPositionalParams() {
        return positionalParams;
    }

    @Override
    public MinijaxQuery<T> setParameter(final String name, final Object value) {
        this.namedParams.put(name, value);
        return this;
    }

    @Override
    public Object getParameterValue(final String name) {
        return this.namedParams.get(name);
    }

    @Override
    public MinijaxQuery<T> setParameter(final int position, final Object value) {
        this.positionalParams.put(position, value);
        return this;
    }

    @Override
    public int getFirstResult() {
        return firstResult;
    }

    @Override
    public MinijaxQuery<T> setFirstResult(final int firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    @Override
    public int getMaxResults() {
        return maxResults;
    }

    @Override
    public MinijaxQuery<T> setMaxResults(final int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    @Override
    public List<T> getResultList() {
        return em.getDialect().getResultList(em, this);
    }
}