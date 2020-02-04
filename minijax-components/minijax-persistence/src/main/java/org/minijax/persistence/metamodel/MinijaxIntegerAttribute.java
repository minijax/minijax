package org.minijax.persistence.metamodel;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.minijax.persistence.MinijaxEntityManager;

public class MinijaxIntegerAttribute<X> extends MinijaxSingularAttribute<X, Integer> {

    MinijaxIntegerAttribute(final MinijaxAttribute.Builder<X, Integer, ?> builder) {
        super(builder);
    }

    @Override
    public void read(final MinijaxEntityManager em, final X instance, final ResultSet rs, final MutableInt columnIndex) throws SQLException {
        memberWrapper.setValue(instance, rs.getInt(columnIndex.next()));
    }

    @Override
    public Integer write(final MinijaxEntityManager em, final X instance) {
        return memberWrapper.getValue(instance);
    }
}
