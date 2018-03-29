package org.minijax.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

import javax.servlet.http.Part;

import org.minijax.MinijaxMultipartForm;
import org.minijax.util.IOUtils;

class MultipartUtils {

    MultipartUtils() {
        throw new UnsupportedOperationException();
    }

    public static InputStream serializeMultipartForm(final MinijaxMultipartForm form) throws IOException {
        final String boundary = "------Boundary" + UUID.randomUUID().toString();

        final StringBuilder b = new StringBuilder();

        for (final Part part : form.getParts()) {
            b.append(boundary);

            if (part.getSubmittedFileName() != null) {
                addFilePart(b, part);
            } else {
                addStringPart(b, part);
            }
        }

        b.append(boundary);
        return new ByteArrayInputStream(b.toString().getBytes(StandardCharsets.UTF_8));
    }


    private static void addFilePart(final StringBuilder b, final Part part) throws IOException {
        b.append("\nContent-Disposition: form-data; name=\"");
        b.append(part.getName());
        b.append("\"; filename=\"");
        b.append(part.getSubmittedFileName());
        b.append("\"\nContent-Transfer-Encoding: base64\n\n");
        b.append(new String(Base64.getEncoder().encode(IOUtils.toByteArray(part.getInputStream()))));
        b.append("\n");
    }


    private static void addStringPart(final StringBuilder b, final Part part) throws IOException {
        b.append("\nContent-Disposition: form-data; name=\"");
        b.append(part.getName());
        b.append("\"\n\n");
        b.append(IOUtils.toString(part.getInputStream(), StandardCharsets.UTF_8));
        b.append("\n");
    }
}
