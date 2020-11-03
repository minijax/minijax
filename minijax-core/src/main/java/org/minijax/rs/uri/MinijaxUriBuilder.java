package org.minijax.rs.uri;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

import jakarta.ws.rs.core.UriBuilder;

import org.minijax.rs.util.UrlUtils;

/**
 * The MinijaxUriBuilder class is the Minijax implementation of the UriBuilder interface.
 */
public class MinijaxUriBuilder extends UriBuilder {
    private final StringBuilder pathBuilder;
    private final StringBuilder queryBuilder;
    private String scheme;
    private String userInfo;
    private String host;
    private String port;
    private String fragment;

    public MinijaxUriBuilder() {
        pathBuilder = new StringBuilder();
        queryBuilder = new StringBuilder();
    }

    private MinijaxUriBuilder(final MinijaxUriBuilder other) {
        this();
        scheme = other.scheme;
        userInfo = other.userInfo;
        host = other.host;
        port = other.port;
        pathBuilder.append(other.pathBuilder.toString());
        queryBuilder.append(other.queryBuilder.toString());
        fragment = other.fragment;
    }

    @Override
    @SuppressWarnings({ "CloneDoesntCallSuperClone", "squid:S2975", "squid:S1182" })
    public MinijaxUriBuilder clone() {
        return new MinijaxUriBuilder(this);
    }

    @Override
    public MinijaxUriBuilder uri(final URI uri) {
        new Parser().parse(uri.toString());
        return this;
    }

    @Override
    public MinijaxUriBuilder uri(final String uriTemplate) {
        new Parser().parse(uriTemplate);
        return this;
    }

    @Override
    public MinijaxUriBuilder scheme(final String scheme) {
        this.scheme = scheme;
        return this;
    }

    @Override
    public MinijaxUriBuilder schemeSpecificPart(final String ssp) {
        new Parser().parse(ssp);
        return this;
    }

    @Override
    public MinijaxUriBuilder userInfo(final String userInfo) {
        this.userInfo = userInfo;
        return this;
    }

    @Override
    public MinijaxUriBuilder host(final String host) {
        this.host = host;
        return this;
    }

    @Override
    public MinijaxUriBuilder port(final int port) {
        this.port = Integer.toString(port);
        return this;
    }

    @Override
    public MinijaxUriBuilder replacePath(final String path) {
        pathBuilder.setLength(0);
        pathBuilder.append(path);
        return this;
    }

