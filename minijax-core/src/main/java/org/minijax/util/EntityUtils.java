package org.minijax.util;

import static javax.ws.rs.core.MediaType.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;

import org.minijax.MinijaxApplication;
import org.minijax.multipart.Multipart;

public class EntityUtils {

    EntityUtils() {
        throw new UnsupportedOperationException();
    }

    public static InputStream convertToInputStream(final Entity<?> entity) throws IOException {
        if (entity == null || entity.getEntity() == null) {
            return null;
        }

        final Object obj = entity.getEntity();

        if (obj instanceof InputStream) {
            return (InputStream) obj;
        }

        if (obj instanceof String) {
            return IOUtils.toInputStream((String) obj, StandardCharsets.UTF_8);
        }

        if (obj instanceof Form) {
            return IOUtils.toInputStream(UrlUtils.urlEncodeMultivaluedParams(((Form) obj).asMap()), StandardCharsets.UTF_8);
        }

        if (obj instanceof Multipart) {
            return MultipartUtils.serializeMultipartForm((Multipart) obj);
        }

        if (entity.getMediaType() == APPLICATION_JSON_TYPE && OptionalClasses.JSON != null) {
            final MinijaxApplication application = MinijaxApplication.getApplication();
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            application.writeEntity(obj, entity.getMediaType(), outputStream);
            return IOUtils.toInputStream(outputStream.toString(), StandardCharsets.UTF_8);
        }

        throw new UnsupportedOperationException("Unknown entity type: " + obj.getClass());
    }
}
