
package org.minijax.rs;

import java.io.InputStream;

import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MultivaluedMap;

import org.minijax.rs.multipart.Part;
import org.minijax.rs.util.UrlUtils;

/**
 * The Form class represents a HTTP form submission.
 */
public class MinijaxUrlEncodedForm implements MinijaxForm {
    private final MultivaluedMap<String, String> values;

    /**
     * Creates a new form.
     *
     * @param encodedForm A URL-encoded form.
     */
    public MinijaxUrlEncodedForm(final String encodedForm) {
        this(UrlUtils.urlDecodeMultivaluedParams(encodedForm));
    }

    public MinijaxUrlEncodedForm(final MultivaluedMap<String, String> values) {
        this.values = values;
    }

    /**
     * Returns a string value or null if not found.
     *
     * @param name The field key name.
     * @return The field value.
     */
    @Override
    public String getString(final String name) {
        return values.getFirst(name);
    }

    @Override
    public InputStream getInputStream(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Part getPart(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Form asForm() {
        return new Form(values);
    }

    @Override
    public void close() {
        // Nothing to do
    }
}
