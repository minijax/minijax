package org.minijax.persistence.metamodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import org.minijax.persistence.MinijaxEntityManager;

public class MinijaxSetAttribute<X, E>
        extends MinijaxPluralAttribute<X, Set<E>, E>
        implements jakarta.persistence.metamodel.SetAttribute<X, E> {

    MinijaxSetAttribute(final MinijaxAttribute.Builder<X, Set<E>, E> builder) {
        super(builder);
    }

    @Override
    public CollectionType getCollectionType() {
        return CollectionType.SET;
    }

    @Override
    public void read(final MinijaxEntityManager em, final X instance, final ResultSet rs, final MutableInt columnIndex)
            throws ReflectiveOperationException, SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object write(final MinijaxEntityManager em, final X instance) {
        throw new UnsupportedOperationException();
    }
}
