package org.minijax.persistence.metamodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.minijax.persistence.MinijaxEntityManager;

public class MinijaxListAttribute<X, E>
        extends MinijaxPluralAttribute<X, List<E>, E>
        implements jakarta.persistence.metamodel.ListAttribute<X, E> {

    MinijaxListAttribute(final MinijaxAttribute.Builder<X, List<E>, E> builder) {
        super(builder);
    }

    @Override
    public CollectionType getCollectionType() {
        return CollectionType.LIST;
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
