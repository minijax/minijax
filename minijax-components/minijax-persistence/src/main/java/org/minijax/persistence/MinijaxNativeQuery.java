package org.minijax.persistence;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MinijaxNativeQuery<T> extends MinijaxBaseTypedQuery<T> {
    private final MinijaxEntityManager em;
    private final Class<T> resultType;
    private final String sql;
    private final List<Object> params;

    public MinijaxNativeQuery(final MinijaxEntityManager em, final Class<T> resultType, final String sql, final Object... params) {
        this.em = Objects.requireNonNull(em);
        this.resultType = Objects.requireNonNull(resultType);
        this.sql = Objects.requireNonNull(sql);
        this.params = Arrays.asList(Objects.requireNonNull(params));
    }

    public Class<T> getResultType() {
        return resultType;
    }

    public String getSql() {
        return sql;
    }

    public List<Object> getParams() {
        return params;
    }

    @Override
    public List<T> getResultList() {
        return em.getDialect().getResultList(em, this);
    }
}
