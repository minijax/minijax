package org.minijax.persistence.metamodel;

import java.lang.reflect.Member;
import java.util.Objects;

import javax.persistence.metamodel.ManagedType;

/**
 * Represents an attribute of a Java type.
 *
 * @param <X> The represented type that contains the attribute
 * @param <Y> The type of the represented attribute
 */
public abstract class MinijaxAttribute<X, Y> implements javax.persistence.metamodel.Attribute<X, Y> {
    private final MinijaxMetamodel metamodel;
    private final String name;
    private final PersistentAttributeType persistentAttributeType;
    private final MinijaxEntityType<X> declaringType;
    private final MemberWrapper<X, Y> memberWrapper;
    private final ColumnDefinition column;

    public MinijaxAttribute(
            final MinijaxMetamodel metamodel,
            final String name,
            final PersistentAttributeType persistentAttributeType,
            final MinijaxEntityType<X> declaringType,
            final MemberWrapper<X, Y> memberWrapper,
            final ColumnDefinition column) {

        this.metamodel = Objects.requireNonNull(metamodel);
        this.name = Objects.requireNonNull(name);
        this.persistentAttributeType = Objects.requireNonNull(persistentAttributeType);
        this.declaringType = Objects.requireNonNull(declaringType);
        this.memberWrapper = Objects.requireNonNull(memberWrapper);
        this.column = Objects.requireNonNull(column);
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

    public ColumnDefinition getColumn() {
        return column;
    }

    public MinijaxSingularAttribute<? super Y, ?> getReferenceId() {
        return metamodel.entity(getJavaType()).getIdAttribute();
    }

    public Y getValue(final X entity) {
        return memberWrapper.getValue(entity);
    }

    public void setValue(final X entity, final Y value) {
        memberWrapper.setValue(entity, value);
    }
}
