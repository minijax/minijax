package org.minijax.persistence.metamodel;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.minijax.persistence.MinijaxEntityManager;

public class MinijaxBinaryAttribute<X> extends MinijaxSingularAttribute<X, byte[]> {

    MinijaxBinaryAttribute(final MinijaxAttribute.Builder<X, byte[], ?> builder) {
        super(builder);
    }

    @Override
    public void read(final MinijaxEntityManager em, final X instance, final ResultSet rs, final MutableInt columnIndex) throws SQLException {
        memberWrapper.setValue(instance, rs.getBytes(columnIndex.next()));
    }

    @Override
    public byte[] write(final MinijaxEntityManager em, final X instance) {
        return memberWrapper.getValue(instance);
    }
}
