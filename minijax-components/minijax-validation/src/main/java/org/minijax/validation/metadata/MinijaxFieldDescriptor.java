package org.minijax.validation.metadata;

import java.lang.reflect.Field;

import javax.validation.ValidationException;

public class MinijaxFieldDescriptor extends MinijaxPropertyDescriptor {
    private final Field field;

    public MinijaxFieldDescriptor(final Field field) {
        super(field.getDeclaringClass(), field.getAnnotatedType(), field.getAnnotations());
        this.field = field;
    }

    @Override
    public Object getValue(final Object object) {
        try {
            return field.get(object);
        } catch (final ReflectiveOperationException ex) {
            throw new ValidationException(ex);
        }
    }

    @Override
    public String getPropertyName() {
        return field.getName();
    }
}
