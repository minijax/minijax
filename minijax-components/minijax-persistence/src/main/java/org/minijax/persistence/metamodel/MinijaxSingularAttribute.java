package org.minijax.persistence.metamodel;

import javax.persistence.metamodel.Type;

/**
 * Represents an attribute of a Java type.
 *
 * @param <X> The represented type that contains the attribute
 * @param <Y> The type of the represented attribute
 */
public abstract class MinijaxSingularAttribute<X, Y>
        extends MinijaxAttribute<X, Y>
        implements javax.persistence.metamodel.SingularAttribute<X, Y> {

    MinijaxSingularAttribute(final MinijaxAttribute.Builder<X, Y, ?> builder) {
        super(builder);
    }

    @Override
    public boolean isCollection() {
        return false;
    }

    @Override
    public BindableType getBindableType() {
        return BindableType.SINGULAR_ATTRIBUTE;
    }

    @Override
    public Class<Y> getBindableJavaType() {
        return getJavaType();
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
