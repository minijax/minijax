package org.minijax.persistence;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

public abstract class MinijaxBaseTypedQuery<X> implements javax.persistence.TypedQuery<X> {

    @Override
    public X getSingleResult() {
        final List<X> results = this.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    /*
     * Unsupported
     */

    @Override
    public int executeUpdate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMaxResults() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getFirstResult() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getHints() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Parameter<?>> getParameters() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Parameter<?> getParameter(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Parameter<T> getParameter(final String name, final Class<T> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Parameter<?> getParameter(final int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Parameter<T> getParameter(final int position, final Class<T> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isBound(final Parameter<?> param) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T getParameterValue(final Parameter<T> param) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getParameterValue(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getParameterValue(final int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FlushModeType getFlushMode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public LockModeType getLockMode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(final Class<T> cls) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TypedQuery<X> setMaxResults(final int maxResult) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TypedQuery<X> setFirstResult(final int startPosition) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TypedQuery<X> setHint(final String hintName, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> TypedQuery<X> setParameter(final Parameter<T> param, final T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TypedQuery<X> setParameter(final Parameter<Calendar> param, final Calendar value, final TemporalType temporalType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TypedQuery<X> setParameter(final Parameter<Date> param, final Date value, final TemporalType temporalType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TypedQuery<X> setParameter(final String name, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TypedQuery<X> setParameter(final String name, final Calendar value, final TemporalType temporalType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TypedQuery<X> setParameter(final String name, final Date value, final TemporalType temporalType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TypedQuery<X> setParameter(final int position, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TypedQuery<X> setParameter(final int position, final Calendar value, final TemporalType temporalType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TypedQuery<X> setParameter(final int position, final Date value, final TemporalType temporalType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TypedQuery<X> setFlushMode(final FlushModeType flushMode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TypedQuery<X> setLockMode(final LockModeType lockMode) {
        throw new UnsupportedOperationException();
    }
}
