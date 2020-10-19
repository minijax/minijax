package org.minijax.security;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.UUID;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.commons.IdUtils;

public class ApiKeyTest {
    private static Validator validator;

    @BeforeClass
    public static void setUpApiKeyTest() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void testGettersSetters() {
        final User user = new User();

        final UUID id = IdUtils.create();

        final ApiKey e = new ApiKey();
        e.setId(id);
        e.setName("bar");
        e.setUser(user);

        assertEquals(id, e.getId());
        assertEquals("bar", e.getName());
        assertEquals(user.getId(), e.getUserId());
    }

    @Test
    public void testEquals() {
        final ApiKey m1 = new ApiKey();
        final ApiKey m2 = new ApiKey();
        final ApiKey m3 = new ApiKey();

        m2.setId(m1.getId());

        assertTrue(m1.equals(m1));
        assertTrue(m1.equals(m2));
        assertFalse(m1.equals(m3));
        assertFalse(m1.equals(new Object()));
        assertFalse(m1.equals(null));
    }

    @Test
    public void testToJson() throws IOException {
        final ApiKey m = new ApiKey();
        final String json = m.toJson();

        assertNotNull(json);
        assertTrue(json.contains("\"id\":\"" + m.getId() + "\""));
    }

    @Test
    public void testValidateSuccess() {
        final ApiKey apiKey = new ApiKey();
        apiKey.setValue("0123456789012345678901234567890123456789");
        apiKey.setUser(new User());
        assertTrue(validator.validate(apiKey).isEmpty());
    }

    @Test
    public void testValidateNullCode() {
        final ApiKey apiKey = new ApiKey();
        apiKey.setValue(null);
        assertEquals(1, validator.validate(apiKey).size());
    }
}
