package org.minijax.view;

import java.util.HashMap;
import java.util.Map;

public class View {
    private final String templateName;
    private final Map<String, Object> model;

    public View(final String templateName) {
        this.templateName = templateName;
        model = new HashMap<>();
    }

    public String getTemplateName() {
        return templateName;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
