package org.minijax.rs;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.minijax.cdi.MinijaxProvider;
import org.minijax.rs.util.MediaTypeUtils;
import org.minijax.rs.util.UrlUtils;

class MinijaxResourceMethod implements javax.ws.rs.container.ResourceInfo {
    private final String httpMethod;
    private final Method method;
    private final MinijaxProvider<?>[] paramProviders;
    private final MinijaxPathPattern pathPattern;
    private final List<MediaType> produces;
    private final Annotation securityAnnotation;
    final int literalLength;

    public MinijaxResourceMethod(final String httpMethod, final Method method, final MinijaxProvider<?>[] paramProviders) {
        this(httpMethod, method, paramProviders, findPath(method), findProduces(method), findSecurityAnnotation(method));
    }

    MinijaxResourceMethod(
            final String httpMethod,
            final Method method,
            final MinijaxProvider<?>[] paramProviders,
            final String path,
            final List<MediaType> produces,
            final Annotation securityAnnotation) {
        this.httpMethod = Objects.requireNonNull(httpMethod);
        this.method = method;
        this.paramProviders = paramProviders;
        this.produces = Objects.requireNonNull(produces);
        this.securityAnnotation = securityAnnotation;
        pathPattern = MinijaxPathPattern.parse(method, path);
        literalLength = calculateLiteralLength(path);
    }

    @Override
    public Method getResourceMethod() {
        return method;
    }

    @Override
    public Class<?> getResourceClass() {
        return method.getDeclaringClass();
    }

    public List<MediaType> getProduces() {
        return produces;
    }

    public Annotation getSecurityAnnotation() {
        return securityAnnotation;
    }

    Object invoke(final MinijaxRequestContext ctx)
            throws Exception { // NOSONAR

        final Object instance;
        if (Modifier.isStatic(method.getModifiers())) {
            instance = null;
        } else {
            instance = ctx.get(method.getDeclaringClass());
        }

        final Object[] params = new Object[paramProviders.length];
        for (int i = 0; i < paramProviders.length; ++i) {
            params[i] = paramProviders[i].get(ctx);
        }

        try {
            return method.invoke(instance, params);
        } catch (final InvocationTargetException ex) {
            final Throwable cause = ex.getCause();
            if (cause instanceof Exception) {
                throw (Exception) cause;
            } else {
                throw ex;
            }
        } catch (IllegalAccessException | IllegalArgumentException ex) {
            throw new WebApplicationException(ex.getMessage(), ex);
        }
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


    /**
     * Calculates the "literal length" of the resource method path.
     *
     * Literal length is defined by the spec as:
     *
     *     Here, literal characters means those not resulting from template variable substitution.
     *
     * @param path The resource method path.
     * @return The literal length.
     */
    private static int calculateLiteralLength(final String path) {
        final int curlyIndex = path.indexOf('{');
        return curlyIndex != -1 ? curlyIndex : path.length();
    }


    public boolean tryMatch(final String httpMethod, final MinijaxUriInfo uriInfo) {
        if (!this.httpMethod.equals(httpMethod)) {
            return false;
        }

        final MultivaluedMap<String, String> pathParameters = pathPattern.tryMatch(uriInfo);
        if (pathParameters == null) {
            return false;
        }

        uriInfo.setPathParameters(pathParameters);
        return true;
    }


    /**
     * Sorts a list of resource methods.
     *
     * The sort function is defined in the JAX-RS spec section 3.7.2:
     *
     *     Sort E using the number of literal characters in each member as the primary key (descending
     *     order), the number of capturing groups as a secondary key (descending order) and the number
     *     of capturing groups with non-default regular expressions (i.e.  not ‘([ˆ/]+?)’) as the tertiary key
     *     (descending order).
     *
     * In short, sort by number of literal characters in the path.
     *
     * @param list The list of resource methods that will be sorted in place.
     */
    public static void sortByLiteralLength(final List<MinijaxResourceMethod> list) {
        Collections.sort(list, (m1, m2) -> Integer.compare(m2.literalLength, m1.literalLength));
    }
}
