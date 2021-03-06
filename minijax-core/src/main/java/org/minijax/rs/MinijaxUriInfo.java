package org.minijax.rs;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.PathSegment;
import jakarta.ws.rs.core.UriBuilder;

import org.minijax.rs.util.UrlUtils;

public class MinijaxUriInfo implements jakarta.ws.rs.core.UriInfo {
    private final URI requestUri;
    private List<PathSegment> pathSegments;
    private MultivaluedMap<String, String> pathParameters;
    private MultivaluedMap<String, String> queryParameters;

    public MinijaxUriInfo(final URI requestUri) {
        this.requestUri = requestUri;
    }

    @Override
    public String getPath() {
        return requestUri.getPath();
    }

    @Override
    public String getPath(final boolean decode) {
        return getPath();
    }

    @Override
    public List<PathSegment> getPathSegments() {
        if (pathSegments == null) {
            pathSegments = Arrays.stream(getPath().replaceFirst("^/", "").split("/"))
                    .map(MinijaxPathSegment::new)
                    .collect(Collectors.toList());
        }
        return pathSegments;
    }

    @Override
    public List<PathSegment> getPathSegments(final boolean decode) {
        return getPathSegments();
    }

    @Override
    public URI getRequestUri() {
        return requestUri;
    }

    @Override
    public UriBuilder getRequestUriBuilder() {
        return UriBuilder.fromUri(requestUri);
    }

    @Override
    public URI getAbsolutePath() {
        return getBaseUri().resolve(getPath());
    }

    @Override
    public UriBuilder getAbsolutePathBuilder() {
        throw new IllegalStateException();
    }

    @Override
    public URI getBaseUri() {
        //  See:  http://stackoverflow.com/a/23810111/2051724
        return URI.create(requestUri.getScheme() + "://" + requestUri.getAuthority());
    }

    @Override
    public UriBuilder getBaseUriBuilder() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultivaluedMap<String, String> getPathParameters() {
        return pathParameters;
    }

    @Override
    public MultivaluedMap<String, String> getPathParameters(final boolean decode) {
        return getPathParameters();
    }

    public void setPathParameters(final MultivaluedMap<String, String> pathParameters) {
        this.pathParameters = pathParameters;
    }

    @Override
    public MultivaluedMap<String, String> getQueryParameters() {
        if (queryParameters == null) {
            queryParameters = UrlUtils.urlDecodeMultivaluedParams(requestUri.getQuery());
        }
        return queryParameters;
    }

    @Override
    public MultivaluedMap<String, String> getQueryParameters(final boolean decode) {
        return getQueryParameters();
    }

    @Override
    public List<String> getMatchedURIs() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getMatchedURIs(final boolean decode) {
        return getMatchedURIs();
    }

    @Override
    public List<Object> getMatchedResources() {
        throw new UnsupportedOperationException();
    }

    @Override
    public URI resolve(final URI uri) {
        throw new UnsupportedOperationException();
    }

    @Override
    public URI relativize(final URI uri) {
        throw new UnsupportedOperationException();
    }
}
