package org.minijax.persistence.metamodel;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;

import org.minijax.commons.MinijaxException;

public class MinijaxAttributeFactory {

    MinijaxAttributeFactory() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public static <X, Y, E> MinijaxAttribute<X, Y> build(
            final MinijaxMetamodel metamodel,
            final MinijaxEntityType<X> declaringType,
            final Field field) {

        return build(
                metamodel,
                declaringType,
                new FieldWrapper<>(declaringType.getJavaType(), field));
    }

    @SuppressWarnings("unchecked")
    private static <X, Y, E> MinijaxAttribute<X, Y> build(
            final MinijaxMetamodel metamodel,
            final MinijaxEntityType<X> declaringType,
            final MemberWrapper<X, Y> memberWrapper) {

        final String name = buildName(memberWrapper);
        final PersistentAttributeType persistentAttributeType = getPersistentAttributeType(memberWrapper);
        final ColumnDefinition column = buildColumn(memberWrapper);
        final Class<Y> attributeType = memberWrapper.getType();

        if (attributeType == Set.class) {
            return (MinijaxAttribute<X, Y>) new MinijaxSetAttribute<X, E>(
                    metamodel,
                    name,
                    persistentAttributeType,
                    declaringType,
                    (MemberWrapper<X, Set<E>>) memberWrapper,
                    column,
                    getGenericParameterType(metamodel, memberWrapper, 0));

        } else {
            return new MinijaxSingularAttribute<>(
                    metamodel,
                    name,
                    persistentAttributeType,
                    declaringType,
                    memberWrapper,
                    column);
        }
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

    private static <X, Y> String buildName(final MemberWrapper<X, Y> field) {
        final Column column = field.getAnnotation(Column.class);
        final String name = column == null ? null : column.name();
        return name == null || name.isBlank() ? field.getName() : name;
    }

    private static <X, Y> ColumnDefinition buildColumn(final MemberWrapper<X, Y> field) {
        final Id idAnnotation = field.getAnnotation(Id.class);
        final boolean primaryKey = idAnnotation != null;

        final Class<Y> attributeType = field.getType();
        final Entity entityAnnotation = attributeType.getAnnotation(Entity.class);
        final MappedSuperclass superclassAnnotation = attributeType.getAnnotation(MappedSuperclass.class);
        final OneToMany oneToManyAnnotation = field.getAnnotation(OneToMany.class);
        final ManyToMany manyToManyAnnotation = field.getAnnotation(ManyToMany.class);

        final String name = buildName(field).toUpperCase();
        final String columnName;
        final Datatype datatype;
        final ForeignReference foreignReference;

        if (entityAnnotation != null || superclassAnnotation != null) {
            columnName = name + "_ID";
            datatype = Datatype.VARCHAR; // TODO
            foreignReference = new ForeignReference("", "", false);

        } else if (oneToManyAnnotation != null || manyToManyAnnotation != null) {
            final String associationTableName = field.getDeclaringType().getSimpleName().toUpperCase() + "_" + name;
            final String associationColumnName = name + "_ID";
            columnName = field.getDeclaringType().getSimpleName().toUpperCase() + "_ID";
            datatype = Datatype.VARCHAR; // TODO
            foreignReference = new ForeignReference(associationTableName, associationColumnName, true);

        } else {
            columnName = name;

            if (attributeType == int.class || attributeType == Integer.class) {
                datatype = Datatype.INTEGER;
            } else {
                datatype = Datatype.VARCHAR;
            }

            foreignReference = null;
        }

        return new ColumnDefinition(columnName, datatype, primaryKey, foreignReference);
    }

    @SuppressWarnings("unchecked")
    private static <X, Y, E> Class<E> getGenericParameterType(
            final MinijaxMetamodel metamodel,
            final MemberWrapper<X, Y> memberWrapper,
            final int index) {

        final java.lang.reflect.Type genericType = memberWrapper.getGenericType();
        if (!(genericType instanceof ParameterizedType)) {
            throw new MinijaxException("Generic type missing generic parameters");
        }

        return (Class<E>) ((ParameterizedType) genericType).getActualTypeArguments()[index];
    }
}
