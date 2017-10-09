package org.minijax;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.minijax.util.MediaTypeUtils;
import org.minijax.util.UrlUtils;

public class MinijaxResourceMethod {
    private final Class<?> resourceClass;
    private final Method method;
    private final String httpMethod;
    private final String path;
    private final List<String> pathParams;
    private final Pattern pathPattern;
    private final List<MediaType> produces;
    private final Annotation securityAnnotation;

    public MinijaxResourceMethod(final Method m) {
        resourceClass = m.getDeclaringClass();
        method = m;
        httpMethod = findHttpMethod(method);
        path = findPath(m);
        pathParams = UrlUtils.getPathParams(path);
        pathPattern = Pattern.compile(UrlUtils.convertPathToRegex(path));
        produces = findProduces(m);
        securityAnnotation = findSecurityAnnotation(m);
    }


    public Class<?> getResourceClass() {
        return resourceClass;
    }


    public Method getMethod() {
        return method;
    }


    public String getHttpMethod() {
        return httpMethod;
    }


    public String getPath() {
        return path;
    }


    public List<MediaType> getProduces() {
        return produces;
    }


    public Annotation getSecurityAnnotation() {
        return securityAnnotation;
    }


    private static String findHttpMethod(final Method m) {
        for (final Annotation annotation : m.getAnnotations()) {
            if (annotation.annotationType() == HttpMethod.class) {
                return ((HttpMethod) annotation).value();
            }

            final HttpMethod hm = annotation.annotationType().getAnnotation(HttpMethod.class);
            if (hm != null) {
                return hm.value();
            }
        }
        return null;
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

        final Matcher matcher = pathPattern.matcher(requestPath);
        if (!matcher.matches()) {
            return false;
        }

        final MultivaluedMap<String, String> pathParameters = new MultivaluedHashMap<>();
        for (final String name : pathParams) {
            pathParameters.add(name, matcher.group(name));
        }

        uriInfo.setPathParameters(pathParameters);
        return true;
    }
}
