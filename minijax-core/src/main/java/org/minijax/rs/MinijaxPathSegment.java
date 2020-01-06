package org.minijax.rs;

import javax.ws.rs.core.MultivaluedMap;

class MinijaxPathSegment implements javax.ws.rs.core.PathSegment {
    private final String path;

    public MinijaxPathSegment(final String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public MultivaluedMap<String, String> getMatrixParameters() {
        throw new UnsupportedOperationException();
    }
}