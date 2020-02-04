package org.minijax.persistence.lazy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.minijax.persistence.MinijaxBaseTypedQuery;

public class MockQuery<T> extends MinijaxBaseTypedQuery<T> {
    private final List<T> results;

    @SuppressWarnings("unchecked")
    public MockQuery(final T... results) {
        this.results = new ArrayList<>(Arrays.asList(results));
    }

    @Override
    public List<T> getResultList() {
        return results;
    }
}
