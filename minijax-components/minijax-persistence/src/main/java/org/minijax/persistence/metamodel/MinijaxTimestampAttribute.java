package org.minijax.persistence.metamodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.minijax.persistence.MinijaxEntityManager;

public class MinijaxTimestampAttribute<X> extends MinijaxSingularAttribute<X, Timestamp> {

    MinijaxTimestampAttribute(final MinijaxAttribute.Builder<X, Timestamp, ?> builder) {
        super(builder);
    }

    @Override
    public void read(final MinijaxEntityManager em, final X instance, final ResultSet rs, final MutableInt columnIndex) throws SQLException {
        memberWrapper.setValue(instance, rs.getTimestamp(columnIndex.next()));
    }

    @Override
    public Timestamp write(final MinijaxEntityManager em, final X instance) {
        return memberWrapper.getValue(instance);
    }
}
