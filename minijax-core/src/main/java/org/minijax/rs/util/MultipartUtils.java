package org.minijax.rs.util;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import org.minijax.commons.IOUtils;
import org.minijax.rs.multipart.Multipart;
import org.minijax.rs.multipart.Part;

public class MultipartUtils {

    MultipartUtils() {
        throw new UnsupportedOperationException();
    }

    public static InputStream serializeMultipartForm(final Multipart form) throws IOException {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            serializeMultipartForm(form, outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        }
    }

    public static void serializeMultipartForm(final Multipart form, final OutputStream outputStream) throws IOException {
        final String boundary = form.getContentType().getParameters().get("boundary");
        try (final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            for (final Part part : form.getParts()) {
                writer.write("--");
                writer.write(boundary);
                writer.write("\r\n");

                if (part.getSubmittedFileName() != null) {
                    addFilePart(writer, part);
                } else {
                    addStringPart(writer, part);
                }
            }

            writer.write("--");
            writer.write(boundary);
            writer.write("--");
        }
    }

    private static void addFilePart(final BufferedWriter writer, final Part part) throws IOException {
        writer.write("Content-Disposition: form-data; name=\"");
        writer.write(part.getName());
        writer.write("\"; filename=\"");
        writer.write(part.getSubmittedFileName());
        writer.write("\"\r\n\r\n");
        writer.write(IOUtils.toString(part.getInputStream(), StandardCharsets.UTF_8));
        writer.write("\r\n");
    }

    private static void addStringPart(final BufferedWriter writer, final Part part) throws IOException {
        writer.write("Content-Disposition: form-data; name=\"");
        writer.write(part.getName());
        writer.write("\"\r\n\r\n");
        writer.write(IOUtils.toString(part.getInputStream(), StandardCharsets.UTF_8));
        writer.write("\r\n");
    }
}
