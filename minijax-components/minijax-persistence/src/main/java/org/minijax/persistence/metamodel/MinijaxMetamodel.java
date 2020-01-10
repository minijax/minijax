package org.minijax.persistence.metamodel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;

public class MinijaxMetamodel implements javax.persistence.metamodel.Metamodel {
    private final Map<Class<?>, MinijaxEntityType<?>> entityTypes;
    private final Map<Class<?>, MinijaxEntityType<?>> managedTypes;
    private final Map<Class<?>, MinijaxEntityType<?>> embeddableTypes;

    public MinijaxMetamodel() {
        entityTypes = new HashMap<>();
        managedTypes = new HashMap<>();
        embeddableTypes = new HashMap<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X> MinijaxEntityType<X> entity(final Class<X> cls) {
        MinijaxEntityType<X> result = (MinijaxEntityType<X>) entityTypes.get(cls);
        if (result == null) {
            result = new MinijaxEntityType<>(this, cls);
            entityTypes.put(cls, result);
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X> MinijaxEntityType<X> managedType(final Class<X> cls) {
        return (MinijaxEntityType<X>) managedTypes.get(cls);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X> MinijaxEntityType<X> embeddable(final Class<X> cls) {
        return (MinijaxEntityType<X>) embeddableTypes.get(cls);
    }

    @Override
    public Set<EntityType<?>> getEntities() {
        return new HashSet<>(entityTypes.values());
    }

    @Override
    public Set<ManagedType<?>> getManagedTypes() {
        return new HashSet<>(managedTypes.values());
    }

    @Override
    public Set<EmbeddableType<?>> getEmbeddables() {
        return new HashSet<>(embeddableTypes.values());
    }
}
