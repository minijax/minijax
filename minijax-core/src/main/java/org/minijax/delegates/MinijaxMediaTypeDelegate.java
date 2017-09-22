package org.minijax.delegates;

import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

import org.minijax.util.UrlUtils;

public class MinijaxMediaTypeDelegate implements HeaderDelegate<MediaType> {

    @Override
    public MediaType fromString(final String value) {
        if (value == null) {
            return null;
        }

        final String[] a1 = value.split(";", 2);
        final String[] a2 = a1[0].split("/", 2);
        final String type = a2[0];
        final String subtype = a2.length == 2 ? a2[1] : null;
        final Map<String, String> parameters = a1.length == 2 ? UrlUtils.urlDecodeParams(a1[1]) : null;
        return new MediaType(type, subtype, parameters);
    }

    @Override
    public String toString(final MediaType value) {
        final StringBuilder b = new StringBuilder();
        b.append(value.getType());

        if (value.getSubtype() != null) {
            b.append("/");
            b.append(value.getSubtype());
        }

        final String encodedParams = UrlUtils.urlEncodeParams(value.getParameters());
        if (encodedParams != null && !encodedParams.isEmpty()) {
            b.append("; ");
            b.append(UrlUtils.urlEncodeParams(value.getParameters()));
        }

        return b.toString();
    }
}
