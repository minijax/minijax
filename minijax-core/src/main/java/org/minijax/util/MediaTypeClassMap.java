package org.minijax.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

public class MediaTypeClassMap<T> {
    private final List<Entry> entries;
    private final Map<MediaType, List<Class<? extends T>>> cache;

    public MediaTypeClassMap() {
        entries = new ArrayList<>();
        cache = new HashMap<>();
    }

    public void add(final Class<T> c, final List<MediaType> mediaTypes) {
        for (final MediaType mediaType : mediaTypes) {
            entries.add(new Entry(c, mediaType));
        }
        cache.clear();
    }

    public List<Class<? extends T>> get(final MediaType mediaType) {
        return cache.computeIfAbsent(mediaType, m -> getImpl(m));
    }

    private List<Class<? extends T>> getImpl(final MediaType mediaType) {
        final List<Class<? extends T>> result = new ArrayList<>();
        for (final Entry entry : entries) {
            if (entry.mediaType.isCompatible(mediaType)) {
                result.add(entry.c);
            }
        }
        return result;
    }

    public class Entry {
        private final Class<? extends T> c;
        private final MediaType mediaType;

        public Entry(final Class<? extends T> c, final MediaType mediaType) {
            this.c = c;
            this.mediaType = mediaType;
        }
    }
}