    @Override
    public MinijaxUriBuilder path(final String path) {
        if (pathBuilder.length() > 0 && pathBuilder.charAt(pathBuilder.length() - 1) != '/') {
            pathBuilder.append('/');
        }
        pathBuilder.append(path);
        return this;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public MinijaxUriBuilder path(final Class resource) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public MinijaxUriBuilder path(final Class resource, final String method) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxUriBuilder path(final Method method) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxUriBuilder segment(final String... segments) {
        for (final String segment : segments) {
            path(segment);
        }
        return this;
    }

    @Override
    public MinijaxUriBuilder replaceMatrix(final String matrix) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxUriBuilder matrixParam(final String name, final Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxUriBuilder replaceMatrixParam(final String name, final Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MinijaxUriBuilder replaceQuery(final String query) {
        queryBuilder.setLength(0);
        queryBuilder.append(query);
        return this;
    }

    @Override
    public MinijaxUriBuilder queryParam(final String name, final Object... values) {
        for (final Object value : values) {
            if (queryBuilder.length() > 0) {
                queryBuilder.append('&');
            }
            queryBuilder.append(UrlUtils.urlEncode(name, true, false));
            queryBuilder.append('=');
            queryBuilder.append(UrlUtils.urlEncode(value.toString(), true, false));
        }
        return this;
    }

    @Override
    public MinijaxUriBuilder replaceQueryParam(final String name, final Object... values) {
        queryBuilder.setLength(0);
        return queryParam(name, values);
    }

    @Override
    public MinijaxUriBuilder fragment(final String fragment) {
        this.fragment = fragment;
        return this;
    }

    @Override
    public MinijaxUriBuilder resolveTemplate(final String name, final Object value) {
        return resolveTemplate(name, value, true);
    }

    @Override
    public MinijaxUriBuilder resolveTemplate(final String name, final Object value, final boolean encodeSlashInPath) {
        uri(resolveTemplateImpl(Collections.singletonMap(name, value), s -> UrlUtils.urlEncode(s, true, !encodeSlashInPath)));
        return this;
    }

    @Override
    public MinijaxUriBuilder resolveTemplateFromEncoded(final String name, final Object value) {
        uri(resolveTemplateImpl(Collections.singletonMap(name, value), s -> s));
        return this;
    }

    @Override
    public MinijaxUriBuilder resolveTemplates(final Map<String, Object> templateValues) {
        return resolveTemplates(templateValues, true);
    }

    @Override
    public MinijaxUriBuilder resolveTemplates(final Map<String, Object> templateValues, final boolean encodeSlashInPath) {
        uri(resolveTemplateImpl(templateValues, s -> UrlUtils.urlEncode(s, true, !encodeSlashInPath)));
        return this;
    }

    @Override
    public MinijaxUriBuilder resolveTemplatesFromEncoded(final Map<String, Object> templateValues) {
        uri(resolveTemplateImpl(templateValues, s -> s));
        return this;
    }

    @Override
    public URI buildFromMap(final Map<String, ?> values) {
        return buildFromMap(values, true);
    }

    @Override
    public URI buildFromMap(final Map<String, ?> values, final boolean encodeSlashInPath) {
        return URI.create(resolveTemplateImpl(values, s -> UrlUtils.urlEncode(s, false, !encodeSlashInPath)));
    }

    @Override
    public URI buildFromEncodedMap(final Map<String, ?> values) {
        return URI.create(resolveTemplateImpl(values, s -> s));
    }

    @Override
    public URI build(final Object... values) {
        return buildFromMap(new ImplicitTemplateMap(values));
    }

    @Override
    public URI build(final Object[] values, final boolean encodeSlashInPath) {
        return buildFromMap(new ImplicitTemplateMap(values), encodeSlashInPath);
    }

    @Override
    public URI buildFromEncoded(final Object... values) {
        return buildFromEncodedMap(new ImplicitTemplateMap(values));
    }

    @Override
    public String toTemplate() {
        final StringBuilder sb = new StringBuilder();
        boolean haveAuthority = false;

        if (scheme != null) {
            sb.append(scheme).append("://");
        }

        if (userInfo != null && !userInfo.isEmpty()) {
            sb.append(userInfo).append('@');
        }

        if (host != null) {
            sb.append(host);
            haveAuthority = true;
        }

        if (port != null) {
            sb.append(':').append(port);
            haveAuthority = true;
        }

        if (pathBuilder.length() > 0) {
            if (haveAuthority && pathBuilder.charAt(0) != '/') {
                sb.append("/");
            }
            sb.append(pathBuilder.toString());
        }

        if (queryBuilder.length() > 0) {
            sb.append('?').append(queryBuilder.toString());
        }

        if (fragment != null && !fragment.isEmpty()) {
            sb.append('#').append(fragment);
        }

        return sb.toString();
    }

    @FunctionalInterface
    private interface EncodePredicate {
        String encode(String str);
    }

    private String resolveTemplateImpl(final Map<String, ?> values, final EncodePredicate encoder) {
        final String template = toTemplate();
        final StringBuilder result = new StringBuilder();
        final StringBuilder templateName = new StringBuilder();
        int curlyDepth = 0;
        boolean colon = false;

        for (int i = 0; i < template.length(); i++) {
            final char c = template.charAt(i);

            if (c == '{') {
                curlyDepth++;
                colon = false;
                templateName.setLength(0);

            } else if (curlyDepth > 0 && c == '}') {
                curlyDepth--;
                if (curlyDepth == 0) {
                    result.append(encoder.encode(values.get(templateName.toString()).toString()));
                }

            } else if (curlyDepth > 0 && c == ':') {
                colon = true;

            } else if (curlyDepth > 0) {
                if (!colon) {
                    templateName.append(c);
                }

            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    /**
     * The Parser class is a "template aware" URI parser.
     */
    private class Parser {

        public MinijaxUriBuilder parse(final String str) {
            final String[] components = split(str, "://");

            if (components.length == 2) {
                scheme = components[0];
                parseSchemeSpecificPart(components[1]);
            } else {
                parseSchemeSpecificPart(components[0]);
            }

            return null;
        }

        private void parseSchemeSpecificPart(final String str) {
            final String[] components1 = split(str, "/");
            if (components1.length == 2) {
                parsePrePath(components1[0]);
                parsePath(components1[1]);
            } else {
                final String[] components2 = split(str, "?");
                if (components2.length == 2) {
                    parsePrePath(components2[0]);
                    parseQuery(components2[1]);
                } else {
                    final String[] components3 = split(components2[0], "#");
                    if (components3.length == 2) {
                        parsePrePath(components3[0]);
                        fragment = components3[1];
                    } else {
                        parsePrePath(components3[0]);
                    }
                }
            }
        }

        private void parsePrePath(final String str) {
            final String[] components = split(str, "@");
            if (components.length == 2) {
                parseUserInfo(components[0]);
                parseAuthority(components[1]);
            } else {
                parseAuthority(components[0]);
            }
        }

        private void parseUserInfo(final String str) {
            userInfo = str;
        }

        private void parseAuthority(final String str) {
            final String[] components = split(str, ":");
            if (components.length == 2) {
                host = components[0];
                port = components[1];
            } else {
                host = components[0];
            }
        }

        private void parsePath(final String str) {
            final String[] components1 = split(str, "?");
            if (components1.length == 2) {
                replacePath(components1[0]);
                parseQuery(components1[1]);
            } else {
                final String[] components2 = split(components1[0], "#");
                if (components2.length == 2) {
                    replacePath(components2[0]);
                    fragment = components2[1];
                } else {
                    replacePath(components2[0]);
                }
            }
        }

        private void parseQuery(final String str) {
            final String[] components = split(str, "#");
            if (components.length == 2) {
                replaceQuery(components[0]);
                fragment = components[1];
            } else {
                replaceQuery(components[0]);
            }
        }

        private String[] split(final String str, final String delimeter) {
            int curlyDepth = 0;
            int squareDepth = 0;

            for (int i = 0; i < str.length(); i++) {
                final char c = str.charAt(i);

                if (c == '{') {
                    curlyDepth++;

                } else if (c == '}') {
                    curlyDepth--;

                } else if (c == '[') {
                    squareDepth++;

                } else if (c == ']') {
                    squareDepth--;

                } else if (curlyDepth == 0 && squareDepth == 0 && str.startsWith(delimeter, i)) {
                    if (delimeter.equals("/")) {
                        return new String[] { str.substring(0, i), str.substring(i) };
                    } else {
                        return new String[] { str.substring(0, i), str.substring(i + delimeter.length()) };
                    }
                }
            }

            return new String[] { str };
        }
    }
}
