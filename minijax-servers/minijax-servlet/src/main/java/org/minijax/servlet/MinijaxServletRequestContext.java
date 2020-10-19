package org.minijax.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

import org.minijax.rs.MinijaxApplicationContext;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.MinijaxUriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MinijaxServletRequestContext extends MinijaxRequestContext {
    private static final Logger LOG = LoggerFactory.getLogger(MinijaxServletRequestContext.class);
    private final HttpServletRequest request;
    private final MinijaxUriInfo uriInfo;
    private MinijaxServletHttpHeaders httpHeaders;
    private InputStream entityStream;

    public MinijaxServletRequestContext(final MinijaxApplicationContext application, final HttpServletRequest request) {
        super(application);
        this.request = request;

        final UriBuilder uriBuilder = UriBuilder.fromUri(request.getRequestURL().toString());
        final String queryString = request.getQueryString();
        if (queryString != null) {
            uriBuilder.replaceQuery(queryString);
        }
        uriInfo = new MinijaxUriInfo(uriBuilder.build(Collections.emptyMap()));
    }

    @Override
    public UriInfo getUriInfo() {
        return uriInfo;
    }

    @Override
    public String getMethod() {
        return request.getMethod();
    }

    @Override
    public HttpHeaders getHttpHeaders() {
         if (httpHeaders == null) {
             httpHeaders = new MinijaxServletHttpHeaders(request);
         }
        return httpHeaders;
    }

    @Override
    public InputStream getEntityStream() {
        if (entityStream == null) {
            try {
                entityStream = request.getInputStream();
            } catch (final IOException ex) {
                LOG.error("Error reading request input stream: {}", ex.getMessage(), ex);
                entityStream = new ByteArrayInputStream(new byte[] { 0 });
            }
        }
        return entityStream;
    }
}
