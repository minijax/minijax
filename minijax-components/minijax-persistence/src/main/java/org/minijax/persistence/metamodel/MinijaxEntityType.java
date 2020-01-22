package org.minijax.persistence.metamodel;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.IdentifiableType;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type;

import org.minijax.commons.MinijaxException;
import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.MinijaxNativeQuery;
import org.minijax.persistence.lazy.LazySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinijaxEntityType<T>
        implements javax.persistence.metamodel.EntityType<T>,
                javax.persistence.metamodel.ManagedType<T>,
                javax.persistence.metamodel.EmbeddableType<T> {

    private static final Logger LOG = LoggerFactory.getLogger(MinijaxEntityType.class);
    private final MinijaxMetamodel metamodel;
    private final Class<T> javaType;
    private final Entity entityAnnotation;
    private final String name;
    private final String tableName;
    private final MinijaxSingularAttribute<? super T, ?> idAttribute;
    private final LinkedHashSet<MinijaxAttribute<? super T, ?>> attributes;
    private final Map<String, MinijaxAttribute<T, ?>> attributeLookup;

    public MinijaxEntityType(final MinijaxMetamodel metamodel, final Class<T> javaType) {
        this.metamodel = metamodel;
        this.javaType = javaType;

        this.entityAnnotation = javaType.getAnnotation(Entity.class);
        if (entityAnnotation == null) {
            throw new MinijaxException("Class \"" + javaType + "\" is not an Entity");
        }

        final String annotationName = entityAnnotation.name();
        this.name = annotationName != null && !annotationName.isBlank() ? annotationName : javaType.getSimpleName();
        this.tableName = name.toUpperCase();

        final List<Field> fields = new ArrayList<>();
        for (final Class<?> cls : getTypeList(javaType)) {
            buildFields(fields, cls);
        }

        MinijaxSingularAttribute<T, ?> idAttr = null;
        attributes = new LinkedHashSet<>();
        attributeLookup = new HashMap<>();
        for (final Field field : fields) {
            final MinijaxAttribute<T, Object> attribute = MinijaxAttributeFactory.build(metamodel, this, field);
            if (attribute instanceof MinijaxSingularAttribute && ((MinijaxSingularAttribute<T, ?>) attribute).isId()) {
                idAttr = (MinijaxSingularAttribute<T, ?>) attribute;
            }
            attributes.add(attribute);
            attributeLookup.put(attribute.getName().toUpperCase(), attribute);
        }

        this.idAttribute = idAttr;
    }

    @Override
    public Class<T> getJavaType() {
        return javaType;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getTableName() {
        return tableName;
    }

    public MinijaxSingularAttribute<? super T, ?> getIdAttribute() {
        return idAttribute;
    }

    @Override
    public Type<?> getIdType() {
        if (idAttribute == null) {
            return null;
        }
        return metamodel.entity(idAttribute.getJavaType());
    }

    public LinkedHashSet<MinijaxAttribute<? super T, ?>> getAttributesImpl() {
        return attributes;
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Set<Attribute<? super T, ?>> getAttributes() {
        return (Set) attributes;
    }

    @Override
    public MinijaxAttribute<? super T, ?> getAttribute(final String name) {
        return attributeLookup.get(name.toUpperCase());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public T createInstanceFromRow(final MinijaxEntityManager em, final ResultSet rs, final MutableInt columnIndex) {
        try {
            final T instance = javaType.getConstructor().newInstance();
            for (final MinijaxAttribute attr : attributes) {
                final Class<?> attrType = attr.getJavaType();
                final Object value;
                if (attr.getPersistentAttributeType() == PersistentAttributeType.MANY_TO_ONE ||
                        attr.getPersistentAttributeType() == PersistentAttributeType.ONE_TO_ONE) {
                  final Class<?> attributeType = attr.getJavaType();
                  final MinijaxEntityType<?> attributeEntityType = metamodel.entity(attributeType);
                    value = attributeEntityType.createInstanceFromRow(em, rs, columnIndex);
                } else if (attrType == int.class || attrType == Integer.class) {
                    value = rs.getInt(columnIndex.getValue());
                    columnIndex.setValue(columnIndex.getValue() + 1);
                } else if (attrType == UUID.class) {
                    final String strValue = rs.getString(columnIndex.getValue());
                    value = strValue == null ? null : UUID.fromString(strValue);
                    columnIndex.setValue(columnIndex.getValue() + 1);
                } else if (attrType == String.class) {
                    value = rs.getString(columnIndex.getValue());
                    columnIndex.setValue(columnIndex.getValue() + 1);
                } else if (attrType == List.class) {
                    value = new ArrayList<>(); // TODO
                    columnIndex.setValue(columnIndex.getValue() + 1);
                } else if (attrType == Set.class) {
                    value = buildLazySet(em, instance, (MinijaxSetAttribute<T, ?>) attr);
                } else {
                    LOG.warn("Unimplemented attribute type: {}", attrType);
                    value = null;
                    columnIndex.setValue(columnIndex.getValue() + 1);
                }
                attr.setValue(instance, value);
            }

            return instance;

        } catch (final ReflectiveOperationException | SQLException ex) {
            throw new MinijaxException(ex.getMessage(), ex);
        }
    }

    /*
     * Private helpers
     */

    private static List<Class<?>> getTypeList(final Class<?> type) {
        final List<Class<?>> types = new ArrayList<>();

        Class<?> current = type;
        while (current != Object.class) {
            types.add(current);
            current = current.getSuperclass();
        }

        Collections.reverse(types);
        return types;
    }

    private void buildFields(final List<Field> fields, final Class<?> type) {
        for (final Field field : type.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (field.getAnnotation(Transient.class) != null) {
                continue;
            }
            field.setAccessible(true);
            fields.add(field);
        }
    }

    private <T2> LazySet<T2> buildLazySet(final MinijaxEntityManager em, final T instance, final MinijaxSetAttribute<T, T2> attr) {
        final MinijaxEntityType<T2> elementType = attr.getElementType();
        final StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        boolean first = true;
        for (final MinijaxAttribute<?, ?> attr2 : elementType.getAttributesImpl()) {
            if (attr2.isAssociation()) {
                continue;
            }
            if (!first) {
                sql.append(", ");
            }
            sql.append("t0.");
            sql.append(attr2.getColumn().getName());
            first = false;
        }
        sql.append(" FROM ");
        sql.append(attr.getColumn().getForeignReference().getTableName());
        sql.append(" jt");
        sql.append(" LEFT JOIN ");
        sql.append(elementType.getTableName());
        sql.append(" t0 ON jt.");
        sql.append(attr.getColumn().getForeignReference().getColumnName());
        sql.append("=t0.");
        sql.append(elementType.getIdAttribute().getColumn().getName());
        sql.append(" WHERE jt.");
        sql.append(attr.getColumn().getName());
        sql.append("=?");

        return new LazySet<>(new MinijaxNativeQuery<>(
                em,
                elementType.getJavaType(),
                sql.toString(),
                idAttribute.getValue(instance)));
    }

    /*
     * Unsupported
     */

    @Override
    public <Y> SingularAttribute<? super T, Y> getId(final Class<Y> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> SingularAttribute<T, Y> getDeclaredId(final Class<Y> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> SingularAttribute<? super T, Y> getVersion(final Class<Y> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> SingularAttribute<T, Y> getDeclaredVersion(final Class<Y> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IdentifiableType<? super T> getSupertype() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasSingleIdAttribute() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasVersionAttribute() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<SingularAttribute<? super T, ?>> getIdClassAttributes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PersistenceType getPersistenceType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BindableType getBindableType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<T> getBindableJavaType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Attribute<T, ?>> getDeclaredAttributes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> SingularAttribute<? super T, Y> getSingularAttribute(final String name, final Class<Y> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <Y> SingularAttribute<T, Y> getDeclaredSingularAttribute(final String name, final Class<Y> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<SingularAttribute<? super T, ?>> getSingularAttributes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<SingularAttribute<T, ?>> getDeclaredSingularAttributes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E> CollectionAttribute<? super T, E> getCollection(final String name, final Class<E> elementType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E> CollectionAttribute<T, E> getDeclaredCollection(final String name, final Class<E> elementType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E> SetAttribute<? super T, E> getSet(final String name, final Class<E> elementType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E> SetAttribute<T, E> getDeclaredSet(final String name, final Class<E> elementType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E> ListAttribute<? super T, E> getList(final String name, final Class<E> elementType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E> ListAttribute<T, E> getDeclaredList(final String name, final Class<E> elementType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, V> MapAttribute<? super T, K, V> getMap(final String name, final Class<K> keyType, final Class<V> valueType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, V> MapAttribute<T, K, V> getDeclaredMap(final String name, final Class<K> keyType, final Class<V> valueType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<PluralAttribute<? super T, ?, ?>> getPluralAttributes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<PluralAttribute<T, ?, ?>> getDeclaredPluralAttributes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Attribute<T, ?> getDeclaredAttribute(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SingularAttribute<? super T, ?> getSingularAttribute(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SingularAttribute<T, ?> getDeclaredSingularAttribute(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CollectionAttribute<? super T, ?> getCollection(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CollectionAttribute<T, ?> getDeclaredCollection(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SetAttribute<? super T, ?> getSet(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SetAttribute<T, ?> getDeclaredSet(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListAttribute<? super T, ?> getList(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListAttribute<T, ?> getDeclaredList(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MapAttribute<? super T, ?, ?> getMap(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MapAttribute<T, ?, ?> getDeclaredMap(final String name) {
        throw new UnsupportedOperationException();
    }

    public static class MutableInt {
        private int value;

        public MutableInt(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(final int value) {
            this.value = value;
        }
    }
}
