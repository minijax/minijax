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
        return new Key<>(type, Strategy.DEFAULT, null, null, null);
    }


    public static <T> Key<T> of(final Class<T> type, final Annotation[] annotations) {
        return new Builder<>(type, annotations).build();
    }


    private static class Builder<T> {
        private final Class<T> type;
        private final Annotation[] annotations;
        private Strategy strategy;
        private Annotation qualifier;
        private String name;
        private DefaultValue defaultValue;

        public Builder(final Class<T> type, final Annotation[] annotations) {
            this.type = type;
            this.annotations = annotations;
        }

        public Key<T> build() {
            for (final Annotation annotation : annotations) {
                processAnnotation(annotation);
            }
            if (strategy == null) {
                strategy = Strategy.DEFAULT;
            }
            return new Key<>(type, strategy, qualifier, name, defaultValue);
        }

        private void processAnnotation(final Annotation annotation) {
            final Class<? extends Annotation> annType = annotation.annotationType();

            if (annType == Context.class) {
                processContextAnnotation((Context) annotation);

            } else if (annType == CookieParam.class) {
                processCookieParamAnnotation((CookieParam) annotation);

            } else if (annType == FormParam.class) {
                processFormParamAnnotation((FormParam) annotation);

            } else if (annType == HeaderParam.class) {
                processHeaderParamAnnotation((HeaderParam) annotation);

            } else if (annType == PathParam.class) {
                processPathParamAnnotation((PathParam) annotation);

            } else if (annType == QueryParam.class) {
                processQueryParamAnnotation((QueryParam) annotation);

            } else if (annType == DefaultValue.class) {
                defaultValue = (DefaultValue) annotation;

            } else if (annType.isAnnotationPresent(Qualifier.class)) {
                processQualifierAnnotation(annotation);
            }
        }

        private void processContextAnnotation(final Context context) {
            setStrategy(Strategy.CONTEXT);
            setQualifier(context);
        }

        private void processCookieParamAnnotation(final CookieParam cookieParam) {
            setStrategy(Strategy.COOKIE);
            setQualifier(cookieParam);
            setName(cookieParam.value());
        }

        private void processFormParamAnnotation(final FormParam formParam) {
            setStrategy(Strategy.FORM);
            setQualifier(formParam);
            setName(formParam.value());
        }

        private void processHeaderParamAnnotation(final HeaderParam headerParam) {
            setStrategy(Strategy.HEADER);
            setQualifier(headerParam);
            setName(headerParam.value());
        }

        private void processPathParamAnnotation(final PathParam pathParam) {
            setStrategy(Strategy.PATH);
            setQualifier(pathParam);
            setName(pathParam.value());
        }

        private void processQueryParamAnnotation(final QueryParam queryParam) {
            setStrategy(Strategy.QUERY);
            setQualifier(queryParam);
            setName(queryParam.value());
        }

        private void processQualifierAnnotation(final Annotation qualifierAnnotation) {
            setQualifier(qualifierAnnotation);
            if (qualifierAnnotation instanceof Named) {
                setName(((Named) qualifierAnnotation).value());
            }
        }

        private void setStrategy(final Strategy strategy) {
            if (this.strategy != null) {
                throw new InjectException("Multiple injection strategies");
            }
            this.strategy = strategy;
        }

        private void setQualifier(final Annotation qualifier) {
            if (this.qualifier != null) {
                throw new InjectException("Multiple injection qualifiers");
            }
            this.qualifier = qualifier;
        }

        private void setName(final String name) {
            if (this.name != null) {
                throw new InjectException("Multiple injection names");
            }
            this.name = name;
        }
    }
}