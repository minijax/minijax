package org.minijax.multipart;

import java.io.IOException;
import java.io.InputStream;

/**
 * The Part interface represents one form element in a multipart form.
 */
public interface Part {

    /**
     * Returns the form element name.
     * @return The form element name.
     */
    public String getName();

    /**
     * Returns the form value as a string.
     * @return The form value as a string.
     */
    public String getValue();

    /**
     * Returns the submitted file name (only applicable for file elements).
     * @return The submitted file name.
     */
    public String getSubmittedFileName();

    /**
     * Returns an input stream for consuming the value content.
     * @return An input stream for consuming the value content.
     * @throws IOException
     */
    public InputStream getInputStream() throws IOException;
}
