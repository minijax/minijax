package org.minijax.util;

import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

public class MediaTypeClassMap<T> {
    private final Map<MediaType, List<Class<? extends T>>> map;

    public MediaTypeClassMap() {
        map = new HashMap<>();
    }

    public void add(final Class<T> c, final List<MediaType> mediaTypes) {
        if (mediaTypes.isEmpty()) {
            throw new IllegalArgumentException("Empty media types");
        }
        for (final MediaType mediaType : mediaTypes) {
            add(mediaType, c);
        }
    }

    private void add(final MediaType mediaType, final Class<T> t) {
        map.computeIfAbsent(mediaType, m -> new ArrayList<>()).add(t);
    }

    public List<Class<? extends T>> get(final MediaType mediaType) {
        return map.getOrDefault(mediaType, emptyList());
    }
}
