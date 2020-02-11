package org.minijax.persistence.metamodel;

import java.lang.reflect.Member;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceException;
import javax.persistence.metamodel.ManagedType;

import org.minijax.commons.GenericUtils;
import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.schema.Column;
import org.minijax.persistence.schema.Table;
import org.minijax.persistence.wrapper.MemberWrapper;

/**
 * Represents an attribute of a Java type.
 *
 * @param <X> The represented type that contains the attribute
 * @param <Y> The type of the represented attribute
 */
public abstract class MinijaxAttribute<X, Y> implements javax.persistence.metamodel.Attribute<X, Y> {
    protected final MinijaxMetamodel metamodel;
    protected final String name;
    protected final PersistentAttributeType persistentAttributeType;
    protected final MinijaxEntityType<X> declaringType;
    protected final MemberWrapper<X, Y> memberWrapper;
    protected final Column column;

    MinijaxAttribute(final MinijaxAttribute.Builder<X, Y, ?> builder) {
        this.metamodel = Objects.requireNonNull(builder.metamodel);
        this.name = Objects.requireNonNull(builder.name);
        this.persistentAttributeType = Objects.requireNonNull(builder.persistentAttributeType);
        this.declaringType = Objects.requireNonNull(builder.declaringType);
        this.memberWrapper = Objects.requireNonNull(builder.memberWrapper);
        this.column = builder.column;
    }

