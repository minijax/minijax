
package org.minijax.multipart;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.minijax.MinijaxForm;
import org.minijax.util.ExceptionUtils;
import org.minijax.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The MinijaxMultipartForm class represents a multipart HTTP form submission.
 */
public class Multipart implements MinijaxForm {
    private static final Logger LOG = LoggerFactory.getLogger(Multipart.class);
    private final Map<String, Part> values;

    /**
     * Creates an empty multipart form.
     */
    public Multipart() {
        values = new HashMap<>();
    }

    /**
     * Creates a form with all of the provided parts.
     *
     * @param parts The multipart form parts.
     */
    public Multipart(final Collection<Part> parts) {
        this();
        for (final Part part : parts) {
            values.put(part.getName(), part);
        }
    }

    /**
     * Adds a new string value.
     *
     * @param name The string field name.
     * @param value The string value.
     */
    public Multipart param(final String name, final String value) {
        values.put(name, new Part(name, value));
        return this;
    }

    /**
     * Adds a new file value.
     *
     * @param name The file field name.
     * @param value The file value.
     */
    public Multipart param(final String name, final File value) {
        values.put(name, new Part(name, value));
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
        try {
            final InputStream inputStream = getInputStream(name);
            return inputStream == null ? null : IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (final IOException ex) {
            throw ExceptionUtils.toWebAppException(ex);
        }
    }

    @Override
    public InputStream getInputStream(final String name) {
        try {
            final Part part = values.get(name);
            return part == null ? null : part.getInputStream();
        } catch (final IOException ex) {
            throw ExceptionUtils.toWebAppException(ex);
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
            if (part.getSubmittedFileName() != null) {
                continue;
            }
            try {
                map.add(part.getName(), IOUtils.toString(part.getInputStream(), StandardCharsets.UTF_8));
            } catch (final IOException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        }

        return new Form(map);
    }

    @Override
    public void close() throws IOException {
        // Nothing to do
    }
}
