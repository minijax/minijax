
package org.minijax.rs;

import java.io.Closeable;
import java.io.InputStream;

import javax.ws.rs.core.Form;

import org.minijax.rs.multipart.Part;

/**
 * The Form class represents a HTTP form submission.
 */
public interface MinijaxForm extends Closeable {


    /**
     * Returns a string value or null if not found.
     *
     * @param name The field key name.
     * @return The field value.
     */
    String getString(final String name);


    InputStream getInputStream(String name);


    Part getPart(String name);


    Form asForm();

}
