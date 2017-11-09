
package org.minijax;

import java.io.InputStream;

import javax.servlet.http.Part;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedMap;

import org.minijax.util.UrlUtils;

/**
 * The Form class represents a HTTP form submission.
 */
class MinijaxUrlEncodedForm implements MinijaxForm {
    private final MultivaluedMap<String, String> values;

    /**
     * Creates a new form.
     *
     * @param encodedForm A URL-encoded form.
     */
    public MinijaxUrlEncodedForm(final String encodedForm) {
        this(UrlUtils.urlDecodeMultivaluedParams(encodedForm));
    }

    private MinijaxUrlEncodedForm(final MultivaluedMap<String, String> values) {
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
