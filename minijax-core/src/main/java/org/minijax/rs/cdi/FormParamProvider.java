package org.minijax.rs.cdi;

import java.io.InputStream;
import java.lang.annotation.Annotation;

import jakarta.ws.rs.DefaultValue;

import org.minijax.rs.MinijaxForm;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.multipart.Part;

class FormParamProvider<T> extends AbstractParamProvider<T> {

    public FormParamProvider(final Class<T> type, final Annotation[] annotations, final String name, final DefaultValue defaultValue) {
        super(type, annotations, name, defaultValue);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(final Object obj) {
        final MinijaxForm form = ((MinijaxRequestContext) obj).getForm();

        if (this.type == InputStream.class) {
            // Special case for injecting InputStream
            return form == null ? null : (T) form.getInputStream(name);
        }

        if (this.type == Part.class) {
            // Special case for injecting multipart form element
            return form == null ? null : (T) form.getPart(name);
        }

        // Otherwise use the normal string conversion
        return super.get(obj);
    }

    @Override
    public String getStringValue(final MinijaxRequestContext ctx) {
        final MinijaxForm form = ctx.getForm();
        return form == null ? null : form.getString(name);
    }
}
