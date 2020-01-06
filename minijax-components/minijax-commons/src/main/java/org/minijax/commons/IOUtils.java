package org.minijax.commons;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class IOUtils {

    IOUtils() {
        throw new UnsupportedOperationException();
    }

    public static byte[] toByteArray(final InputStream input) throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public static InputStream toInputStream(final String str, final Charset charset) {
        return new ByteArrayInputStream(str.getBytes(charset));
    }

    public static String toString(final InputStream input, final Charset charset) throws IOException {
        return new String(toByteArray(input), charset);
    }

    public static void copy(final InputStream input, final OutputStream output)
            throws IOException {

        final byte[] buffer = new byte[1024];
        int len;
        while ((len = input.read(buffer)) != -1) {
            output.write(buffer, 0, len);
        }
    }
}
