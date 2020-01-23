package org.minijax.persistence.metamodel;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.minijax.persistence.MinijaxEntityManager;

public class MinijaxStringAttribute<X> extends MinijaxSingularAttribute<X, String> {

    MinijaxStringAttribute(final MinijaxAttribute.Builder<X, String, ?> builder) {
        super(builder);
    }

    @Override
    public void read(final MinijaxEntityManager em, final X instance, final ResultSet rs, final MutableInt columnIndex) throws SQLException {
        memberWrapper.setValue(instance, rs.getString(columnIndex.next()));
    }

    @Override
    public String write(final MinijaxEntityManager em, final X instance) {
        return memberWrapper.getValue(instance);
    }
}
