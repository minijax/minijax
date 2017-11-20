package org.minijax.validator;

import java.lang.annotation.Annotation;
import java.util.Locale;

import javax.validation.MessageInterpolator;
import javax.validation.ValidationException;

public class MinijaxMessageInterpolator implements MessageInterpolator {

    @Override
    public String interpolate(final String messageTemplate, final Context context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String interpolate(final String messageTemplate, final Context context, final Locale locale) {
        throw new UnsupportedOperationException();
    }

    public static String generateMessage(final String messageTemplate, final Annotation annotation) {
        final StringBuilder result = new StringBuilder();
        final StringBuilder expr = new StringBuilder();
        boolean inside = false;

        for (int i = 0; i < messageTemplate.length(); i++) {
            final char c = messageTemplate.charAt(i);

            if (c == '{') {
                inside = true;
            } else if (c == '}') {
                inside = false;
                result.append(evaluate(expr.toString(), annotation));
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
            return String.valueOf(annotation.annotationType().getMethod(expr.toString()).invoke(annotation));
        } catch (final ReflectiveOperationException ex) {
            throw new ValidationException(ex);
        }
    }
}