package org.minijax.persistence.metamodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import javax.persistence.AttributeConverter;

import org.minijax.persistence.MinijaxEntityManager;

public class MinijaxStringConverterAttribute<X, Y> extends MinijaxSingularAttribute<X, Y> {
    private final AttributeConverter<Y, String> converter;

    @SuppressWarnings("unchecked")
    MinijaxStringConverterAttribute(final MinijaxAttribute.Builder<X, Y, ?> builder) {
        super(builder);
        this.converter = (AttributeConverter<Y, String>) Objects.requireNonNull(builder.converter);
    }

    @Override
    public void read(final MinijaxEntityManager em, final X instance, final ResultSet rs, final MutableInt columnIndex) throws SQLException {
        memberWrapper.setValue(instance, converter.convertToEntityAttribute(rs.getString(columnIndex.next())));
    }

    @Override
    public String write(final MinijaxEntityManager em, final X instance) {
        return converter.convertToDatabaseColumn(memberWrapper.getValue(instance));
    }
}
