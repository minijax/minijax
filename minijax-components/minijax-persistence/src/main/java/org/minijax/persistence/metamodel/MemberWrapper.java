package org.minijax.persistence.metamodel;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Type;

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
