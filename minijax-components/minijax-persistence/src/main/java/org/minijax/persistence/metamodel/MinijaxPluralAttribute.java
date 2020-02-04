package org.minijax.persistence.metamodel;

import java.util.Objects;

import org.minijax.persistence.wrapper.MemberWrapper;

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
    private final MemberWrapper<E, ?> elementIdWrapper;

    MinijaxPluralAttribute(final MinijaxAttribute.Builder<X, Y, E> builder) {
        super(builder);
        this.elementType = Objects.requireNonNull(builder.elementType);
        this.elementIdWrapper = Objects.requireNonNull(builder.elementIdWrapper);
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
        return BindableType.PLURAL_ATTRIBUTE;
    }

    @Override
    public Class<E> getBindableJavaType() {
        return elementType;
    }

    public MemberWrapper<?, ?> getElementIdWrapper() {
        return elementIdWrapper;
    }
}
