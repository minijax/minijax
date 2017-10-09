package org.minijax.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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


    public static List<MediaType> parseMediaTypes(final String[] values) {
        final List<MediaType> result = new ArrayList<>();

        for (final String str : values) {
            for (final String type : str.split(",")) {
                result.add(MediaType.valueOf(type.trim()));
            }
        }

        return result;
    }
}
