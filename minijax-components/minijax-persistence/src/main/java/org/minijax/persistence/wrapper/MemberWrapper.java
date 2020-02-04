package org.minijax.persistence.wrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Type;

/**
 * Represents an attribute of a Java type.
 *
 * @param <X> The represented type that contains the attribute
 * @param <Y> The type of the represented attribute
 */
public interface MemberWrapper<X, Y> {

    Class<X> getDeclaringType();

    Member getMember();

    Class<Y> getType();

    Type getGenericType();

    String getName();

    <T extends Annotation> T getAnnotation(Class<T> annotationClass);

    Y getValue(X instance);

    void setValue(X instance, Y value);
}
