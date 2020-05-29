package org.minijax.persistence.metamodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import jakarta.persistence.AttributeConverter;

import org.minijax.persistence.MinijaxEntityManager;

public class MinijaxIntegerConverterAttribute<X, Y> extends MinijaxSingularAttribute<X, Y> {
    private final AttributeConverter<Y, Integer> converter;

    @SuppressWarnings("unchecked")
    MinijaxIntegerConverterAttribute(final MinijaxAttribute.Builder<X, Y, ?> builder) {
        super(builder);
        this.converter = (AttributeConverter<Y, Integer>) Objects.requireNonNull(builder.converter);
    }

    @Override
    public void read(final MinijaxEntityManager em, final X instance, final ResultSet rs, final MutableInt columnIndex) throws SQLException {
        memberWrapper.setValue(instance, converter.convertToEntityAttribute(rs.getInt(columnIndex.next())));
    }

    @Override
    public Integer write(final MinijaxEntityManager em, final X instance) {
        return converter.convertToDatabaseColumn(memberWrapper.getValue(instance));
    }
}
