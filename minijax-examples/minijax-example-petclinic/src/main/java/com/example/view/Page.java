package com.example.view;

import java.util.HashMap;
import java.util.Map;

public class Page {
    private final String templateName;
    private final Map<String, Object> props;

    public Page(final String templateName) {
        this.templateName = templateName;
        props = new HashMap<>();
    }

    public String getTemplateName() {
        return templateName;
    }

    public Map<String, Object> getProps() {
        return props;
    }
}
