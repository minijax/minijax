package org.minijax.cdi;

import java.lang.annotation.Annotation;
import java.util.Objects;

import javax.enterprise.inject.InjectionException;
import javax.inject.Named;
import javax.inject.Qualifier;

public class Key<T> {
    protected final Class<T> type;
    private final Annotation injectAnnotation;
    private final Class<? extends Annotation> qualifier;
    private final String name;

    public Key(final Class<T> type) {
        this.type = type;
        this.injectAnnotation = null;
        this.qualifier = null;
        this.name = null;
    }

    public Key(final Class<T> type, final Class<? extends Annotation> qualifier) {
        this.type = type;
        this.injectAnnotation = null;
        this.qualifier = qualifier;
        this.name = null;
    }

    public Key(final Class<T> type, final String name) {
        this.type = type;
        this.injectAnnotation = null;
        this.qualifier = Named.class;
        this.name = name;
    }

    public Key(final Class<T> type, final Annotation injectAnnotation, final Annotation[] annotations) {
        Class<? extends Annotation> qualifier = null;
        String name = null;

        if (annotations != null) {
            for (final Annotation annotation : annotations) {
                if (annotation.annotationType().isAnnotationPresent(Qualifier.class)) {
                    if (qualifier != null) {
                        throw new InjectionException("Multiple qualifiers");
                    }
                    qualifier = annotation.annotationType();
                    if (annotation.annotationType() == Named.class) {
                        name = ((Named) annotation).value();
                    }
                }
            }
        }

        this.type = type;
        this.injectAnnotation = injectAnnotation;
        this.qualifier = qualifier;
        this.name = name;
    }

    public Class<T> getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = type.hashCode();
        result = prime * result + (qualifier == null ? 0 : qualifier.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
        if (injectAnnotation != null) {
            result = prime * result + injectAnnotation.hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        @SuppressWarnings("rawtypes")
        final Key other = (Key) obj;
        return type == other.type &&
                Objects.equals(injectAnnotation, other.injectAnnotation) &&
                Objects.equals(qualifier, other.qualifier) &&
                Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return "Key [type=" + type
                + ", qualifier=" + qualifier
                + ", name=" + name + "]";
    }
}
