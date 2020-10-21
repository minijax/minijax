package org.minijax.persistence.wrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Type;

import jakarta.persistence.PersistenceException;

/**
 * Represents an attribute of a Java type.
 *
 * @param <X> The represented type that contains the attribute
 * @param <Y> The type of the represented attribute
 */
public class FieldWrapper<X, Y> implements MemberWrapper<X, Y> {
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
            throw new PersistenceException(ex.getMessage(), ex);
        }
    }

    @Override
    public void setValue(final X entity, final Y value) {
        try {
            field.set(entity, value);
        } catch (final IllegalAccessException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
        }
    }
}
