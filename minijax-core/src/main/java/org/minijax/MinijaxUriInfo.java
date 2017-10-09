package org.minijax;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriBuilder;

import org.minijax.util.UrlUtils;

public class MinijaxUriInfo implements javax.ws.rs.core.UriInfo {
    private final URI requestUri;
    private List<PathSegment> pathSegments;
    private MultivaluedMap<String, String> pathParameters;
    private MultivaluedMap<String, String> queryParameters;


    public MinijaxUriInfo(final URI requestUri) {
        this.requestUri = requestUri;
    }


    /**
     * Get the path of the current request relative to the base URI as a string.
     * All sequences of escaped octets are decoded, equivalent to
     * {@link #getPath(boolean) getPath(true)}.
     *
     * @return the relative URI path.
     * @throws java.lang.IllegalStateException
     *          if called outside the scope of
     *          a request.
     */
    @Override
    public String getPath() {
        return requestUri.getPath();
    }


    /**
     * Get the path of the current request relative to the base URI as a string.
     *
     * @param decode controls whether sequences of escaped octets are decoded
     *               ({@code true}) or not ({@code false}).
     * @return the relative URI path
     * @throws java.lang.IllegalStateException
     *          if called outside the scope of
     *          a request.
     */
    @Override
    public String getPath(final boolean decode) {
        return getPath();
    }


    /**
     * Get the path of the current request relative to the base URI as a
     * list of {@link MinijaxPathSegment}. This method is useful when the
     * path needs to be parsed, particularly when matrix parameters may be
     * present in the path. All sequences of escaped octets in path segments
     * and matrix parameter values are decoded,
     * equivalent to {@code getPathSegments(true)}.
     *
     * @return an unmodifiable list of {@link MinijaxPathSegment}. The matrix parameter
     *         map of each path segment is also unmodifiable.
     * @throws IllegalStateException if called outside the scope of a request
     * @see MinijaxPathSegment
     * @see <a href="http://www.w3.org/DesignIssues/MatrixURIs.html">Matrix URIs</a>
     */
    @Override
    public List<PathSegment> getPathSegments() {
        if (pathSegments == null) {
            final String[] paths = getPath().split("/");
            pathSegments = new ArrayList<>(paths.length);
            for (final String path : paths) {
                pathSegments.add(new MinijaxPathSegment(path));
            }
        }
        return pathSegments;
    }


    /**
     * Get the path of the current request relative to the base URI as a list of
     * {@link MinijaxPathSegment}. This method is useful when the path needs to be parsed,
     * particularly when matrix parameters may be present in the path.
     *
     * @param decode controls whether sequences of escaped octets in path segments
     *               and matrix parameter values are decoded ({@code true}) or not ({@code false}).
     * @return an unmodifiable list of {@link MinijaxPathSegment}. The matrix parameter
     *         map of each path segment is also unmodifiable.
     * @throws java.lang.IllegalStateException
     *          if called outside the scope of a request
     * @see MinijaxPathSegment
     * @see <a href="http://www.w3.org/DesignIssues/MatrixURIs.html">Matrix URIs</a>
     */
    @Override
    public List<PathSegment> getPathSegments(final boolean decode) {
        return getPathSegments();
    }


    /**
     * Get the absolute request URI including any query parameters.
     *
     * @return the absolute request URI
     * @throws java.lang.IllegalStateException
     *          if called outside the scope of a request
     */
    @Override
    public URI getRequestUri() {
        return requestUri;
    }


    /**
     * Get the absolute request URI in the form of a UriBuilder.
     *
     * @return a UriBuilder initialized with the absolute request URI.
     * @throws java.lang.IllegalStateException
     *          if called outside the scope of a
     *          request.
     */
    @Override
    public UriBuilder getRequestUriBuilder() {
        throw new IllegalStateException();
    }


    /**
     * Get the absolute path of the request. This includes everything preceding
     * the path (host, port etc) but excludes query parameters.
     * This is a shortcut for {@code uriInfo.getBaseUri().resolve(uriInfo.getPath(false))}.
     *
     * @return the absolute path of the request.
     * @throws java.lang.IllegalStateException
     *          if called outside the scope of a
     *          request.
     */
    @Override
    public URI getAbsolutePath() {
        return getBaseUri().resolve(getPath());
    }


    /**
     * Get the absolute path of the request in the form of a UriBuilder.
     * This includes everything preceding the path (host, port etc) but excludes
     * query parameters.
     *
     * @return a UriBuilder initialized with the absolute path of the request.
     * @throws java.lang.IllegalStateException
     *          if called outside the scope of a
     *          request.
     */
    @Override
    public UriBuilder getAbsolutePathBuilder() {
        throw new IllegalStateException();
    }


