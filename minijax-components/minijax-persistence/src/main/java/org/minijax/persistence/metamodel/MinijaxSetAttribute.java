package org.minijax.persistence.metamodel;

import java.util.Set;

public class MinijaxSetAttribute<X, E>
        extends MinijaxPluralAttribute<X, Set<E>, E>
        implements javax.persistence.metamodel.SetAttribute<X, E> {

    public MinijaxSetAttribute(
            final MinijaxMetamodel metamodel,
            final String name,
            final PersistentAttributeType persistentAttributeType,
            final MinijaxEntityType<X> declaringType,
            final MemberWrapper<X, Set<E>> memberWrapper,
            final ColumnDefinition column,
            final Class<E> elementType) {

        super(metamodel, name, persistentAttributeType, declaringType, memberWrapper, column, elementType);
    }

    @Override
    public CollectionType getCollectionType() {
        return CollectionType.SET;
    }
}
