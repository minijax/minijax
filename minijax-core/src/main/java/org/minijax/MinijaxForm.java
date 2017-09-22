
package org.minijax;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.Form;

/**
 * The Form class represents a HTTP form submission.
 */
public interface MinijaxForm extends Closeable {


    /**
     * Returns a string value or null if not found.
     *
     * @param name The field key name.
     * @return The field value.
     * @throws IOException
     */
    public String getString(final String name);


    public InputStream getInputStream(String name);


    public Form asForm();

}