    /**
     * Get the base URI of the application. URIs of root resource classes
     * are all relative to this base URI.
     *
     * @return the base URI of the application.
     */
    @Override
    public URI getBaseUri() {
        //  See:  http://stackoverflow.com/a/23810111/2051724
        return URI.create(requestUri.getScheme() + "://" + requestUri.getAuthority());
    }


    /**
     * Get the base URI of the application in the form of a UriBuilder.
     *
     * @return a UriBuilder initialized with the base URI of the application.
     */
    @Override
    public UriBuilder getBaseUriBuilder() {
        throw new UnsupportedOperationException();
    }


    /**
     * Get the values of any embedded URI template parameters. All sequences of
     * escaped octets are decoded, equivalent to
     * {@link #getPathParameters(boolean) getPathParameters(true)}.
     *
     * @return an unmodifiable map of parameter names and values.
     * @throws java.lang.IllegalStateException
     *          if called outside the scope of a
     *          request.
     * @see javax.ws.rs.Path
     * @see javax.ws.rs.PathParam
     */
    @Override
    public MultivaluedMap<String, String> getPathParameters() {
        return pathParameters;
    }


    /**
     * Get the values of any embedded URI template parameters.
     *
     * @param decode controls whether sequences of escaped octets are decoded
     *               ({@code true}) or not ({@code false}).
     * @return an unmodifiable map of parameter names and values
     * @throws java.lang.IllegalStateException
     *          if called outside the scope of a
     *          request.
     * @see javax.ws.rs.Path
     * @see javax.ws.rs.PathParam
     */
    @Override
    public MultivaluedMap<String, String> getPathParameters(final boolean decode) {
        return getPathParameters();
    }


    public void setPathParameters(final MultivaluedMap<String, String> pathParameters) {
        this.pathParameters = pathParameters;
    }


    /**
     * Get the URI query parameters of the current request. The map keys are the
     * names of the query parameters with any escaped characters decoded. All sequences
     * of escaped octets in parameter names and values are decoded, equivalent to
     * {@link #getQueryParameters(boolean) getQueryParameters(true)}.
     *
     * @return an unmodifiable map of query parameter names and values.
     * @throws java.lang.IllegalStateException
     *          if called outside the scope of a
     *          request.
     */
    @Override
    public MultivaluedMap<String, String> getQueryParameters() {
        if (queryParameters == null) {
            queryParameters = UrlUtils.urlDecodeMultivaluedParams(requestUri.getQuery());
        }
        return queryParameters;
    }


    /**
     * Get the URI query parameters of the current request. The map keys are the
     * names of the query parameters with any escaped characters decoded.
     *
     * @param decode controls whether sequences of escaped octets in parameter
     *               names and values are decoded ({@code true}) or not ({@code false}).
     * @return an unmodifiable map of query parameter names and values.
     * @throws java.lang.IllegalStateException
     *          if called outside the scope of a
     *          request.
     */
    @Override
    public MultivaluedMap<String, String> getQueryParameters(final boolean decode) {
        return getQueryParameters();
    }


    /**
     * Get a read-only list of URIs for matched resources.
     *
     * Each entry is a relative URI that matched a resource class, a
     * sub-resource method or a sub-resource locator. All sequences of escaped
     * octets are decoded, equivalent to {@code getMatchedURIs(true)}.
     * Entries do not include query parameters but do include matrix parameters
     * if present in the request URI. Entries are ordered in reverse request
     * URI matching order, with the current resource URI first.  E.g. given the
     * following resource classes:
     *
     * <pre>&#064;Path("foo")
     * public class FooResource {
     *  &#064;GET
     *  public String getFoo() {...}
     *
     *  &#064;Path("bar")
     *  public BarResource getBarResource() {...}
     * }
     *
     * public class BarResource {
     *  &#064;GET
     *  public String getBar() {...}
     * }
     * </pre>
     *
     * <p>The values returned by this method based on request uri and where
     * the method is called from are:</p>
     *
     * <table border="1">
     * <tr>
     * <th>MinijaxRequest</th>
     * <th>Called from</th>
     * <th>Value(s)</th>
     * </tr>
     * <tr>
     * <td>GET /foo</td>
     * <td>FooResource.getFoo</td>
     * <td>foo</td>
     * </tr>
     * <tr>
     * <td>GET /foo/bar</td>
     * <td>FooResource.getBarResource</td>
     * <td>foo/bar, foo</td>
     * </tr>
     * <tr>
     * <td>GET /foo/bar</td>
     * <td>BarResource.getBar</td>
     * <td>foo/bar, foo</td>
     * </tr>
     * </table>
     *
     * In case the method is invoked prior to the request matching (e.g. from a
     * pre-matching filter), the method returns an empty list.
     *
     * @return a read-only list of URI paths for matched resources.
     */
    @Override
    public List<String> getMatchedURIs() {
        throw new UnsupportedOperationException();
    }


