package org.minijax.persistence.metamodel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.PersistenceException;
import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;

import org.minijax.persistence.MinijaxPersistenceUnitInfo;
import org.minijax.persistence.criteria.MinijaxCriteriaQuery;
import org.minijax.persistence.schema.Schema;
import org.minijax.persistence.wrapper.MemberWrapper;

public class MinijaxMetamodel implements javax.persistence.metamodel.Metamodel {
    private final Map<Class<?>, MinijaxEntityType<?>> entityTypes;
    private final Map<Class<?>, MinijaxEntityType<?>> embeddableTypes;
    private final Map<Class<?>, MinijaxEntityType<?>> managedTypes;
    private final Map<String, MinijaxEntityType<?>> entitiesByName;
    private final Map<String, MinijaxCriteriaQuery<?>> namedQueries;

    private MinijaxMetamodel(final Builder builder) {
        entityTypes = new HashMap<>();
        embeddableTypes = new HashMap<>();
        managedTypes = new HashMap<>();
        entitiesByName = new HashMap<>();
        namedQueries = new HashMap<>();

        for (final MinijaxEntityType.Builder<?> entityTypeBuilder : builder.entityClasses.values()) {
            final MinijaxEntityType<?> entityType = entityTypeBuilder.build(this);
            entityTypes.put(entityType.getJavaType(), entityType);
            managedTypes.put(entityType.getJavaType(), entityType);
            namedQueries.putAll(entityTypeBuilder.getNamedQueries(this));
        }

        for (final MinijaxEntityType.Builder<?> embeddedTypeBuilder : builder.embeddedClasses.values()) {
            final MinijaxEntityType<?> embeddedType = embeddedTypeBuilder.build(this);
            embeddableTypes.put(embeddedType.getJavaType(), embeddedType);
            managedTypes.put(embeddedType.getJavaType(), embeddedType);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X> MinijaxEntityType<X> entity(final Class<X> cls) {
        return (MinijaxEntityType<X>) entityTypes.get(cls);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X> MinijaxEntityType<X> embeddable(final Class<X> cls) {
        return (MinijaxEntityType<X>) embeddableTypes.get(cls);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X> MinijaxEntityType<X> managedType(final Class<X> cls) {
        return (MinijaxEntityType<X>) managedTypes.get(cls);
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

    @SuppressWarnings("unchecked")
    public <X> MinijaxEntityType<X> getEntityTypeByName(final String name) {
        return (MinijaxEntityType<X>) entitiesByName.get(name);
    }

    @SuppressWarnings("unchecked")
    public <X> MinijaxCriteriaQuery<X> getNamedQuery(final String name) {
        return (MinijaxCriteriaQuery<X>) namedQueries.get(name);
    }

    public static class Builder {
        private final MinijaxPersistenceUnitInfo unitInfo;
        private final Schema schema;
        private final Map<Class<?>, MinijaxEntityType.Builder<?>> entityClasses;
        private final Map<Class<?>, MinijaxEntityType.Builder<?>> mappedSuperclasses;
        private final Map<Class<?>, MinijaxEntityType.Builder<?>> embeddedClasses;

        public Builder(final MinijaxPersistenceUnitInfo unitInfo) {
            this.unitInfo = unitInfo;
            schema = new Schema(unitInfo.getPersistenceUnitName());
            entityClasses = new HashMap<>();
            mappedSuperclasses = new HashMap<>();
            embeddedClasses = new HashMap<>();
        }

        public MinijaxPersistenceUnitInfo getUnitInfo() {
            return unitInfo;
        }

        public Schema getSchema() {
            return schema;
        }

        @SuppressWarnings("unchecked")
        public <X, Y> MemberWrapper<X, Y> getIdMapper(final Class<X> cls) {
            final MinijaxEntityType.Builder<X> entityTypeBuilder = (MinijaxEntityType.Builder<X>) entityClasses.get(cls);
            if (entityTypeBuilder == null) {
                throw new PersistenceException("Entity class not found: " + cls);
            }
            return (MemberWrapper<X, Y>) entityTypeBuilder.getIdWrapper();
        }

        void buildClassList() {
            for (final String className : unitInfo.getManagedClassNames()) {
                buildClassList(className);
            }
        }

        void buildClassList(final String className) {
            final Class<?> cls;
            try {
                cls = Class.forName(className);
            } catch (final ClassNotFoundException ex) {
                throw new PersistenceException(ex.getMessage(), ex);
            }

            if (cls.getAnnotation(Entity.class) != null) {
                entityClasses.put(cls, new MinijaxEntityType.Builder<>(this, cls));
            }

            if (cls.getAnnotation(MappedSuperclass.class) != null) {
                mappedSuperclasses.put(cls, new MinijaxEntityType.Builder<>(this, cls));
            }

            if (cls.getAnnotation(Embeddable.class) != null) {
                embeddedClasses.put(cls, new MinijaxEntityType.Builder<>(this, cls));
            }
        }

        public MinijaxMetamodel build() {
            buildClassList();
            embeddedClasses.forEach((k, e) -> e.buildAttributes());
            entityClasses.forEach((k, e) -> e.buildAttributes());
            entityClasses.forEach((k, e) -> e.buildId(this));
            entityClasses.forEach((k, e) -> e.buildColumns(this));
            entityClasses.forEach((k, e) -> e.buildTable());
            return new MinijaxMetamodel(this);
        }
    }
}
