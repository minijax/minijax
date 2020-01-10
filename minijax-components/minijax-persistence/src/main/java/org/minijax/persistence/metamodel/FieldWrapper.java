package org.minijax.persistence.metamodel;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Type;

import org.minijax.commons.MinijaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FieldWrapper<X, Y> implements MemberWrapper<X, Y> {
    private static final Logger LOG = LoggerFactory.getLogger(FieldWrapper.class);
    private final Class<X> declaringType;
    private final Field field;

    public FieldWrapper(final Class<X> declaringType, final Field field) {
        this.declaringType = declaringType;
        this.field = field;
        field.setAccessible(true);
    }

    @Override
    public Class<X> getDeclaringType() {
        return declaringType;
    }

    @Override
    public Member getMember() {
        return field;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<Y> getType() {
        return (Class<Y>) field.getType();
    }

    @Override
    public Type getGenericType() {
        return field.getGenericType();
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public <T extends Annotation> T getAnnotation(final Class<T> annotationClass) {
        return field.getAnnotation(annotationClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Y getValue(final X entity) {
        try {
            return (Y) field.get(entity);
        } catch (final IllegalAccessException ex) {
            LOG.error("Illegal access: {}", ex.getMessage(), ex);
            throw new MinijaxException(ex.getMessage(), ex);
        }
    }

    @Override
    public void setValue(final X entity, final Y value) {
        try {
            field.set(entity, value);
        } catch (final IllegalAccessException ex) {
            LOG.error("Illegal access: {}", ex.getMessage(), ex);
            throw new MinijaxException(ex.getMessage(), ex);
        }
    }
}
