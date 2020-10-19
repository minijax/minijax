package org.minijax.rs.delegates;

import java.util.Map;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

import org.minijax.commons.CopyOnWriteMap;
import org.minijax.rs.util.UrlUtils;

class MinijaxMediaTypeDelegate implements HeaderDelegate<MediaType> {
    private static final Map<String, MediaType> TO_MEDIATYPE_CACHE = new CopyOnWriteMap<>();
    private static final Map<MediaType, String> TO_STRING_CACHE = new CopyOnWriteMap<>();

    @Override
    public MediaType fromString(final String value) {
        if (value == null) {
            return null;
        }
        return TO_MEDIATYPE_CACHE.computeIfAbsent(value, MinijaxMediaTypeDelegate::fromStringImpl);
    }

    @Override
    public String toString(final MediaType value) {
        if (value == null) {
            return null;
        }
        return TO_STRING_CACHE.computeIfAbsent(value, MinijaxMediaTypeDelegate::toStringImpl);
    }

    private static MediaType fromStringImpl(final String value) {
        final String[] a1 = value.split(";\\s*", 2);
        final String[] a2 = a1[0].split("/", 2);
        final String type = a2[0].isEmpty() ? "*" : a2[0];
        final String subtype = a2.length == 2 ? a2[1] : null;
        final Map<String, String> parameters = a1.length == 2 ? UrlUtils.urlDecodeParams(a1[1]) : null;
        return new MediaType(type, subtype, parameters);
    }

    private static String toStringImpl(final MediaType value) {
        final StringBuilder b = new StringBuilder();
        b.append(value.getType());

        if (!value.getSubtype().equals(MediaType.MEDIA_TYPE_WILDCARD)) {
            b.append("/");
            b.append(value.getSubtype());
        }

        if (!value.getParameters().isEmpty()) {
            b.append(";");
            b.append(UrlUtils.urlEncodeParams(value.getParameters()));
        }

        return b.toString();
    }
}
