package org.minijax.swagger.config;

import javax.servlet.ServletConfig;

import io.swagger.jaxrs.config.DefaultJaxrsConfig;

public class MinijaxSwaggerConfig extends DefaultJaxrsConfig {
    private static final long serialVersionUID = 1L;

    @Override
    public void init(final ServletConfig servletConfig) throws javax.servlet.ServletException {
        super.init(servletConfig);
    }
}
