package org.minijax.rs.cdi;

import java.lang.annotation.Annotation;

import org.minijax.cdi.annotation.FieldAnnotationProcessor;

abstract class AbstractParamAnnotationProcessor<T> implements FieldAnnotationProcessor<T> {


//    @Override
//    public MinijaxProvider<T> buildProvider(final MinijaxInjectorState state, final Class<T> type, final Annotation[] annotations) {
//        final DefaultValue defaultValue = getDefaultValue(annotations);
//        MinijaxProvider<T> provider = null;
//        int count = 0;
//
//        for (final Annotation annotation : annotations) {
//            final Class<?> annotationType = annotation.annotationType();
//
//            if (annotationType == CookieParam.class) {
//                final CookieParam cookieParam = (CookieParam) annotation;
//                final String name = cookieParam.value();
//                provider = new CookieParamProvider<>(type, annotations, name, defaultValue);
//                count++;
//
//            } else if (annotationType == FormParam.class) {
//                final FormParam formParam = (FormParam) annotation;
//                final String name = formParam.value();
//                provider = new FormParamProvider<>(type, annotations, name, defaultValue);
//                count++;
//
//            } else if (annotationType == HeaderParam.class) {
//                final HeaderParam headerParam = (HeaderParam) annotation;
//                final String name = headerParam.value();
//                provider = new HeaderParamProvider<>(type, annotations, name, defaultValue);
//                count++;
//
//            } else if (annotationType == PathParam.class) {
//                final PathParam pathParam = (PathParam) annotation;
//                final String name = pathParam.value();
//                provider = new PathParamProvider<>(type, annotations, name, defaultValue);
//                count++;
//
//            } else if (annotationType == QueryParam.class) {
//                final QueryParam queryParam = (QueryParam) annotation;
//                final String name = queryParam.value();
//                provider = new QueryParamProvider<>(type, annotations, name, defaultValue);
//                count++;
//            }
//        }
//
//        if (count > 1) {
//            throw new InjectionException("Multiple request injection annotations");
//        }
//
//        return provider;
//    }

    @SuppressWarnings("unchecked")
    protected static <T extends Annotation> T getAnnotationByType(final Annotation[] annotations, final Class<T> c) {
        for (final Annotation annotation : annotations) {
            if (annotation.annotationType() == c) {
                return (T) annotation;
            }
        }
        return null;
    }
}
