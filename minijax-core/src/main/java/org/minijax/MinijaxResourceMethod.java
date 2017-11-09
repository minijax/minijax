package org.minijax;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.minijax.util.MediaTypeUtils;
import org.minijax.util.UrlUtils;

class MinijaxResourceMethod {
    private final String httpMethod;
    private final Method method;
    private final String path;
    private final MinijaxPathPattern pathPattern;
    private final List<MediaType> produces;
    private final Annotation securityAnnotation;

    public MinijaxResourceMethod(final String httpMethod, final Method m) {
        this.httpMethod = httpMethod;
        method = m;
        path = findPath(m);
        pathPattern = MinijaxPathPattern.parse(m, path);
        produces = findProduces(m);
        securityAnnotation = findSecurityAnnotation(m);
    }


    public Method getMethod() {
        return method;
    }


    public List<MediaType> getProduces() {
        return produces;
    }


    public Annotation getSecurityAnnotation() {
        return securityAnnotation;
    }


    private static String findPath(final Method m) {
        final Annotation classAnnotation = m.getDeclaringClass().getAnnotation(Path.class);
        final Annotation methodAnnotation = m.getAnnotation(Path.class);

        final String classPath = classAnnotation == null ? "/" : ((Path) classAnnotation).value();
        final String methodPath = methodAnnotation == null ? "" : ((Path) methodAnnotation).value();

        return UrlUtils.concatUrlPaths(classPath, methodPath);
    }


    private static List<MediaType> findProduces(final Method m) {
        final List<MediaType> result = new ArrayList<>();
        result.addAll(MediaTypeUtils.parseMediaTypes(m.getDeclaringClass().getAnnotation(Produces.class)));
        result.addAll(MediaTypeUtils.parseMediaTypes(m.getAnnotation(Produces.class)));
        return result;
    }


    private static Annotation findSecurityAnnotation(final Method m) {
        final Annotation methodAnnotation = findSecurityAnnotation(m.getAnnotations());
        if (methodAnnotation != null) {
            return methodAnnotation;
        }
        return findSecurityAnnotation(m.getDeclaringClass().getAnnotations());
    }


    private static Annotation findSecurityAnnotation(final Annotation[] annotations) {
        for (final Annotation a : annotations) {
            final Class<?> c = a.annotationType();
            if (c == PermitAll.class || c == DenyAll.class || c == RolesAllowed.class) {
                return a;
            }
        }
        return null;
    }


    public boolean tryMatch(final String httpMethod, final MinijaxUriInfo uriInfo) {
        if (!this.httpMethod.equals(httpMethod)) {
            return false;
        }

        final String requestPath = uriInfo.getRequestUri().getPath();

        final Matcher matcher = pathPattern.getPattern().matcher(requestPath);
        if (!matcher.matches()) {
            return false;
        }

        final MultivaluedMap<String, String> pathParameters = new MultivaluedHashMap<>();
        for (final String name : pathPattern.getParams()) {
            pathParameters.add(name, matcher.group(name));
        }

        uriInfo.setPathParameters(pathParameters);
        return true;
    }
}
