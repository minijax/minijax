
package org.minijax.rs.multipart;

import static java.util.Collections.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.minijax.rs.MinijaxException;
import org.minijax.rs.MinijaxForm;

/**
 * The MinijaxMultipartForm class represents a multipart HTTP form submission.
 */
public class Multipart implements MinijaxForm {
    private final MediaType contentType;
    private final Map<String, Part> values;

    /**
     * Creates an empty multipart form.
     */
    public Multipart() {
        this(new MediaType("multipart", "form-data", singletonMap("boundary", createBoundary())));
    }

    public Multipart(final MediaType contentType) {
        this.contentType = contentType;
        values = new HashMap<>();
    }

    public MediaType getContentType() {
        return contentType;
    }

    /**
     * Adds a new string value.
     *
     * @param name The string field name.
     * @param value The string value.
     */
    public Multipart param(final String name, final String value) {
        values.put(name, new StringPart(name, value));
        return this;
    }

    /**
     * Adds a new file value.
     *
     * @param name The file field name.
     * @param filename The submitted file name.
     * @param inputStream The file contents.
     */
    public Multipart param(final String name, final String filename, final InputStream inputStream) {
        values.put(name, new FilePart(name, filename, inputStream));
        return this;
    }

    /**
     * Returns the parts.
     *
     * @return A collection of all multipart form parts.
     */
    public Collection<Part> getParts() {
        return Collections.unmodifiableCollection(values.values());
    }

    /**
     * Returns a string value or null if not found.
     *
     * @param name The field key name.
     * @return The field value.
     */
    @Override
    public String getString(final String name) {
        final Part part = values.get(name);
        return part == null ? null : part.getValue();
    }

    @Override
    public InputStream getInputStream(final String name) {
        try {
            final Part part = values.get(name);
            return part == null ? null : part.getInputStream();
        } catch (final IOException ex) {
            throw new MinijaxException(ex);
        }
    }

    @Override
    public Part getPart(final String name) {
        return values.get(name);
    }

    @Override
    public Form asForm() {
        final MultivaluedMap<String, String> map = new MultivaluedHashMap<>();

        for (final Part part : values.values()) {
            if (part instanceof StringPart) {
                map.add(part.getName(), part.getValue());
            }
        }

        return new Form(map);
    }

    @Override
    public void close() throws IOException {
        // Nothing to do
    }

    @SuppressWarnings("squid:S2095")
    public static Multipart read(final MediaType contentType, final int contentLength, final InputStream inputStream) throws IOException {
        final FileUpload fileUpload = new FileUpload();
        fileUpload.setFileItemFactory(new DiskFileItemFactory());

        final List<FileItem> fileItems;

        try {
            fileItems = fileUpload.parseRequest(new UploadRequest(contentType, contentLength, inputStream));
        } catch (final FileUploadException ex) {
            throw new IOException("Error parsing form: " + ex.getMessage(), ex);
        }

        final Multipart result = new Multipart(contentType);

        for (final FileItem fileItem : fileItems) {
            if (fileItem.isFormField()) {
                result.param(fileItem.getFieldName(), fileItem.getString());
            } else {
                result.param(fileItem.getFieldName(), fileItem.getName(), fileItem.getInputStream());
            }
        }

        return result;
    }

    private static String createBoundary() {
        return "Boundary" + UUID.randomUUID().toString().replace("-", "");
    }

    private static class UploadRequest implements org.apache.commons.fileupload.RequestContext {
        private final MediaType contentType;
        private final int contentLength;
        private final InputStream inputStream;

        public UploadRequest(final MediaType contentType, final int contentLength, final InputStream inputStream) {
            this.contentType = contentType;
            this.contentLength = contentLength;
            this.inputStream = inputStream;
        }

        @Override
        public String getCharacterEncoding() {
            return null;
        }

        @Override
        public String getContentType() {
            return contentType.toString();
        }

        @Override
        public int getContentLength() {
            return contentLength;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return inputStream;
        }
    }
}
