package org.minijax.persistence.metamodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import javax.persistence.AttributeConverter;

import org.minijax.persistence.MinijaxEntityManager;

public class MinijaxBinaryConverterAttribute<X, Y> extends MinijaxSingularAttribute<X, Y> {
    private final AttributeConverter<Y, byte[]> converter;

    @SuppressWarnings("unchecked")
    MinijaxBinaryConverterAttribute(final MinijaxAttribute.Builder<X, Y, ?> builder) {
        super(builder);
        this.converter = (AttributeConverter<Y, byte[]>) Objects.requireNonNull(builder.converter);
    }

    @Override
    public void read(final MinijaxEntityManager em, final X instance, final ResultSet rs, final MutableInt columnIndex) throws SQLException {
        memberWrapper.setValue(instance, converter.convertToEntityAttribute(rs.getBytes(columnIndex.next())));
    }

    @Override
    public byte[] write(final MinijaxEntityManager em, final X instance) {
        return converter.convertToDatabaseColumn(memberWrapper.getValue(instance));
    }
}
