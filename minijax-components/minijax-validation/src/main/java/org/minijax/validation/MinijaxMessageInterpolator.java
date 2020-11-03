package org.minijax.validation;

import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.ResourceBundle;

import jakarta.validation.MessageInterpolator;
import jakarta.validation.ValidationException;

public class MinijaxMessageInterpolator implements MessageInterpolator {

    @Override
    public String interpolate(final String messageTemplate, final Context context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String interpolate(final String messageTemplate, final Context context, final Locale locale) {
        throw new UnsupportedOperationException();
    }

    public static String generateMessage(final String messageKey, final Annotation annotation, final Object invalidValue) {
        if (messageKey == null) {
            return null;
        }

        final String messageTemplate;
        if (messageKey.startsWith("{") && messageKey.endsWith("}")) {
            final ResourceBundle resourceBundle = ResourceBundle.getBundle("org.minijax.validator.ValidationMessages");
            messageTemplate = resourceBundle.getString(messageKey.substring(1, messageKey.length() - 1));
        } else {
            messageTemplate = messageKey;
        }

        final StringBuilder result = new StringBuilder();
        final StringBuilder expr = new StringBuilder();
        boolean inside = false;
        boolean dollar = false;

        for (int i = 0; i < messageTemplate.length(); i++) {
            final char c = messageTemplate.charAt(i);

            if (c == '$') {
                dollar = true;
            } else if (c == '{') {
                inside = true;
            } else if (c == '}') {
                if (dollar) {
                    result.append(invalidValue);
                } else {
                    result.append(evaluate(expr.toString(), annotation));
                }
                inside = false;
                dollar = false;
                expr.setLength(0);
            } else if (inside) {
                expr.append(c);
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    private static String evaluate(final String expr, final Annotation annotation) {
        try {
            return String.valueOf(annotation.annotationType().getMethod(expr).invoke(annotation));
        } catch (final ReflectiveOperationException ex) {
            throw new ValidationException(ex);
        }
    }
}
