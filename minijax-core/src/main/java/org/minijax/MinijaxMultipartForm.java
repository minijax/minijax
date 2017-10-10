
package org.minijax;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Part;
import javax.ws.rs.core.Form;

import org.minijax.util.ExceptionUtils;
import org.minijax.util.IOUtils;

/**
 * The Form class represents a HTTP form submission.
 */
public class MinijaxMultipartForm implements MinijaxForm {
    private final Map<String, Part> values;

    /**
     * Creates an empty form.
     */
    public MinijaxMultipartForm() {
        this(null);
    }

    /**
     * Creates a form with all of the provided parts.
     *
     * @param parts The multipart form parts.
     */
    public MinijaxMultipartForm(final Collection<Part> parts) {
        values = new HashMap<>();
        if (parts != null) {
            for (final Part part : parts) {
                values.put(part.getName(), part);
            }
        }
    }

    /**
     * Adds a value to the form.
     *
     * @param name The field key name.
     * @param part The field value.
     */
    public void put(final String name, final Part part) {
        values.put(name, part);
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
            return IOUtils.toString(getInputStream(name), StandardCharsets.UTF_8);
        } catch (final IOException ex) {
            throw ExceptionUtils.toWebAppException(ex);
        }
    }

    @Override
    public InputStream getInputStream(final String name) {
        try {
            return values.get(name).getInputStream();
        } catch (final IOException ex) {
            throw ExceptionUtils.toWebAppException(ex);
        }
    }

    @Override
    public Form asForm() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() throws IOException {
        for (final Part part : values.values()) {
            part.delete();
        }
    }
}
