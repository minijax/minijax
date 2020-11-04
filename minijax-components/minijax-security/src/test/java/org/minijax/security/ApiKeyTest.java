package org.minijax.security;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.UUID;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.minijax.commons.IdUtils;

class ApiKeyTest {
    private static Validator validator;

    @BeforeAll
    public static void setUpApiKeyTest() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testGettersSetters() {
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
    void testEquals() {
        final ApiKey m1 = new ApiKey();
        final ApiKey m2 = new ApiKey();
        final ApiKey m3 = new ApiKey();

        m2.setId(m1.getId());

        assertEquals(m1, m1);
        assertEquals(m1, m2);
        assertNotEquals(m1, m3);
        assertNotEquals(m1, null);
        assertNotEquals(m1, new Object());
    }

    @Test
    void testToJson() throws IOException {
        final ApiKey m = new ApiKey();
        final String json = m.toJson();

        assertNotNull(json);
        assertTrue(json.contains("\"id\":\"" + m.getId() + "\""));
    }

    @Test
    void testValidateSuccess() {
        final ApiKey apiKey = new ApiKey();
        apiKey.setValue("0123456789012345678901234567890123456789");
        apiKey.setUser(new User());
        assertTrue(validator.validate(apiKey).isEmpty());
    }

    @Test
    void testValidateNullCode() {
        final ApiKey apiKey = new ApiKey();
        apiKey.setValue(null);
        assertEquals(1, validator.validate(apiKey).size());
    }
}
