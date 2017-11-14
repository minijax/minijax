package org.minijax.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class View {
    private final String templateName;
    private final Map<String, Object> model;

    public View(final String templateName) {
        this.templateName = templateName;
        model = new HashMap<>();
    }

    public View(final String templateName, final String title) {
        this(templateName);
        model.put("title", title);
    }

    public String getTemplateName() {
        return templateName;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    @SuppressWarnings("unchecked")
    public List<Alert> getAlerts() {
        return ((List<Alert>) model.computeIfAbsent("alerts", k -> new ArrayList<Alert>()));
    }

    public void addAlert(final Alert alert) {
        getAlerts().add(alert);
    }

    public void addSuccessAlert(final String msg) {
        addAlert(new Alert(AlertType.SUCCESS, msg));
    }

    public void addInfoAlert(final String msg) {
        addAlert(new Alert(AlertType.INFO, msg));
    }

    public void addWarningAlert(final String msg) {
        addAlert(new Alert(AlertType.WARNING, msg));
    }

    public void addDangerAlert(final String msg) {
        addAlert(new Alert(AlertType.DANGER, msg));
    }

    public enum AlertType {
        SUCCESS,
        INFO,
        WARNING,
        DANGER
    }

    public static class Alert {
        private final AlertType alertType;
        private final String message;

        public Alert(final AlertType alertType, final String message) {
            this.alertType = alertType;
            this.message = message;
        }

        public AlertType getAlertType() {
            return alertType;
        }

        public String getMessage() {
            return message;
        }

        public String getStyle() {
            return "alert alert-" + alertType.toString().toLowerCase();
        }
    }
}