    public MinijaxMetamodel getMetamodel() {
        return metamodel;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PersistentAttributeType getPersistentAttributeType() {
        return persistentAttributeType;
    }

    @Override
    public ManagedType<X> getDeclaringType() {
        return declaringType;
    }

    @Override
    public Class<Y> getJavaType() {
        return memberWrapper.getType();
    }

    @Override
    public Member getJavaMember() {
        return memberWrapper.getMember();
    }

    @Override
    public boolean isAssociation() {
        return column.getForeignReference() != null && column.getForeignReference().isAssociation();
    }

    public Column getColumn() {
        return column;
    }

    public Y getValue(final X entity) {
        return memberWrapper.getValue(entity);
    }

    public void setValue(final X entity, final Y value) {
        memberWrapper.setValue(entity, value);
    }

    public abstract void read(MinijaxEntityManager em, X instance, ResultSet rs, MutableInt columnIndex)
            throws ReflectiveOperationException, SQLException;

    public abstract Object write(MinijaxEntityManager em, X instance);

    public static class Builder<X, Y, E> {
        final MinijaxEntityType.Builder<X> declaringTypeBuilder;
        final MemberWrapper<X, Y> memberWrapper;
        final String name;
        final PersistentAttributeType persistentAttributeType;
        final Class<Y> attributeType;
        Class<?> columnType;
        Class<E> elementType;
        Column column;
        MemberWrapper<E, ?> elementIdWrapper;
        AttributeConverter<Y, ?> converter;
        MinijaxMetamodel metamodel;
        MinijaxEntityType<X> declaringType;
        MinijaxAttribute<X, Y> result;

        public Builder(final MinijaxEntityType.Builder<X> declaringTypeBuilder, final MemberWrapper<X, Y> memberWrapper) {
            this.declaringTypeBuilder = declaringTypeBuilder;
            this.memberWrapper = memberWrapper;
            this.name = buildName(memberWrapper);
            this.persistentAttributeType = getPersistentAttributeType(memberWrapper);
            this.attributeType = memberWrapper.getType();
        }

        public MemberWrapper<X, Y> getMemberWrapper() {
            return memberWrapper;
        }

        public Column getColumn() {
            return column;
        }

        private static <X, Y> String buildName(final MemberWrapper<X, Y> field) {
            final javax.persistence.Column columnAnnotation = field.getAnnotation(javax.persistence.Column.class);
            final String name = columnAnnotation == null ? null : columnAnnotation.name();
            return name == null || name.isEmpty() ? field.getName() : name;
        }

        private static <X, Y> PersistentAttributeType getPersistentAttributeType(final MemberWrapper<X, Y> member) {
            if (member.getAnnotation(ElementCollection.class) != null) {
                return PersistentAttributeType.ELEMENT_COLLECTION;
            }

            if (member.getAnnotation(Embedded.class) != null) {
                return PersistentAttributeType.EMBEDDED;
            }

            if (member.getAnnotation(ManyToOne.class) != null) {
                return PersistentAttributeType.MANY_TO_ONE;
            }

            if (member.getAnnotation(ManyToMany.class) != null) {
                return PersistentAttributeType.MANY_TO_MANY;
            }

            if (member.getAnnotation(OneToMany.class) != null) {
                return PersistentAttributeType.ONE_TO_MANY;
            }

            if (member.getAnnotation(OneToOne.class) != null) {
                return PersistentAttributeType.ONE_TO_ONE;
            }

            return PersistentAttributeType.BASIC;
        }

        @SuppressWarnings("unchecked")
        public void buildColumn(final MinijaxMetamodel.Builder metamodelBuilder) {
            final Convert convertAnnotation = memberWrapper.getAnnotation(Convert.class);
            if (convertAnnotation != null) {
                try {
                    converter = (AttributeConverter<Y, ?>) convertAnnotation.converter().getDeclaredConstructor().newInstance();
                    columnType = GenericUtils.getGenericParameterType(convertAnnotation.converter(), AttributeConverter.class, 1);
                } catch (final ReflectiveOperationException | SecurityException ex) {
                    throw new PersistenceException(ex.getMessage(), ex);
                }
            } else {
                columnType = attributeType;
            }

            if (columnType == Set.class || columnType == List.class) {
                elementType = GenericUtils.getGenericParameterType(memberWrapper.getGenericType(), 0);
            }

            if (elementType != null) {
                elementIdWrapper = metamodelBuilder.getIdMapper(elementType);
            }

            final Id idAnnotation = memberWrapper.getAnnotation(Id.class);
            final Entity entityAnnotation = attributeType.getAnnotation(Entity.class);
            final MappedSuperclass superclassAnnotation = attributeType.getAnnotation(MappedSuperclass.class);
            final OneToMany oneToManyAnnotation = memberWrapper.getAnnotation(OneToMany.class);
            final ManyToMany manyToManyAnnotation = memberWrapper.getAnnotation(ManyToMany.class);
            final String attributeName = name.toUpperCase();
            final Table table = declaringTypeBuilder.getTable();

            final String columnName;
            final Datatype datatype;
            final ForeignReference foreignReference;
            final Table joinTable;

            if (entityAnnotation != null || superclassAnnotation != null) {
                columnName = attributeName + "_ID";
                datatype = getDatatype(metamodelBuilder.getIdMapper(attributeType).getType());
                foreignReference = new ForeignReference("", "", false);
                joinTable = null;

            } else if (oneToManyAnnotation != null || manyToManyAnnotation != null) {
                final String associationTableName = memberWrapper.getDeclaringType().getSimpleName().toUpperCase() + "_" + attributeName;
                final String associationColumnName = attributeName + "_ID";
                columnName = memberWrapper.getDeclaringType().getSimpleName().toUpperCase() + "_ID";
                datatype = getDatatype(elementIdWrapper.getType());
                foreignReference = new ForeignReference(associationTableName, associationColumnName, true);
                joinTable = new Table(table.getSchema(), associationTableName);
                joinTable.getColumns().add(new Column(joinTable, columnName, datatype));
                joinTable.getColumns().add(new Column(joinTable, associationColumnName, datatype));

            } else {
                columnName = attributeName;
                datatype = getDatatype(attributeType);
                foreignReference = null;
                joinTable = null;
            }

            column = new Column(table, columnName, datatype);
            column.setPrimaryKey(idAnnotation != null);
            column.setForeignReference(foreignReference);
            column.setJoinTable(joinTable);
        }

        private static Datatype getDatatype(final Class<?> attributeType) {
            if (attributeType == int.class || attributeType == Integer.class) {
                return Datatype.INTEGER;
            } else if (attributeType == UUID.class) {
                return Datatype.BINARY;
            } else if (attributeType == Timestamp.class || attributeType == Instant.class) {
                return Datatype.TIMESTAMP;
            } else {
                return Datatype.VARCHAR;
            }
        }

        @SuppressWarnings("unchecked")
        public MinijaxAttribute<X, Y> build(final MinijaxMetamodel metamodel, final MinijaxEntityType<X> declaringType) {
            if (result != null) {
                return result;
            }

            this.metamodel = metamodel;
            this.declaringType = declaringType;

            final Convert convertAnnotation = memberWrapper.getAnnotation(Convert.class);
            final Class<?> columnType;
            if (convertAnnotation != null) {
                try {
                    converter = (AttributeConverter<Y, ?>) convertAnnotation.converter().getDeclaredConstructor().newInstance();
                    columnType = GenericUtils.getGenericParameterType(convertAnnotation.converter(), AttributeConverter.class, 1);
                } catch (final ReflectiveOperationException | SecurityException ex) {
                    throw new PersistenceException(ex.getMessage(), ex);
                }
            } else {
                converter = null;
                columnType = attributeType;
            }

            final Embedded embeddedAnnotation = memberWrapper.getAnnotation(Embedded.class);
            if (embeddedAnnotation != null) {
                result = new MinijaxEmbeddedAttribute<X, Y>(this);

            } else if (columnType == Set.class) {
                result = (MinijaxAttribute<X, Y>) new MinijaxSetAttribute<X, E>((Builder<X, Set<E>, E>) this);

            } else if (columnType == List.class) {
                result = (MinijaxAttribute<X, Y>) new MinijaxListAttribute<X, E>((Builder<X, List<E>, E>) this);

            } else if (persistentAttributeType == PersistentAttributeType.MANY_TO_ONE ||
                    persistentAttributeType == PersistentAttributeType.ONE_TO_ONE) {
                result = new MinijaxEntityAttribute<X, Y>(this);

            } else if (columnType == int.class || columnType == Integer.class) {
                if (converter != null) {
                    result = new MinijaxIntegerConverterAttribute<X, Y>(this);
                } else {
                    result = (MinijaxAttribute<X, Y>) new MinijaxIntegerAttribute<X>((Builder<X, Integer, ?>) this);
                }

            } else if (columnType == String.class) {
                if (converter != null) {
                    result = new MinijaxStringConverterAttribute<X, Y>(this);
                } else {
                    result = (MinijaxAttribute<X, Y>) new MinijaxStringAttribute<X>((Builder<X, String, ?>) this);
                }

            } else if (columnType == byte[].class) {
                if (converter != null) {
                    result = new MinijaxBinaryConverterAttribute<X, Y>(this);
                } else {
                    result = (MinijaxAttribute<X, Y>) new MinijaxBinaryAttribute<X>((Builder<X, byte[], ?>) this);
                }

            } else if (columnType == Timestamp.class) {
                if (converter != null) {
                    result = new MinijaxTimestampConverterAttribute<X, Y>(this);
                } else {
                    result = (MinijaxAttribute<X, Y>) new MinijaxTimestampAttribute<X>((Builder<X, Timestamp, ?>) this);
                }

            } else {
                throw new PersistenceException("Unimplemented attribute type: " + columnType);
            }

            return result;
        }
    }
}
