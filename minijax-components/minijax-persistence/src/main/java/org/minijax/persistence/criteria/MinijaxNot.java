package org.minijax.persistence.criteria;

public class MinijaxNot extends MinijaxPredicate {
    private final MinijaxPredicate innerPredicate;

    public MinijaxNot(final MinijaxPredicate innerPredicate) {
        this.innerPredicate = innerPredicate;
    }

    public MinijaxPredicate getInnerPredicate() {
        return innerPredicate;
    }

    @Override
    public boolean isNegated() {
        return true;
    }

    @Override
    public MinijaxPredicate not() {
        return innerPredicate;
    }
}
