package org.minijax.rs;

import static jakarta.ws.rs.HttpMethod.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.RuntimeType;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Configuration;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.ReaderInterceptor;
import jakarta.ws.rs.ext.WriterInterceptor;

import org.minijax.cdi.MinijaxInjector;
import org.minijax.cdi.MinijaxProvider;
import org.minijax.cdi.annotation.DefaultFieldAnnotationProcessor;
import org.minijax.commons.MinijaxException;
import org.minijax.commons.OptionalClasses;
import org.minijax.rs.cdi.ContextAnnotationProcessor;
import org.minijax.rs.cdi.CookieParamAnnotationProcessor;
import org.minijax.rs.cdi.EntityProvider;
import org.minijax.rs.cdi.FormParamAnnotationProcessor;
import org.minijax.rs.cdi.HeaderParamAnnotationProcessor;
import org.minijax.rs.cdi.PathParamAnnotationProcessor;
import org.minijax.rs.cdi.QueryParamAnnotationProcessor;
import org.minijax.rs.cdi.RequestScopedAnnotationProcessor;
import org.minijax.rs.converters.ConstructorParamConverterProvider;
import org.minijax.rs.converters.PrimitiveParamConverterProvider;
import org.minijax.rs.converters.UuidParamConverterProvider;
import org.minijax.rs.converters.ValueOfParamConverterProvider;
import org.minijax.rs.util.MediaTypeClassMap;
import org.minijax.rs.util.MediaTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinijaxApplicationContext implements Configuration, FeatureContext {
    private static final Logger LOG = LoggerFactory.getLogger(MinijaxApplicationContext.class);
    private final String path;
    private final MinijaxInjector injector;
    private final MinijaxConfiguration configuration;
    private final Set<Class<?>> classesScanned;
    private final List<MinijaxResourceMethod> resourceMethods;
    private final List<Class<?>> webSockets;
    private final List<DynamicFeature> dynamicFeatures;
    private final List<Class<? extends ContainerRequestFilter>> requestFilters;
    private final List<Class<? extends ContainerResponseFilter>> responseFilters;
    private final List<Class<? extends ReaderInterceptor>> readerInterceptors;
    private final List<Class<? extends WriterInterceptor>> writerInterceptors;
    private final MediaTypeClassMap<MessageBodyReader<?>> readers;
    private final MediaTypeClassMap<MessageBodyWriter<?>> writers;
    private final MediaTypeClassMap<ExceptionMapper<?>> exceptionMappers;
    private final List<ParamConverterProvider> paramConverterProviders;
    private Class<? extends SecurityContext> securityContextClass;

    public MinijaxApplicationContext(final String path) {
        this.path = path;
        injector = new MinijaxInjector();
        injector.addTypeAnnotationProcessor(RequestScoped.class, new RequestScopedAnnotationProcessor<>());
        injector.addFieldAnnotationProcessor(BeanParam.class, new DefaultFieldAnnotationProcessor<>());
        injector.addFieldAnnotationProcessor(CookieParam.class, new CookieParamAnnotationProcessor<>());
        injector.addFieldAnnotationProcessor(FormParam.class, new FormParamAnnotationProcessor<>());
        injector.addFieldAnnotationProcessor(HeaderParam.class, new HeaderParamAnnotationProcessor<>());
        injector.addFieldAnnotationProcessor(PathParam.class, new PathParamAnnotationProcessor<>());
        injector.addFieldAnnotationProcessor(QueryParam.class, new QueryParamAnnotationProcessor<>());
        injector.addFieldAnnotationProcessor(Context.class, new ContextAnnotationProcessor<>());

        configuration = new MinijaxConfiguration();
        classesScanned = new HashSet<>();
        resourceMethods = new ArrayList<>();
        webSockets = new ArrayList<>();
        dynamicFeatures = new ArrayList<>();
        requestFilters = new ArrayList<>();
        responseFilters = new ArrayList<>();
        readerInterceptors = new ArrayList<>();
        writerInterceptors = new ArrayList<>();
        readers = new MediaTypeClassMap<>();
        writers = new MediaTypeClassMap<>();
        exceptionMappers = new MediaTypeClassMap<>();
        paramConverterProviders = new ArrayList<>();
        paramConverterProviders.add(new PrimitiveParamConverterProvider());
        paramConverterProviders.add(new ConstructorParamConverterProvider());
        paramConverterProviders.add(new ValueOfParamConverterProvider());
        paramConverterProviders.add(new UuidParamConverterProvider());
    }

    /**
     * Registers a <code>jakarta.ws.rs.core.Application</code> class.
     *
     * Instantiate the class and register the individual components.
     *
     * Note the somewhat indirect relationship between:
     *   1) Registering an Application
     *   2) Creating an ApplicationContext
     *   3) Using an Application later via MinijaxApplicationView.
     *
     * Minijax does not make any guarantees about the consistency of the application instance.
     *
     * @param applicationClass The application class.
     */
    public MinijaxApplicationContext(final Class<Application> applicationClass) {
        this(applicationClass.getAnnotation(ApplicationPath.class).value());
        registerApplication(applicationClass);
    }

    public Application getApplication() {
        return new MinijaxApplicationView(this);
    }

    public MinijaxInjector getInjector() {
        return injector;
    }

    public <T> T getResource(final Class<T> c) {
        return getInjector().getResource(c, null);
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
        return configuration.getProperties();
    }

    @Override
    public Object getProperty(final String name) {
        return configuration.getProperties().get(name);
    }

    @Override
    public Collection<String> getPropertyNames() {
        return configuration.getProperties().keySet();
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

    public List<Class<?>> getWebSockets() {
        return webSockets;
    }

    @Override
    public MinijaxApplicationContext property(final String name, final Object value) {
        configuration.getProperties().put(name, value);
        return this;
    }

    public MinijaxApplicationContext properties(final Map<String, String> props) {
        configuration.properties(props);
        return this;
    }

    public MinijaxApplicationContext properties(final Properties props) {
        configuration.properties(props);
        return this;
    }

    public MinijaxApplicationContext properties(final File file) throws IOException {
        configuration.properties(file);
        return this;
    }

    public MinijaxApplicationContext properties(final InputStream inputStream) throws IOException {
        configuration.properties(inputStream);
        return this;
    }

    public MinijaxApplicationContext properties(final String fileName) throws IOException {
        configuration.properties(fileName);
        return this;
    }

    @Override
    public MinijaxApplicationContext register(final Class<?> componentClass) {
        registerImpl(componentClass);
        return this;
    }

    @Override
    public MinijaxApplicationContext register(final Class<?> componentClass, final int priority) {
        registerImpl(componentClass);
        return this;
    }

    @Override
    public MinijaxApplicationContext register(final Class<?> componentClass, final Class<?>... contracts) {
        for (final Class<?> contract : contracts) {
            getInjector().register(componentClass, contract);
        }
        registerImpl(componentClass);
        return this;
    }

    @Override
    public MinijaxApplicationContext register(final Class<?> componentClass, final Map<Class<?>, Integer> contracts) {
        for (final Class<?> contract : contracts.keySet()) {
            getInjector().register(componentClass, contract);
        }
        registerImpl(componentClass);
        return this;
    }

    @Override
    public MinijaxApplicationContext register(final Object component) {
        return this.register(component, component.getClass());
    }

    @Override
    public MinijaxApplicationContext register(final Object component, final int priority) {
        return this.register(component, component.getClass());
    }

    @Override
    public MinijaxApplicationContext register(final Object component, final Class<?>... contracts) {
        for (final Class<?> contract : contracts) {
            getInjector().register(component, contract);
        }
        registerImpl(component.getClass());
        return this;
    }

    @Override
    public MinijaxApplicationContext register(final Object component, final Map<Class<?>, Integer> contracts) {
        for (final Class<?> contract : contracts.keySet()) {
            getInjector().register(component, contract);
        }
        registerImpl(component.getClass());
        return this;
    }

    public MinijaxApplicationContext allowCors(final String urlPrefix) {
        register(MinijaxCorsFilter.class);
        getResource(MinijaxCorsFilter.class).addPathPrefix(urlPrefix);
        return this;
    }

    /*
     * Protected accessors.
     */

    MediaTypeClassMap<MessageBodyReader<?>> getReaders() {
        return readers;
    }

    MediaTypeClassMap<MessageBodyWriter<?>> getWriters() {
        return writers;
    }

    MediaTypeClassMap<ExceptionMapper<?>> getExceptionMappers() {
        return exceptionMappers;
    }

    List<ParamConverterProvider> getParamConverterProviders() {
        return paramConverterProviders;
    }

    /*
     * Private helpers
     */

    @SuppressWarnings("unchecked")
    private void registerImpl(final Class<?> c) {
        if (classesScanned.contains(c)) {
            return;
        }
        registerResourceMethods(c);
        registerWebSockets(c);
        registerFeature(c);
        registerDynamicFeature(c);
        registerFilter(c);
        registerSecurityContext(c);

        if (MessageBodyReader.class.isAssignableFrom(c)) {
            readers.add((Class<MessageBodyReader<?>>) c, MediaTypeUtils.parseMediaTypes(c.getAnnotation(Consumes.class)));
        }

        if (MessageBodyWriter.class.isAssignableFrom(c)) {
            writers.add((Class<MessageBodyWriter<?>>) c, MediaTypeUtils.parseMediaTypes(c.getAnnotation(Produces.class)));
        }

        if (ReaderInterceptor.class.isAssignableFrom(c)) {
            readerInterceptors.add((Class<? extends ReaderInterceptor>) c);
        }

        if (WriterInterceptor.class.isAssignableFrom(c)) {
            writerInterceptors.add((Class<? extends WriterInterceptor>) c);
        }

        if (ExceptionMapper.class.isAssignableFrom(c)) {
            exceptionMappers.add((Class<ExceptionMapper<?>>) c, MediaTypeUtils.parseMediaTypes(c.getAnnotation(Produces.class)));
        }

        if (ParamConverterProvider.class.isAssignableFrom(c)) {
            paramConverterProviders.add((ParamConverterProvider) getResource(c));
        }

        classesScanned.add(c);
    }

    /**
     * Registers a <code>jakarta.ws.rs.core.Application</code> class.
     *
     * Instantiate the class and register the individual components.
     *
     * Note the somewhat indirect relationship between:
     *   1) Registering an Application
     *   2) Creating an ApplicationContext
     *   3) Using an Application later via MinijaxApplicationView.
     *
     * Minijax does not make any guarantees about the consistency of the application instance.
     *
     * @param c The auto scanned class.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void registerApplication(final Class<?> c) {
        final Application app = (Application) getInjector().getResource(c);

        final Map<String, Object> properties = app.getProperties();
        if (properties != null) {
            properties((Map) properties);
        }

        final Set<Class<?>> classes = app.getClasses();
        if (classes != null) {
            for (final Class<?> c2 : classes) {
                register(c2);
            }
        }

        final Set<Object> singletons = app.getSingletons();
        if (singletons != null) {
            for (final Object singleton : singletons) {
                register(singleton);
            }
        }
    }

    private void registerResourceMethods(final Class<?> c) {
        for (final Method method : c.getDeclaredMethods()) {
            for (final Annotation annotation : method.getAnnotations()) {
                final HttpMethod httpMethod = annotation.annotationType().getAnnotation(HttpMethod.class);
                if (httpMethod != null) {
                    addResourceMethod(new MinijaxResourceMethod(httpMethod.value(), method, getParamProviders(method)));
                }
            }
        }
    }

    public void addResourceMethod(final MinijaxResourceMethod rm) {
        for (final DynamicFeature dynamicFeature : dynamicFeatures) {
            dynamicFeature.configure(rm, this);
        }

        resourceMethods.add(rm);
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

    private void registerDynamicFeature(final Class<?> c) {
        if (DynamicFeature.class.isAssignableFrom(c)) {
            dynamicFeatures.add((DynamicFeature) getResource(c));
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
            return context.toResponse(new NotFoundException());
        }

        context.setResourceMethod(rm);

        Response response = null;

        try {
            if (securityContextClass != null) {
                context.setSecurityContext(context.getResource(securityContextClass));
            }

            runRequestFilters(context);
            checkSecurity(context);
            response = context.toResponse(rm.invoke(context));
        } catch (final MinijaxAbortException ex) {
            response = ex.getResponse();
        } catch (final WebApplicationException ex) {
            LOG.debug(ex.getMessage(), ex);
            response = context.toResponse(ex);
        } catch (final Exception ex) {
            LOG.warn(ex.getMessage(), ex);
            response = context.toResponse(ex);
        }

        try {
            runResponseFilters(context, response);
            return response;
        } catch (final Exception ex) {
            // ContextResponseFilters should not throw exceptions, but they might
            LOG.error(ex.getMessage(), ex);
            return context.toResponse(ex);
        }
    }

    private MinijaxResourceMethod findRoute(final String httpMethod, final MinijaxUriInfo uriInfo) {
        for (final MinijaxResourceMethod rm : resourceMethods) {
            if (rm.tryMatch(httpMethod, uriInfo)) {
                return rm;
            }
        }

        if (httpMethod.equals(HEAD) || httpMethod.equals(OPTIONS)) {
            return findRoute(GET, uriInfo);
        }

        return null;
    }

    private void runRequestFilters(final MinijaxRequestContext context) throws IOException {
        for (final Class<? extends ContainerRequestFilter> filterClass : requestFilters) {
            final ContainerRequestFilter filter = context.getResource(filterClass);
            filter.filter(context);
        }
    }

    private void runResponseFilters(final MinijaxRequestContext context, final Response response) throws IOException {
        final ContainerResponseContext responseContext = (ContainerResponseContext) response;
        for (final Class<? extends ContainerResponseFilter> filterClass : responseFilters) {
            final ContainerResponseFilter filter = context.getResource(filterClass);
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
    private MinijaxProvider<?>[] getParamProviders(final Method method) {
        final Class<?>[] paramClasses = method.getParameterTypes();
        final Type[] paramTypes = method.getGenericParameterTypes();
        final Annotation[][] annotations = method.getParameterAnnotations();
        final MinijaxProvider<?>[] result = new MinijaxProvider<?>[paramTypes.length];

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
}
