package org.minijax;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.DispatcherType;
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.minijax.cdi.MinijaxInjector;
import org.minijax.util.ClassPathScanner;
import org.minijax.util.ExceptionUtils;
import org.minijax.util.IOUtils;
import org.minijax.util.IdUtils;
import org.minijax.util.MediaTypeClassMap;
import org.minijax.util.MediaTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Minijax implements FeatureContext {
    private static final Logger LOG = LoggerFactory.getLogger(Minijax.class);
    private static final Class<?> webSocketUtilsClass = safeGetClass("org.minijax.websocket.MinijaxWebSocketUtils");
    private static final Class<Annotation> serverEndpoint = safeGetClass("javax.websocket.server.ServerEndpoint");
    private final MinijaxInjector injector;
    private final List<MinijaxStaticResource> staticResources;
    private final Set<Class<?>> classesScanned;
    private final List<MinijaxResourceMethod> resourceMethods;
    private final List<Class<?>> webSockets;
    private final List<Class<? extends ContainerRequestFilter>> requestFilters;
    private final List<Class<? extends ContainerResponseFilter>> responseFilters;
    private final MediaTypeClassMap<MessageBodyReader<?>> readers;
    private final MediaTypeClassMap<MessageBodyWriter<?>> writers;
    private final MediaTypeClassMap<ExceptionMapper<?>> exceptionMappers;
    private Class<? extends SecurityContext> securityContextClass;


    public Minijax() {
        injector = new MinijaxInjector();
        staticResources = new ArrayList<>();
        classesScanned = new HashSet<>();
        resourceMethods = new ArrayList<>();
        webSockets = new ArrayList<>();
        requestFilters = new ArrayList<>();
        responseFilters = new ArrayList<>();
        readers = new MediaTypeClassMap<>();
        writers = new MediaTypeClassMap<>();
        exceptionMappers = new MediaTypeClassMap<>();
    }


    @Override
    public Configuration getConfiguration() {
        throw new UnsupportedOperationException();
    }


    @Override
    public Minijax property(final String name, final Object value) {
        throw new UnsupportedOperationException();
    }


    @Override
    public Minijax register(final Class<?> componentClass) {
        registerImpl(componentClass);
        return this;
    }


    @Override
    public Minijax register(final Class<?> componentClass, final int priority) {
        registerImpl(componentClass);
        return this;
    }


    @Override
    public Minijax register(final Class<?> componentClass, final Class<?>... contracts) {
        for (final Class<?> contract : contracts) {
            injector.register(componentClass, contract);
        }
        registerImpl(componentClass);
        return this;
    }


    @Override
    public Minijax register(final Class<?> componentClass, final Map<Class<?>, Integer> contracts) {
        for (final Class<?> contract : contracts.keySet()) {
            injector.register(componentClass, contract);
        }
        registerImpl(componentClass);
        return this;
    }


    @Override
    public Minijax register(final Object component) {
        return this.register(component, component.getClass());
    }


    @Override
    public Minijax register(final Object component, final int priority) {
        return this.register(component, component.getClass());
    }


    @Override
    public Minijax register(final Object component, final Class<?>... contracts) {
        for (final Class<?> contract : contracts) {
            injector.register(component, contract);
        }
        return this;
    }


    @Override
    public Minijax register(final Object component, final Map<Class<?>, Integer> contracts) {
        for (final Class<?> contract : contracts.keySet()) {
            injector.register(component, contract);
        }
        return this;
    }


    public Minijax packages(final String... packageNames) {
        for (final String packageName : packageNames) {
            scanPackage(packageName);
        }
        return this;
    }


    public Minijax addStaticFile(final String resourceName) {
        final String pathSpec = String.format("/%s", resourceName);
        return addStaticFile(resourceName, pathSpec);
    }


    public Minijax addStaticFile(final String resourceName, final String pathSpec) {
        final String resourceUrl = Minijax.class.getClassLoader().getResource(resourceName).toExternalForm();
        return addStaticResource(resourceUrl, pathSpec);
    }


    public Minijax addStaticDirectory(final String resourceName) {
        final String resourceUrl = Minijax.class.getClassLoader().getResource(resourceName).toExternalForm();
        final String pathSpec = String.format("/%s/*", resourceName);
        return addStaticResource(resourceUrl, pathSpec);
    }


    public Minijax addStaticResource(final String resourceUrl, final String pathSpec) {
        staticResources.add(new MinijaxStaticResource(resourceUrl, pathSpec));
        return this;
    }


    public Minijax allowCors(final String urlPrefix) {
        register(MinijaxCorsFilter.class);
        get(MinijaxCorsFilter.class).addPathPrefix(urlPrefix);
        return this;
    }


    public void run(final int port) {
        try {
            final Server server = createServer(port);

            final ServletContextHandler context = new ServletContextHandler();
            context.setContextPath("/");
            context.addFilter(new FilterHolder(new MinijaxFilter(this)), "/*", EnumSet.of(DispatcherType.REQUEST));
            server.setHandler(context);

            // (1) WebSocket endpoints
            if (webSocketUtilsClass != null) {
                webSocketUtilsClass
                        .getMethod("init", Minijax.class, ServletContextHandler.class, List.class)
                        .invoke(null, this, context, webSockets);
            }

            // (2) Static resources
            for (final MinijaxStaticResource staticResource : staticResources) {
                final ServletHolder staticHolder = new ServletHolder(new DefaultServlet());
                staticHolder.setInitParameter("pathInfoOnly", "true");
                staticHolder.setInitParameter("resourceBase", staticResource.getResourceBase());
                context.addServlet(staticHolder, staticResource.getPathSpec());
            }

            // (3) Dynamic JAX-RS content
            final MinijaxServlet servlet = new MinijaxServlet(this);
            final ServletHolder servletHolder = new ServletHolder(servlet);
            servletHolder.getRegistration().setMultipartConfig(new MultipartConfigElement(""));
            context.addServlet(servletHolder, "/*");

            server.start();
            server.join();

        } catch (final Exception ex) {
            throw new MinijaxException(ex);
        }
    }


    /*
     * Private helpers
     */


    private void scanPackage(final String packageName) {
        try {
            for (final Class<?> c : ClassPathScanner.scan(packageName)) {
                registerImpl(c);
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
        registerReader(c);
        registerWriter(c);
        registerExceptionMapper(c);
        registerSecurityContext(c);
        classesScanned.add(c);
    }


    private void registerResourceMethods(final Class<?> c) {
        for (final Method method : c.getDeclaredMethods()) {
            for (final Annotation annotation : method.getAnnotations()) {
                final HttpMethod httpMethod = annotation.annotationType().getAnnotation(HttpMethod.class);
                if (httpMethod != null) {
                    resourceMethods.add(new MinijaxResourceMethod(httpMethod.value(), method));
                }
            }
        }
    }


    private void registerWebSockets(final Class<?> c) {
        if (serverEndpoint == null) {
            return;
        }
        final Annotation ws = c.getAnnotation(serverEndpoint);
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
            get(featureClass).configure(this);
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
    private void registerReader(final Class<?> c) {
        if (MessageBodyReader.class.isAssignableFrom(c)) {
            readers.add((Class<MessageBodyReader<?>>) c, MediaTypeUtils.parseMediaTypes(c.getAnnotation(Consumes.class)));
        }
    }


    @SuppressWarnings("unchecked")
    private void registerWriter(final Class<?> c) {
        if (MessageBodyWriter.class.isAssignableFrom(c)) {
            writers.add((Class<MessageBodyWriter<?>>) c, MediaTypeUtils.parseMediaTypes(c.getAnnotation(Produces.class)));
        }
    }


    @SuppressWarnings("unchecked")
    private void registerExceptionMapper(final Class<?> c) {
        if (ExceptionMapper.class.isAssignableFrom(c)) {
            exceptionMappers.add((Class<ExceptionMapper<?>>) c, MediaTypeUtils.parseMediaTypes(c.getAnnotation(Produces.class)));
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


    /**
     * Creates a new web server listening on the given port.
     *
     * Override this method in unit tests to mock server functionality.
     *
     * @param port The listening port.
     * @return A new web server.
     */
    protected Server createServer(final int port) {
        return new Server(port);
    }


    public Response handle(final MinijaxRequestContext context) {
        final MinijaxResourceMethod rm = findRoute(context.getMethod(), (MinijaxUriInfo) context.getUriInfo());
        if (rm == null) {
            return toResponse(context, new NotFoundException());
        }

        context.setResourceMethod(rm);

        try {
            if (securityContextClass != null) {
                context.setSecurityContext(get(securityContextClass));
            }

            runRequestFilters(context);
            checkSecurity(context);
            final Response response = toResponse(rm, invoke(context, rm.getMethod()));
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
            final ContainerRequestFilter filter = get(filterClass);
            filter.filter(context);
        }
    }


    private void runResponseFilters(final MinijaxRequestContext context, final Response response) throws IOException {
        final ContainerResponseContext responseContext = (ContainerResponseContext) response;
        for (final Class<? extends ContainerResponseFilter> filterClass : responseFilters) {
            final ContainerResponseFilter filter = get(filterClass);
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


    public Object invoke(final MinijaxRequestContext context, final Method method) throws IOException {
        final Object instance;
        if (Modifier.isStatic(method.getModifiers())) {
            instance = null;
        } else {
            instance = get(method.getDeclaringClass());
        }

        try {
            return method.invoke(instance, getArgs(context, method));
        } catch (final InvocationTargetException ex) {
            throw ExceptionUtils.toWebAppException(ex.getCause());
        } catch (IllegalAccessException | IllegalArgumentException ex) {
            throw new WebApplicationException(ex.getMessage(), ex);
        }
    }


    private Object[] getArgs(final MinijaxRequestContext context, final Executable executable) throws IOException {
        final Parameter[] parameters = executable.getParameters();
        final Annotation[][] annotations = executable.getParameterAnnotations();
        final Object[] args = new Object[parameters.length];

        // If constructor - any number of non-annotated args
        // If resource method - only one non-annoted arg, and only if @Consumes
        final Consumes consumes = executable.getAnnotation(Consumes.class);
        final List<MediaType> consumesTypes = MediaTypeUtils.parseMediaTypes(consumes);
        boolean consumed = false;

        for (int i = 0; i < parameters.length; i++) {
            if (annotations[i].length == 0 && consumes != null && consumesTypes.size() == 1 && context != null && !consumed) {
                args[i] = consumeEntity(parameters[i].getType(), context.getEntityStream(), consumesTypes.get(0));
                consumed = true;
            } else {
                args[i] = get(parameters[i].getType(), annotations[i]);
            }
        }
        return args;
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
            final List<Class<? extends ExceptionMapper<?>>> mappers = exceptionMappers.get(mediaType);
            if (!mappers.isEmpty()) {
                // Cast should not be necessary, but Eclipse chokes on it
                return ((ExceptionMapper) get(mappers.get(0))).toResponse(ex); // NOSONAR
            }
        }

        return ExceptionUtils.toWebAppException(ex).getResponse();
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    private MediaType findResponseType(
            final Object obj,
            final List<MediaType> produces) {

        final Class<?> objType = obj == null ? null : obj.getClass();

        for (final MediaType mediaType : produces) {
            for (final Class<? extends MessageBodyWriter<?>> writerClass : writers.get(mediaType)) {
                final MessageBodyWriter writer = get(writerClass);
                if (writer.isWriteable(objType, null, null, mediaType)) {
                    return mediaType;
                }
            }
        }

        return MediaType.TEXT_PLAIN_TYPE;
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void write(
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

        final Object obj = response.getEntity();
        final Class<?> objType = obj == null ? null : obj.getClass();
        final MediaType mediaType = response.getMediaType();
        if (mediaType != null) {
            servletResponse.setContentType(mediaType.toString());
        }

        final MessageBodyWriter writer = findWriter(obj, mediaType);
        if (writer != null) {
            writer.writeTo(obj, objType, null, null, mediaType, null, servletResponse.getOutputStream());
        } else if (obj != null) {
            if (obj instanceof InputStream) {
                IOUtils.copy((InputStream) obj, servletResponse.getOutputStream());
            } else {
                servletResponse.getWriter().println(obj.toString());
            }
        }
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    private MessageBodyWriter findWriter(
            final Object obj,
            final MediaType mediaType) {

        final Class<?> objType = obj == null ? null : obj.getClass();

        for (final Class<? extends MessageBodyWriter<?>> writerClass : writers.get(mediaType)) {
            final MessageBodyWriter writer = get(writerClass);
            if (writer.isWriteable(objType, null, null, mediaType)) {
                return writer;
            }
        }

        // Return "plain text writer"?
        return null;
    }


    public <T> T get(final Class<T> c) {
        return injector.get(c);
    }


    /**
     * Returns a resource instance.
     *
     * @param <T> The type of the result.
     * @param c The class type of the result.
     * @param annotations Annotations of the declaration (member, parameter, etc).
     * @return The resource instance.
     */
    public <T> T get(final Class<T> c, final Annotation[] annotations) {
        return injector.get(c, annotations);
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    private <T> T consumeEntity(final Class<T> c, final InputStream entityStream, final MediaType mediaType) throws IOException {
        final Type genericType = null;
        final Annotation[] annotations = null;
        final MultivaluedMap<String, String> httpHeaders = null;

        for (final Class<? extends MessageBodyReader<?>> readerClass : readers.get(mediaType)) {
            final MessageBodyReader reader = get(readerClass);
            if (reader.isReadable(c, genericType, annotations, mediaType)) {
                return (T) reader.readFrom(c, genericType, annotations, mediaType, httpHeaders, entityStream);
            }
        }

        // No reader, now what?
        // Try to convert InputStream -> String -> Type
        return convertStringToType(IOUtils.toString(entityStream, StandardCharsets.UTF_8), c);
    }


    @SuppressWarnings({ "unchecked" })
    public <T> T convertStringToType(final String str, final Class<T> c) {
        if (str == null) {
            return null;
        }

        if (c == String.class) {
            return (T) str;
        }

        if (c.isPrimitive()) {
            return convertStringToPrimitive(str, c);
        }

        if (c == UUID.class) {
            return (T) IdUtils.tryParse(str);
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


    @SuppressWarnings("unchecked")
    private static <T> Class<T> safeGetClass(final String className) {
        try {
            return (Class<T>) Class.forName(className);
        } catch (final ClassNotFoundException ex) {
            LOG.trace(ex.getMessage(), ex);
            return null;
        }
    }
}
