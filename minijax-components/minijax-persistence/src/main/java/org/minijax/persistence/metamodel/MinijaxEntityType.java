package org.minijax.persistence.metamodel;

import static java.util.stream.Collectors.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Transient;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.CollectionAttribute;
import jakarta.persistence.metamodel.IdentifiableType;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.MapAttribute;
import jakarta.persistence.metamodel.PluralAttribute;
import jakarta.persistence.metamodel.PluralAttribute.CollectionType;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.Type;

import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.MinijaxNativeQuery;
import org.minijax.persistence.criteria.MinijaxCriteriaBuilder;
import org.minijax.persistence.criteria.MinijaxCriteriaQuery;
import org.minijax.persistence.jpql.Parser;
import org.minijax.persistence.jpql.Tokenizer;
import org.minijax.persistence.lazy.LazyList;
import org.minijax.persistence.lazy.LazySet;
import org.minijax.persistence.schema.Column;
import org.minijax.persistence.schema.Table;
import org.minijax.persistence.wrapper.FieldWrapper;
import org.minijax.persistence.wrapper.MemberWrapper;

public class MinijaxEntityType<T>
        implements jakarta.persistence.metamodel.EntityType<T>,
                jakarta.persistence.metamodel.ManagedType<T>,
                jakarta.persistence.metamodel.EmbeddableType<T> {

    private final Class<T> javaType;
    private final String name;
    private final Table table;
    private final List<Table> joinTables;
    private final MinijaxSingularAttribute<? super T, ?> idAttribute;
    private final List<MinijaxAttribute<? super T, ?>> attributes;
    private final Set<MinijaxAttribute<? super T, ?>> attributeSet;
    private final Map<String, MinijaxAttribute<T, ?>> attributeLookup;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private MinijaxEntityType(final MinijaxMetamodel metamodel, final Builder<T> builder) {
        Objects.requireNonNull(builder);
        this.javaType = Objects.requireNonNull(builder.javaType);
        this.name = Objects.requireNonNull(builder.name);
        this.table = Objects.requireNonNull(builder.table);
        this.joinTables = Collections.unmodifiableList(builder.joinTables);
        this.attributes = builder.attributes.stream().map(a -> a.build(metamodel, this)).collect(toList());
        this.attributeSet = attributes.stream().collect(toSet());
        this.attributeLookup = (Map) attributes.stream().collect(toMap(a -> a.getName().toUpperCase(), a -> a));

        if (builder.idAttribute != null) {
            this.idAttribute = (MinijaxSingularAttribute<? super T, ?>) builder.idAttribute.build(metamodel, this);
        } else {
            this.idAttribute = null;
        }
    }

    @Override
    public Class<T> getJavaType() {
        return javaType;
    }

    @Override
    public String getName() {
        return name;
    }

    public Table getTable() {
        return table;
    }

    public List<Table> getJoinTables() {
        return joinTables;
    }

    public MinijaxSingularAttribute<? super T, ?> getId() {
        return idAttribute;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Y> MinijaxSingularAttribute<? super T, Y> getId(final Class<Y> type) {
        return (MinijaxSingularAttribute<? super T, Y>) idAttribute;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Y> MinijaxSingularAttribute<T, Y> getDeclaredId(final Class<Y> type) {
        return (MinijaxSingularAttribute<T, Y>) idAttribute;
    }

    @Override
    public Type<?> getIdType() {
        return null;
    }

    public List<MinijaxAttribute<? super T, ?>> getAttributesList() {
        return attributes;
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Set<Attribute<? super T, ?>> getAttributes() {
        return (Set) attributeSet;
    }

    @Override
    public MinijaxAttribute<? super T, ?> getAttribute(final String name) {
        return attributeLookup.get(name.toUpperCase());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public T read(final MinijaxEntityManager em, final ResultSet rs, final MutableInt columnIndex)
            throws ReflectiveOperationException, SQLException {

        final T instance = javaType.getConstructor().newInstance();
        for (final MinijaxAttribute<? super T, ?> attr : attributes) {
            if (attr instanceof MinijaxPluralAttribute) {
                continue;
            }
            attr.read(em, instance, rs, columnIndex);
        }
        for (final MinijaxAttribute<? super T, ?> attr : attributes) {
            if (attr instanceof MinijaxPluralAttribute) {
                final MinijaxPluralAttribute<? super T, ?, ?> joinTableMapper = (MinijaxPluralAttribute<? super T, ?, ?>) attr;
                final CollectionType collectionType = joinTableMapper.getCollectionType();
                final Object value;
                if (collectionType == CollectionType.SET) {
                    value = buildLazySet(em, instance, joinTableMapper);
                } else if (collectionType == CollectionType.LIST) {
                    value = buildLazyList(em, instance, joinTableMapper);
                } else {
                    throw new PersistenceException("Unsupported lazy collection type: " + collectionType);
                }
                ((MinijaxAttribute) attr).setValue(instance, value);
            }
        }
        return instance;
    }

    /*
     * Private helpers
     */

    private <T2> LazySet<T2> buildLazySet(final MinijaxEntityManager em, final T instance, final MinijaxPluralAttribute<? super T, T2, ?> attr) {
        return new LazySet<>(buildLazyQuery(em, instance, attr));
    }

    private <T2> LazyList<T2> buildLazyList(final MinijaxEntityManager em, final T instance, final MinijaxPluralAttribute<? super T, T2, ?> attr) {
        return new LazyList<>(buildLazyQuery(em, instance, attr));
    }

    /**
     * TODO: Move this method into AnsiSqlDialect or AnsiSqlBuilder.
     */
    @SuppressWarnings("unchecked")
    private <T2> MinijaxNativeQuery<T2> buildLazyQuery(
            final MinijaxEntityManager em,
            final T instance,
            final MinijaxPluralAttribute<? super T, T2, ?> attr) {

        final MinijaxEntityType<T2> elementType = (MinijaxEntityType<T2>) attr.getElementType();
        final StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        boolean first = true;

        for (final MinijaxAttribute<? super T2, ?> attr2 : elementType.getAttributesList()) {
            if (attr2 instanceof MinijaxPluralAttribute) {
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
        sql.append(attr.getColumn().getJoinTable().getName());
        sql.append(" jt");
        sql.append(" LEFT JOIN ");
        sql.append(elementType.getTable().getName());
        sql.append(" t0 ON jt.");
        sql.append(attr.getColumn().getForeignReference().getColumnName());
        sql.append("=t0.");
        sql.append(elementType.getId().getColumn().getName());
        sql.append(" WHERE jt.");
        sql.append(attr.getColumn().getName());
        sql.append("=?");

        return new MinijaxNativeQuery<>(
                em,
                elementType.getJavaType(),
                sql.toString(),
                idAttribute.getValue(instance));
    }

    /*
     * Unsupported
     */

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

    public static class Builder<T> {
        private final Class<T> javaType;
        private final List<Class<?>> typeList;
        private final List<Field> fields;
        private final String name;
        private final Table table;
        private final List<Table> joinTables;
        private MinijaxAttribute.Builder<T, ?, ?> idAttribute;
        private final List<MinijaxAttribute.Builder<T, ?, ?>> attributes;
        private MinijaxEntityType<T> result;

        public Builder(final MinijaxMetamodel.Builder metamodelBuilder, final Class<T> javaType) {
            this.javaType = javaType;
            this.typeList = getTypeList();
            this.fields = getFields();
            this.attributes = new ArrayList<>();

            final Entity entityAnnotation = javaType.getAnnotation(Entity.class);
            final String annotationName = entityAnnotation != null ? entityAnnotation.name() : null;
            this.name = annotationName != null && !annotationName.isEmpty() ? annotationName : javaType.getSimpleName();

            final jakarta.persistence.Table tableAnnotation = javaType.getAnnotation(jakarta.persistence.Table.class);
            final String annotationTableName = tableAnnotation != null ? tableAnnotation.name() : null;
            final String tableName = annotationTableName != null && !annotationTableName.isEmpty() ? annotationTableName : this.name.toUpperCase();
            this.table = new Table(metamodelBuilder.getSchema(), tableName);
            this.joinTables = new ArrayList<>();
        }

        public Table getTable() {
            return table;
        }

        public List<Class<?>> getTypeList() {
            final List<Class<?>> types = new ArrayList<>();

            Class<?> current = javaType;
            while (current != Object.class) {
                types.add(current);
                current = current.getSuperclass();
            }

            Collections.reverse(types);
            return types;
        }

        public List<Field> getFields() {
            final List<Field> fields = new ArrayList<>();
            for (final Class<?> type : typeList) {
                buildFields(fields, type);
            }
            return fields;
        }

        public void buildFields(final List<Field> fields, final Class<?> type) {
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

        @SuppressWarnings({ "rawtypes", "unchecked" })
        public void buildAttributes() {
            for (final Field field : fields) {
                final FieldWrapper<?, ?> fieldWrapper = new FieldWrapper<>(javaType, field);
                final MinijaxAttribute.Builder attribute = new MinijaxAttribute.Builder(this, fieldWrapper);
                attributes.add(attribute);
                if (field.getAnnotation(Id.class) != null) {
                    idAttribute = attribute;
                }
            }
        }

        public void buildId(final MinijaxMetamodel.Builder metamodelBuilder) {
            idAttribute.buildColumn(metamodelBuilder);
        }

        public void buildColumns(final MinijaxMetamodel.Builder metamodelBuilder) {
            for (final MinijaxAttribute.Builder<T, ?, ?> attrBuilder : attributes) {
                if (attrBuilder == idAttribute) {
                    // Should already be built
                    continue;
                }
                attrBuilder.buildColumn(metamodelBuilder);
            }
        }

        public void buildTable() {
            for (final MinijaxAttribute.Builder<T, ?, ?> attr : attributes) {
                final Column column = attr.getColumn();
                if (column.getJoinTable() != null) {
                    joinTables.add(column.getJoinTable());
                } else {
                    table.getColumns().add(column);
                }
            }
        }

        public MemberWrapper<T, ?> getIdWrapper() {
            return idAttribute.getMemberWrapper();
        }

        public Map<String, MinijaxCriteriaQuery<T>> getNamedQueries(final MinijaxMetamodel metamodel) {
            final MinijaxCriteriaBuilder cb = new MinijaxCriteriaBuilder(metamodel);
            final Map<String, MinijaxCriteriaQuery<T>> result = new HashMap<>();
            final NamedQuery[] annotations = javaType.getAnnotationsByType(NamedQuery.class);
            for (final NamedQuery annotation : annotations) {
                final MinijaxCriteriaQuery<T> query = Parser.parse(cb, javaType, Tokenizer.tokenize(annotation.query()));
                result.put(annotation.name(), query);
            }
            return result;
        }

        public MinijaxEntityType<T> build(final MinijaxMetamodel metamodel) {
            if (result == null) {
                result = new MinijaxEntityType<>(metamodel, this);
            }
            return result;
        }
    }
}
