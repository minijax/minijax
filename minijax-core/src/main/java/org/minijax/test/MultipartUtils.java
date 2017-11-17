package org.minijax.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedMap;

class MultipartUtils {

    MultipartUtils() {
        throw new UnsupportedOperationException();
    }

    public static InputStream serializeMultipartForm(final Form form) {
        final String boundary = "------Boundary" + UUID.randomUUID().toString();

        final StringBuilder b = new StringBuilder();

        final MultivaluedMap<String, String> map = form.asMap();

        for (final Entry<String, List<String>> entry : map.entrySet()) {
            for (final String value : entry.getValue()) {
                b.append(boundary);
                b.append("\nContent-Disposition: form-data; name=\"");
                b.append(entry.getKey());
                b.append("\"\n\n");
                b.append(value);
                b.append("\n");
            }
        }

        b.append(boundary);
        return new ByteArrayInputStream(b.toString().getBytes(StandardCharsets.UTF_8));
    }
}
