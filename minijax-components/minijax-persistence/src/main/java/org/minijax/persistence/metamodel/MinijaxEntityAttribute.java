package org.minijax.persistence.metamodel;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.minijax.persistence.MinijaxEntityManager;

public class MinijaxEntityAttribute<X, Y> extends MinijaxSingularAttribute<X, Y> {
    private MinijaxEntityType<Y> entityType;

    MinijaxEntityAttribute(final MinijaxAttribute.Builder<X, Y, ?> builder) {
        super(builder);
    }

    public MinijaxEntityType<Y> getEntityType() {
        if (entityType == null) {
            entityType = metamodel.entity(getJavaType());
        }
        return entityType;
    }

    @Override
    public void read(final MinijaxEntityManager em, final X instance, final ResultSet rs, final MutableInt columnIndex)
            throws ReflectiveOperationException, SQLException {
        memberWrapper.setValue(instance, getEntityType().read(em, rs, columnIndex));
    }

    @Override
    public Object write(final MinijaxEntityManager em, final X instance) {
        return getEntityType().getId().getValue(memberWrapper.getValue(instance));
    }
}
