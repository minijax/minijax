package org.minijax.persistence.metamodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.AttributeConverter;

import org.minijax.persistence.MinijaxEntityManager;

public class MinijaxTimestampConverterAttribute<X, Y> extends MinijaxSingularAttribute<X, Y> {
    private final AttributeConverter<Y, Timestamp> converter;

    @SuppressWarnings("unchecked")
    MinijaxTimestampConverterAttribute(final MinijaxAttribute.Builder<X, Y, ?> builder) {
        super(builder);
        this.converter = (AttributeConverter<Y, Timestamp>) Objects.requireNonNull(builder.converter);
    }

    @Override
    public void read(final MinijaxEntityManager em, final X instance, final ResultSet rs, final MutableInt columnIndex) throws SQLException {
        memberWrapper.setValue(instance, converter.convertToEntityAttribute(rs.getTimestamp(columnIndex.next())));
    }

    @Override
    public Timestamp write(final MinijaxEntityManager em, final X instance) {
        return converter.convertToDatabaseColumn(memberWrapper.getValue(instance));
    }
}
