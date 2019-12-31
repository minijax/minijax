package org.minijax.validation.metadata;

import java.lang.reflect.Method;

import javax.validation.ValidationException;

public class MinijaxGetterDescriptor extends MinijaxPropertyDescriptor {
    private final Method getter;
    private final String propertyName;

    public MinijaxGetterDescriptor(final Method getter) {
        super(getter.getDeclaringClass(), getter.getAnnotatedReturnType(), getter.getAnnotations());
        this.getter = getter;
        propertyName = getter.getName().substring(3, 4).toLowerCase() + getter.getName().substring(4);
    }

    @Override
    public Object getValue(final Object object) {
        try {
            return getter.invoke(object);
        } catch (final ReflectiveOperationException ex) {
            throw new ValidationException(ex);
        }
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }
}
