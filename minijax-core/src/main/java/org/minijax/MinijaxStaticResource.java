package org.minijax;

import static javax.ws.rs.HttpMethod.*;
import static javax.ws.rs.core.MediaType.*;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collections;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

class MinijaxStaticResource extends MinijaxResourceMethod {
    private static final CacheControl PUBLIC = CacheControl.valueOf("public, max-age=31536000");
    private final String baseResourceName;
    private final boolean directory;

    public MinijaxStaticResource(final String resourceName, final String path) {
        super(GET, null, null, path, Collections.emptyList(), null);
        baseResourceName = resourceName;
        directory = path.contains("{");
    }

    @Override
    Object invoke(final MinijaxRequestContext ctx) throws Exception {
        final String resourceName;

        if (directory) {
            resourceName = baseResourceName + "/" + ctx.getUriInfo().getPathParameters().getFirst("file");
        } else {
            resourceName = baseResourceName;
        }

        final URL resourceUrl = MinijaxStaticResource.class.getClassLoader().getResource(resourceName);
        if (resourceUrl == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        final File resourceFile = new File(resourceUrl.getFile());
        if (resourceFile.isDirectory()) {
            return Response.status(Status.NOT_FOUND).build();
        }

        final String contentType = Files.probeContentType(resourceFile.toPath());
        final MediaType mediaType = contentType == null ? APPLICATION_OCTET_STREAM_TYPE : MediaType.valueOf(contentType);
        return Response.ok(resourceUrl.openStream(), mediaType).cacheControl(PUBLIC).build();
    }
}