    /**
     * Get a read-only list of URIs for matched resources.
     *
     * Each entry is a relative URI that matched a resource class, a sub-resource
     * method or a sub-resource locator. Entries do not include query
     * parameters but do include matrix parameters if present in the request URI.
     * Entries are ordered in reverse request URI matching order, with the
     * current resource URI first. See {@link #getMatchedURIs()} for an
     * example.
     *
     * In case the method is invoked prior to the request matching (e.g. from a
     * pre-matching filter), the method returns an empty list.
     *
     * @param decode controls whether sequences of escaped octets are decoded
     *               ({@code true}) or not ({@code false}).
     * @return a read-only list of URI paths for matched resources.
     */
    @Override
    public List<String> getMatchedURIs(final boolean decode) {
        return getMatchedURIs();
    }


    /**
     * Get a read-only list of the currently matched resource class instances.
     *
     * Each entry is a resource class instance that matched the request URI
     * either directly or via a sub-resource method or a sub-resource locator.
     * Entries are ordered according to reverse request URI matching order,
     * with the current resource first. E.g. given the following resource
     * classes:
     *
     * <pre>&#064;Path("foo")
     * public class FooResource {
     *  &#064;GET
     *  public String getFoo() {...}
     *
     *  &#064;Path("bar")
     *  public BarResource getBarResource() {...}
     * }
     *
     * public class BarResource {
     *  &#064;GET
     *  public String getBar() {...}
     * }
     * </pre>
     *
     * <p>The values returned by this method based on request uri and where
     * the method is called from are:</p>
     *
     * <table border="1">
     * <tr>
     * <th>MinijaxRequest</th>
     * <th>Called from</th>
     * <th>Value(s)</th>
     * </tr>
     * <tr>
     * <td>GET /foo</td>
     * <td>FooResource.getFoo</td>
     * <td>FooResource</td>
     * </tr>
     * <tr>
     * <td>GET /foo/bar</td>
     * <td>FooResource.getBarResource</td>
     * <td>FooResource</td>
     * </tr>
     * <tr>
     * <td>GET /foo/bar</td>
     * <td>BarResource.getBar</td>
     * <td>BarResource, FooResource</td>
     * </tr>
     * </table>
     *
     * In case the method is invoked prior to the request matching (e.g. from a
     * pre-matching filter), the method returns an empty list.
     *
     * @return a read-only list of matched resource class instances.
     */
    @Override
    public List<Object> getMatchedResources() {
        throw new UnsupportedOperationException();
    }


    /**
     * Resolve a relative URI with respect to the base URI of the application.
     * The resolved URI returned by this method is normalized. If the supplied URI is
     * already resolved, it is just returned.
     *
     * @param uri URI to resolve against the base URI of the application.
     * @return newly resolved URI or supplied URI if already resolved.
     * @since 2.0
     */
    @Override
    public URI resolve(final URI uri) {
        throw new UnsupportedOperationException();
    }


    /**
     * <p>Relativize a URI with respect to the current request URI. Relativization
     * works as follows:
     * <ol>
     * <li>If the URI to relativize is already relative, it is first resolved using
     * {@link #resolve(java.net.URI)}.</li>
     * <li>The resulting URI is relativized with respect to the current request
     * URI. If the two URIs do not share a prefix, the URI computed in
     * step 1 is returned.</li>
     * </ol>
     * </p>
     *
     * <p>Examples (for base URI {@code http://example.com:8080/app/root/}):
     * <br/>
     * <br/><b>MinijaxRequest URI:</b> <tt>http://example.com:8080/app/root/a/b/c/resource.html</tt>
     * <br/><b>Supplied URI:</b> <tt>a/b/c/d/file.txt</tt>
     * <br/><b>Returned URI:</b> <tt>d/file.txt</tt>
     * <br/>
     * <br/><b>MinijaxRequest URI:</b> <tt>http://example.com:8080/app/root/a/b/c/resource.html</tt>
     * <br/><b>Supplied URI:</b> <tt>http://example2.com:9090/app2/root2/a/d/file.txt</tt>
     * <br/><b>Returned URI:</b> <tt>http://example2.com:9090/app2/root2/a/d/file.txt</tt>
     * </p>
     *
     * <p>In the second example, the supplied URI is returned given that it is absolute
     * and there is no common prefix between it and the request URI.</p>
     *
     * @param uri URI to relativize against the request URI.
     * @return newly relativized URI.
     * @throws java.lang.IllegalStateException if called outside the scope of a request.
     * @since 2.0
     */
    @Override
    public URI relativize(final URI uri) {
        throw new UnsupportedOperationException();
    }
}
