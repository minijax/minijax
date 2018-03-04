package org.minijax.websocket;

import javax.websocket.DeploymentException;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

import org.minijax.MinijaxApplication;
import org.minijax.MinijaxException;
import org.minijax.MinijaxRequestContext;

import io.undertow.servlet.api.ClassIntrospecter;
import io.undertow.servlet.util.DefaultClassIntrospector;
import io.undertow.servlet.util.ImmediateInstanceHandle;
import io.undertow.util.PathTemplate;
import io.undertow.websockets.jsr.EncodingFactory;
import io.undertow.websockets.jsr.annotated.AnnotatedEndpointFactory;

class MinijaxWebSocketConfigurator extends io.undertow.websockets.jsr.DefaultContainerConfigurator {
    private final MinijaxApplication application;
    private final Class<?> annotatedClass;
    private final AnnotatedEndpointFactory annotatedEndpointFactory;


    public MinijaxWebSocketConfigurator(final MinijaxApplication application, final Class<?> endpoint) {
        this.application = application;
        this.annotatedClass = endpoint;

        final ServerEndpoint serverEndpoint = endpoint.getAnnotation(ServerEndpoint.class);
        final PathTemplate template = PathTemplate.create(serverEndpoint.value());
        final ClassIntrospecter classIntrospecter = DefaultClassIntrospector.INSTANCE;

        try {
            final EncodingFactory encodingFactory = EncodingFactory.createFactory(classIntrospecter, serverEndpoint.decoders(), serverEndpoint.encoders());
            annotatedEndpointFactory = AnnotatedEndpointFactory.create(endpoint, encodingFactory, template.getParameterNames());

        } catch (final DeploymentException ex) {
            throw new MinijaxException("Error creating websocket: " + ex.getMessage(), ex);
        }
    }


    @Override
    public void modifyHandshake(final ServerEndpointConfig sec, final HandshakeRequest request, final HandshakeResponse response) {
        MinijaxRequestContext.getThreadLocal().setUpgraded(true);
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> T getEndpointInstance(final Class<T> endpointClass) throws InstantiationException {
        try {
            final Object instance = application.getResource(this.annotatedClass);
            return (T) annotatedEndpointFactory.createInstance(new ImmediateInstanceHandle<>(instance));
        } catch (final Exception ex) {
            throw new InstantiationException(ex.getMessage());
        }
    }
}
