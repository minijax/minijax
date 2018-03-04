package org.minijax;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Provider;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.ParamConverter;

import org.minijax.cdi.EntityProvider;
import org.minijax.cdi.MinijaxInjector;
import org.minijax.util.ClassPathScanner;
import org.minijax.util.ExceptionUtils;
import org.minijax.util.MediaTypeUtils;
import org.minijax.util.OptionalClasses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinijaxApplication extends Application implements Configuration, FeatureContext {
    private static final Logger LOG = LoggerFactory.getLogger(MinijaxApplication.class);
    private final Minijax container;
    private final String path;
    private final Set<Class<?>> classesScanned;
    private final List<MinijaxResourceMethod> resourceMethods;
    private final List<Class<?>> webSockets;
    private final List<Class<? extends ContainerRequestFilter>> requestFilters;
    private final List<Class<? extends ContainerResponseFilter>> responseFilters;
    private final MinijaxProviders providers;
    private Class<? extends SecurityContext> securityContextClass;


    public MinijaxApplication(final Minijax container, final String path) {
        this.container = container;
        this.path = path;
        classesScanned = new HashSet<>();
        resourceMethods = new ArrayList<>();
        webSockets = new ArrayList<>();
        requestFilters = new ArrayList<>();
        responseFilters = new ArrayList<>();
        providers = new MinijaxProviders(this);
    }

    public MinijaxInjector getInjector() {
        return container.getInjector();
    }

    public <T> T getResource(final Class<T> c) {
        return getInjector().getResource(c);
    }

    public String getPath() {
        return path;
    }

    @Override
    public Configuration getConfiguration() {
        return this;
    }

    @Override
    public RuntimeType getRuntimeType() {
        return RuntimeType.SERVER;
    }

    @Override
    public Map<String, Object> getProperties() {
        return container.getProperties();
    }

    @Override
    public Object getProperty(final String name) {
        return container.getProperties().get(name);
    }

    @Override
    public Collection<String> getPropertyNames() {
        return container.getProperties().keySet();
    }

    @Override
    public boolean isEnabled(final Feature feature) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEnabled(final Class<? extends Feature> featureClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRegistered(final Object component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRegistered(final Class<?> componentClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<Class<?>, Integer> getContracts(final Class<?> componentClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classesScanned;
    }

    @Override
    public Set<Object> getInstances() {
        return getInjector().getSingletons();
    }

    @Override
    public Set<Object> getSingletons() {
        return getInjector().getSingletons();
    }

    public List<Class<?>> getWebSockets() {
        return webSockets;
    }

    @Override
    public MinijaxApplication property(final String name, final Object value) {
        container.getProperties().put(name, value);
        return this;
    }


    @Override
    public MinijaxApplication register(final Class<?> componentClass) {
        registerImpl(componentClass);
        return this;
    }


    @Override
    public MinijaxApplication register(final Class<?> componentClass, final int priority) {
        registerImpl(componentClass);
        return this;
    }


    @Override
    public MinijaxApplication register(final Class<?> componentClass, final Class<?>... contracts) {
        for (final Class<?> contract : contracts) {
            getInjector().register(componentClass, contract);
        }
        registerImpl(componentClass);
        return this;
    }


    @Override
    public MinijaxApplication register(final Class<?> componentClass, final Map<Class<?>, Integer> contracts) {
        for (final Class<?> contract : contracts.keySet()) {
            getInjector().register(componentClass, contract);
        }
        registerImpl(componentClass);
        return this;
    }


    @Override
    public MinijaxApplication register(final Object component) {
        return this.register(component, component.getClass());
    }


    @Override
    public MinijaxApplication register(final Object component, final int priority) {
        return this.register(component, component.getClass());
    }


    @Override
    public MinijaxApplication register(final Object component, final Class<?>... contracts) {
        for (final Class<?> contract : contracts) {
            getInjector().register(component, contract);
        }
        registerImpl(component.getClass());
        return this;
    }


    @Override
    public MinijaxApplication register(final Object component, final Map<Class<?>, Integer> contracts) {
        for (final Class<?> contract : contracts.keySet()) {
            getInjector().register(component, contract);
        }
        registerImpl(component.getClass());
        return this;
    }


    public MinijaxApplication packages(final String... packageNames) {
        for (final String packageName : packageNames) {
            scanPackage(packageName);
        }
        return this;
    }


    public MinijaxApplication allowCors(final String urlPrefix) {
        register(MinijaxCorsFilter.class);
        getResource(MinijaxCorsFilter.class).addPathPrefix(urlPrefix);
        return this;
    }


    public MinijaxProviders getProviders() {
        return providers;
    }


    /*
     * Private helpers
     */


    private void scanPackage(final String packageName) {
        try {
            for (final Class<?> c : ClassPathScanner.scan(packageName)) {
                if (c.isAnnotationPresent(javax.ws.rs.ext.Provider.class)
                        || c.isAnnotationPresent(javax.ws.rs.Path.class)) {
                    registerImpl(c);
                }
            }
        } catch (final IOException ex) {
            throw new MinijaxException(ex.getMessage(), ex);
        }
    }


    private void registerImpl(final Class<?> c) {
        if (classesScanned.contains(c)) {
            return;
        }

        registerResourceMethods(c);
        registerWebSockets(c);
        registerFeature(c);
        registerFilter(c);
        registerSecurityContext(c);
        providers.register(c);
        classesScanned.add(c);
    }


    private void registerResourceMethods(final Class<?> c) {
        for (final Method method : c.getDeclaredMethods()) {
            for (final Annotation annotation : method.getAnnotations()) {
                final HttpMethod httpMethod = annotation.annotationType().getAnnotation(HttpMethod.class);
                if (httpMethod != null) {
                    resourceMethods.add(new MinijaxResourceMethod(httpMethod.value(), method, getParamProviders(method)));
                }
            }
        }
    }


    void addResourceMethod(final MinijaxResourceMethod rm) {
        resourceMethods.add(rm);
    }


    void sortResourceMethods() {
        MinijaxResourceMethod.sortByLiteralLength(resourceMethods);
    }


    private void registerWebSockets(final Class<?> c) {
        if (OptionalClasses.SERVER_ENDPOINT == null) {
            return;
        }
        final Annotation ws = c.getAnnotation(OptionalClasses.SERVER_ENDPOINT);
        if (ws != null) {
            webSockets.add(c);
        }
    }


    @SuppressWarnings("unchecked")
    private void registerFeature(final Class<?> c) {
        if (!Feature.class.isAssignableFrom(c)) {
            return;
        }

        final Class<? extends Feature> featureClass = (Class<? extends Feature>) c;
        try {
            getResource(featureClass).configure(this);
        } catch (final Exception ex) {
            throw new MinijaxException(ex);
        }
    }


    @SuppressWarnings("unchecked")
    private void registerFilter(final Class<?> c) {
        if (ContainerRequestFilter.class.isAssignableFrom(c)) {
            requestFilters.add((Class<? extends ContainerRequestFilter>) c);
        }

        if (ContainerResponseFilter.class.isAssignableFrom(c)) {
            responseFilters.add((Class<? extends ContainerResponseFilter>) c);
        }
    }


    @SuppressWarnings("unchecked")
    private void registerSecurityContext(final Class<?> c) {
        if (SecurityContext.class.isAssignableFrom(c)) {
            if (securityContextClass != null) {
                throw new IllegalStateException("Multiple security contexts detected (" + securityContextClass + ", " + c + ")");
            }
            securityContextClass = (Class<? extends SecurityContext>) c;
        }
    }


    public Response handle(final MinijaxRequestContext context) {
        final MinijaxResourceMethod rm = findRoute(context.getMethod(), (MinijaxUriInfo) context.getUriInfo());
        if (rm == null) {
            return toResponse(context, new NotFoundException());
        }

        context.setResourceMethod(rm);

        try {
            if (securityContextClass != null) {
                context.setSecurityContext(getResource(securityContextClass));
            }

            runRequestFilters(context);
            checkSecurity(context);
            final Response response = toResponse(rm, rm.invoke(context));
            runResponseFilters(context, response);
            return response;
        } catch (final Exception ex) {
            LOG.debug(ex.getMessage(), ex);
            return toResponse(context, ex);
        }
    }


    public void handle(
            final MinijaxRequestContext context,
            final HttpServletResponse servletResponse) {

        try {
            write(context, handle(context), servletResponse);
        } catch (final Exception ex) {
            LOG.debug(ex.getMessage(), ex);
            handleUnexpectedError(ex, servletResponse);
        }
    }


    private void handleUnexpectedError(final Exception ex, final HttpServletResponse servletResponse) {
        LOG.warn("Unexpected error: {}", ex.getMessage(), ex);
        servletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }


    private MinijaxResourceMethod findRoute(final String httpMethod, final MinijaxUriInfo uriInfo) {
        for (final MinijaxResourceMethod rm : resourceMethods) {
            if (rm.tryMatch(httpMethod, uriInfo)) {
                return rm;
            }
        }

        if (httpMethod.equals("OPTIONS")) {
            return findRoute("GET", uriInfo);
        }

        return null;
    }


    private void runRequestFilters(final MinijaxRequestContext context) throws IOException {
        for (final Class<? extends ContainerRequestFilter> filterClass : requestFilters) {
            final ContainerRequestFilter filter = getResource(filterClass);
            filter.filter(context);
        }
    }


    private void runResponseFilters(final MinijaxRequestContext context, final Response response) throws IOException {
        final ContainerResponseContext responseContext = (ContainerResponseContext) response;
        for (final Class<? extends ContainerResponseFilter> filterClass : responseFilters) {
            final ContainerResponseFilter filter = getResource(filterClass);
            filter.filter(context, responseContext);
        }
    }


    private void checkSecurity(final MinijaxRequestContext context) {
        final Annotation a = context.getResourceMethod().getSecurityAnnotation();
        if (a == null) {
            return;
        }

        final Class<?> c = a.annotationType();
        if (c == PermitAll.class) {
            return;
        }

        if (c == DenyAll.class) {
            throw new ForbiddenException();
        }

        if (c == RolesAllowed.class) {
            final SecurityContext security = context.getSecurityContext();
            if (security == null || security.getUserPrincipal() == null) {
                throw new NotAuthorizedException(Response.status(Status.UNAUTHORIZED).build());
            }

            boolean found = false;
            for (final String role : ((RolesAllowed) a).value()) {
                if (security.isUserInRole(role)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new ForbiddenException();
            }
        }
    }


    /**
     * Returns the param providers for a resource method.
     *
     * This is very similar to the logic used in building param providers for a normal
     * <code>@Inject</code> constructor, with one major difference.
     *
     * A resource method is allowed one special "entity" parameter representing the content body.
     * This entity parameter is handled by a <code>EntityProvider</code>.
     *
     * @param method The resource method.
     * @return The array of resource method param providers.
     */
    private Provider<?>[] getParamProviders(final Method method) {
        final Class<?>[] paramClasses = method.getParameterTypes();
        final Type[] paramTypes = method.getGenericParameterTypes();
        final Annotation[][] annotations = method.getParameterAnnotations();
        final Provider<?>[] result = new Provider<?>[paramTypes.length];

        final Consumes consumes = method.getAnnotation(Consumes.class);
        final List<MediaType> consumesTypes = MediaTypeUtils.parseMediaTypes(consumes);
        boolean consumed = false;

        for (int i = 0; i < paramTypes.length; i++) {
            if (annotations[i].length == 0 && !consumed) {
                result[i] = new EntityProvider<>(paramClasses[i], paramTypes[i], annotations[i], consumesTypes);
                consumed = true;
            } else {
                result[i] = getInjector().getProvider(paramClasses[i], annotations[i]);
            }
        }
        return result;
    }


    private Response toResponse(final MinijaxResourceMethod rm, final Object obj) {
        if (obj == null) {
            throw new NotFoundException();
        }

        if (obj instanceof Response) {
            return (Response) obj;
        }

        return Response.ok()
                .entity(obj)
                .type(findResponseType(obj, rm.getProduces()))
                .build();
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Response toResponse(final MinijaxRequestContext context, final Exception ex) {
        final MinijaxResourceMethod rm = context.getResourceMethod();
        final List<MediaType> mediaTypes;

        if (rm != null) {
            mediaTypes = rm.getProduces();
        } else {
            mediaTypes = context.getAcceptableMediaTypes();
        }

        for (final MediaType mediaType : mediaTypes) {
            final ExceptionMapper mapper = providers.getExceptionMapper(ex.getClass(), mediaType);
            if (mapper != null) {
                return mapper.toResponse(ex);
            }
        }

        return ExceptionUtils.toWebAppException(ex).getResponse();
    }


    @SuppressWarnings("rawtypes")
    private MediaType findResponseType(
            final Object obj,
            final List<MediaType> produces) {

        final Class<?> objType = obj == null ? null : obj.getClass();

        for (final MediaType mediaType : produces) {
            final MessageBodyWriter writer = providers.getMessageBodyWriter(objType, null, null, mediaType);
            if (writer != null) {
                return mediaType;
            }
        }

        return MediaType.TEXT_PLAIN_TYPE;
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void write(
            final MinijaxRequestContext context,
            final Response response,
            final HttpServletResponse servletResponse)
                    throws IOException {

        servletResponse.setStatus(response.getStatus());

        for (final Entry<String, List<Object>> entry : response.getHeaders().entrySet()) {
            final String name = entry.getKey();
            for (final Object value : entry.getValue()) {
                servletResponse.addHeader(name, value.toString());
            }
        }

        if (context.getMethod().equals("OPTIONS")) {
            return;
        }

        final MediaType mediaType = response.getMediaType();
        if (mediaType != null) {
            servletResponse.setContentType(mediaType.toString());
        }

        final Object obj = response.getEntity();
        if (obj == null) {
            return;
        }

        final MessageBodyWriter writer = providers.getMessageBodyWriter(obj.getClass(), null, null, mediaType);
        if (writer != null) {
            writer.writeTo(obj, obj.getClass(), null, null, mediaType, null, servletResponse.getOutputStream());
            return;
        }

        // What to do
        servletResponse.getWriter().println(obj.toString());
    }


    /**
     * Converts a parameter to a type.
     *
     * @param <T>         the supported Java type convertible to/from a {@code String} format.
     * @param str         The parameter string contents.
     * @param c           the raw type of the object to be converted.
     * @param annotations an array of the annotations associated with the convertible
     *                    parameter instance. E.g. if a string value is to be converted into a method parameter,
     *                    this would be the annotations on that parameter as returned by
     *                    {@link java.lang.reflect.Method#getParameterAnnotations}.
     * @return            the newly created instance of {@code T}.
     */
    public <T> T convertParamToType(final String str, final Class<T> c, final Annotation[] annotations) {
        final ParamConverter<T> converter = providers.getParamConverter(c, null, annotations);
        if (converter != null) {
            return converter.fromString(str);
        }

        // Try default primitive converters
        return convertStringToType(str, c);
    }


    @SuppressWarnings({ "unchecked" })
    private <T> T convertStringToType(final String str, final Class<T> c) {
        if (str == null) {
            return null;
        }

        if (c == String.class) {
            return (T) str;
        }

        if (c.isPrimitive()) {
            return convertStringToPrimitive(str, c);
        }

        try {
            final Constructor<?> ctor = c.getDeclaredConstructor(String.class);
            return (T) ctor.newInstance(str);
        } catch (final NoSuchMethodException ex) {
            // Ignore
        } catch (final Exception ex) {
            throw ExceptionUtils.toWebAppException(ex);
        }

        try {
            final Method valueOf = c.getDeclaredMethod("valueOf", String.class);
            return (T) valueOf.invoke(null, str);
        } catch (final NoSuchMethodException ex) {
            // Ignore
        } catch (final Exception ex) {
            throw ExceptionUtils.toWebAppException(ex);
        }

        throw new MinijaxException("No string conversion for \"" + c + "\"");
    }


    @SuppressWarnings({ "unchecked" })
    private <T> T convertStringToPrimitive(final String str, final Class<T> c) {
        if (c == boolean.class) {
            return (T) Boolean.valueOf(str);
        }
        if (c == byte.class) {
            return (T) Byte.valueOf(str);
        }
        if (c == char.class) {
            return (T) (str.isEmpty() ? null : ((Character) str.charAt(0)));
        }
        if (c == double.class) {
            return (T) Double.valueOf(str);
        }
        if (c == float.class) {
            return (T) Float.valueOf(str);
        }
        if (c == int.class) {
            return (T) Integer.valueOf(str);
        }
        if (c == long.class) {
            return (T) Long.valueOf(str);
        }
        if (c == short.class) {
            return (T) Short.valueOf(str);
        }
        throw new IllegalArgumentException("Unrecognized primitive (" + c + ")");
    }
}
