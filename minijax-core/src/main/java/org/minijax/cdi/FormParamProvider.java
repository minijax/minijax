package org.minijax.cdi;

import java.io.InputStream;

import javax.inject.Provider;
import javax.ws.rs.DefaultValue;

import org.minijax.MinijaxForm;
import org.minijax.MinijaxRequestContext;
import org.minijax.multipart.Part;

class FormParamProvider<T> implements Provider<T> {
    private final Key<T> key;

    public FormParamProvider(final Key<T> key) {
        this.key = key;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get() {
        final MinijaxRequestContext context = MinijaxRequestContext.getThreadLocal();
        final Class<?> c = key.getType();
        final MinijaxForm form = context.getForm();
        final String name = key.getName();

        if (c == InputStream.class) {
            return form == null ? null : (T) form.getInputStream(name);
        }

        if (c == Part.class) {
            return form == null ? null : (T) form.getPart(name);
        }

        String value = form == null ? null : form.getString(name);

        if (value == null) {
            final DefaultValue defaultValue = key.getDefaultValue();
            if (defaultValue != null) {
                value = defaultValue.value();
            }
        }

        return (T) context.getApplicationContext().convertParamToType(value, c, key.getAnnotations());
    }
}
