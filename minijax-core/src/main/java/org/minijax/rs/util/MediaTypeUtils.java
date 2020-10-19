package org.minijax.rs.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

public class MediaTypeUtils {

    MediaTypeUtils() {
        throw new UnsupportedOperationException();
    }

    public static List<MediaType> parseMediaTypes(final Consumes consumes) {
        return consumes == null ? Collections.emptyList() : parseMediaTypes(consumes.value());
    }

    public static List<MediaType> parseMediaTypes(final Produces produces) {
        return produces == null ? Collections.emptyList() : parseMediaTypes(produces.value());
    }

    private static List<MediaType> parseMediaTypes(final String[] values) {
        final List<MediaType> result = new ArrayList<>();

        for (final String str : values) {
            result.addAll(parseMediaTypes(str));
        }

        return result;
    }

    public static List<MediaType> parseMediaTypes(final String str) {
        final List<MediaType> result = new ArrayList<>();

        if (str != null) {
            for (final String acceptType : str.split(",\\s*")) {
                result.add(MediaType.valueOf(acceptType));
            }
        }

        return result;
    }
}
