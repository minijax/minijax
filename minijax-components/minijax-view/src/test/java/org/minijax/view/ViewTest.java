package org.minijax.view;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.minijax.view.View.AlertType;

class ViewTest {

    @Test
    void testConstructor() {
        final View page = new View("foo", "bar");
        assertEquals("foo", page.getTemplateName());
        assertEquals("bar", page.getModel().get("title"));
    }

    @Test
    void testGettersSetters() {
        final View page = new View("foo", "bar");
        page.addDangerAlert("this is an error");
        page.addInfoAlert("this is info");
        page.addWarningAlert("this is a warning");
        page.addSuccessAlert("this is a success");

        assertEquals(AlertType.DANGER, page.getAlerts().get(0).getAlertType());
        assertEquals("this is an error", page.getAlerts().get(0).getMessage());
        assertEquals("alert alert-danger", page.getAlerts().get(0).getStyle());

        assertEquals(AlertType.INFO, page.getAlerts().get(1).getAlertType());
        assertEquals("this is info", page.getAlerts().get(1).getMessage());
        assertEquals("alert alert-info", page.getAlerts().get(1).getStyle());

        assertEquals(AlertType.WARNING, page.getAlerts().get(2).getAlertType());
        assertEquals("this is a warning", page.getAlerts().get(2).getMessage());
        assertEquals("alert alert-warning", page.getAlerts().get(2).getStyle());

        assertEquals(AlertType.SUCCESS, page.getAlerts().get(3).getAlertType());
        assertEquals("this is a success", page.getAlerts().get(3).getMessage());
        assertEquals("alert alert-success", page.getAlerts().get(3).getStyle());
    }
}
