package org.minijax.cdi;

import java.lang.annotation.Annotation;
import java.util.Objects;

import javax.inject.Named;
import javax.inject.Qualifier;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

public class Key<T> {
    private final Class<T> type;
    private final Strategy strategy;
    private final Annotation qualifier;
    private final String name;
    private final DefaultValue defaultValue;

    private Key(
            final Class<T> type,
            final Strategy strategy,
            final Annotation qualifier,
            final String name,
            final DefaultValue defaultValue) {

        this.type = type;
        this.strategy = strategy;
        this.qualifier = qualifier;
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public Class<T> getType() {
        return type;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public String getName() {
        return name;
    }

    public DefaultValue getDefaultValue() {
        return defaultValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = type.hashCode();
        result = prime * result + strategy.hashCode();
        result = prime * result + (qualifier == null ? 0 : qualifier.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (defaultValue == null ? 0 : defaultValue.hashCode());
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
        return Objects.equals(type, other.type) &&
                Objects.equals(strategy, other.strategy) &&
                Objects.equals(qualifier, other.qualifier) &&
                Objects.equals(name, other.name) &&
                Objects.equals(defaultValue, other.defaultValue);
    }

    @Override
    public String toString() {
        return "Key [type=" + type
                + ", strategy=" + strategy
                + ", qualifier=" + qualifier
                + ", name=" + name
                + ", defaultValue=" + defaultValue + "]";
    }


    public static <T> Key<T> of(final Class<T> type) {
        return new Key<T>(type, Strategy.DEFAULT, null, null, null);
    }


    public static <T> Key<T> of(final Class<T> type, final Annotation[] annotations) {
        Strategy strategy = Strategy.DEFAULT;
        Annotation qualifier = null;
        String name = null;
        DefaultValue defaultValue = null;

        for (final Annotation annotation : annotations) {
            final Class<? extends Annotation> annType = annotation.annotationType();

            if (annType.isAnnotationPresent(Qualifier.class)) {
                if (qualifier != null) {
                    throw new InjectException("Multiple qualifiers");
                }
                if (annType == Named.class) {
                    name = ((Named) annotation).value();
                }
                qualifier = annotation;

            } else if (annType == Context.class) {
                strategy = Strategy.CONTEXT;
                qualifier = annotation;

            } else if (annType == CookieParam.class) {
                strategy = Strategy.COOKIE;
                qualifier = annotation;
                name = ((CookieParam) qualifier).value();

            } else if (annType == FormParam.class) {
                strategy = Strategy.FORM;
                qualifier = annotation;
                name = ((FormParam) qualifier).value();

            } else if (annType == HeaderParam.class) {
                strategy = Strategy.HEADER;
                qualifier = annotation;
                name = ((HeaderParam) qualifier).value();

            } else if (annType == PathParam.class) {
                strategy = Strategy.PATH;
                qualifier = annotation;
                name = ((PathParam) qualifier).value();

            } else if (annType == QueryParam.class) {
                strategy = Strategy.QUERY;
                qualifier = annotation;
                name = ((QueryParam) qualifier).value();

            } else if (annType == DefaultValue.class) {
                defaultValue = (DefaultValue) annotation;
            }
        }

        return new Key<T>(type, strategy, qualifier, name, defaultValue);
    }
}