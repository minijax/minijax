package org.minijax.persistence.metamodel;

import javax.persistence.metamodel.Type;

/**
 * Represents an attribute of a Java type.
 *
 * @param <X> The represented type that contains the attribute
 * @param <Y> The type of the represented attribute
 */
public class MinijaxSingularAttribute<X, Y>
        extends MinijaxAttribute<X, Y>
        implements javax.persistence.metamodel.SingularAttribute<X, Y> {

    public MinijaxSingularAttribute(
            final MinijaxMetamodel metamodel,
            final String name,
            final PersistentAttributeType persistentAttributeType,
            final MinijaxEntityType<X> declaringType,
            final MemberWrapper<X, Y> field,
            final ColumnDefinition column) {

        super(metamodel, name, persistentAttributeType, declaringType, field, column);
    }

    @Override
    public boolean isCollection() {
        return false;
    }

    @Override
    public BindableType getBindableType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<Y> getBindableJavaType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isId() {
        return getColumn().isPrimaryKey();
    }

    @Override
    public boolean isVersion() {
        return false;
    }

    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public Type<Y> getType() {
        return getMetamodel().managedType(getJavaType());
    }
}
