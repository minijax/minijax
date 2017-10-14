package org.minijax.mustache;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;

public class MinijaxMustacheFeature implements Feature {

    @Override
    public boolean configure(final FeatureContext context) {
        context.register(new DefaultMustacheFactory(), MustacheFactory.class);
        context.register(MinijaxMustacheWriter.class);
        context.register(MinijaxMustacheExceptionMapper.class);
        return true;
    }
}
