package org.minijax.persistence.metamodel;

import java.util.Objects;

/**
 * Represents an attribute of a Java type.
 *
 * @param <X> The represented type that contains the attribute
 * @param <Y> The type of the represented attribute
 */
public abstract class MinijaxPluralAttribute<X, Y, E>
        extends MinijaxAttribute<X, Y>
        implements javax.persistence.metamodel.PluralAttribute<X, Y, E> {

    private final Class<E> elementType;

    public MinijaxPluralAttribute(
            final MinijaxMetamodel metamodel,
            final String name,
            final PersistentAttributeType persistentAttributeType,
            final MinijaxEntityType<X> declaringType,
            final MemberWrapper<X, Y> memberWrapper,
            final ColumnDefinition column,
            final Class<E> elementType) {

        super(metamodel, name, persistentAttributeType, declaringType, memberWrapper, column);
        this.elementType = Objects.requireNonNull(elementType);
    }

    @Override
    public boolean isCollection() {
        return true;
    }

    @Override
    public MinijaxEntityType<E> getElementType() {
        return getMetamodel().entity(elementType);
    }

    @Override
    public BindableType getBindableType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<E> getBindableJavaType() {
        throw new UnsupportedOperationException();
    }
}
