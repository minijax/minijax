package org.minijax;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.ws.rs.PathParam;

/**
 * The MinijaxPathPattern class is a rich representation of a parameterized URL path.
 */
public class MinijaxPathPattern {
    private static final String DOUBLE_REGEX = getDoubleRegex();
    private final Pattern pattern;
    private final List<String> params;

    private MinijaxPathPattern(final Pattern pattern, final List<String> params) {
        this.pattern = pattern;
        this.params = params;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getPatternString() {
        return pattern.pattern();
    }

    public List<String> getParams() {
        return params;
    }

    public static MinijaxPathPattern parse(final Method method, final String path) {
        return new Builder(method, path).build();
    }

    private static class Builder {
        private final Method method;
        private final String path;
        private final StringBuilder regexBuilder = new StringBuilder();
        private final StringBuilder paramBuilder = new StringBuilder();
        private final List<String> params = new ArrayList<>();
        private int curlyDepth = 0;
        private int index;

        public Builder(final Method method, final String path) {
            this.method = method;
            this.path = path;
        }

        public MinijaxPathPattern build() {
            for (index = 0; index < path.length(); index++) {
                final char c = path.charAt(index);
                if (c == '{') {
                    handleOpen();
                } else if (c == '}') {
                    handleClose();
                } else {
                    handleOther(c);
                }
            }

            if (curlyDepth != 0) {
                throw new IllegalArgumentException("Unexpected end of input, missing '}'");
            }

            return new MinijaxPathPattern(Pattern.compile(regexBuilder.toString()), params);
        }

        private void handleOpen() {
            if (curlyDepth > 0) {
                paramBuilder.append('{');
            }
            curlyDepth++;
        }

        private void handleClose() {
            curlyDepth--;
            if (curlyDepth < 0) {
                throw new IllegalArgumentException("Unexpected '}' character at index " + index);
            } else if (curlyDepth > 0) {
                paramBuilder.append('}');
            } else {
                addParam();
            }
        }

        private void handleOther(final char c) {
            if (curlyDepth > 0) {
                paramBuilder.append(c);
            } else {
                regexBuilder.append(c);
            }
        }

        private void addParam() {
            final String paramStr = paramBuilder.toString();
            paramBuilder.setLength(0);

            final int colonIndex = paramStr.indexOf(':');
            if (colonIndex == 0) {
                throw new IllegalArgumentException("Unexpected ':' character at index " + index);
            }

            final String paramName;
            final String paramRegex;
            if (colonIndex > 0) {
                paramName = paramStr.substring(0, colonIndex).trim();
                paramRegex = paramStr.substring(colonIndex + 1).trim();
            } else {
                paramName = paramStr.trim();
                paramRegex = getDefaultPathParamRegex(paramName);
            }

            if (paramName.isEmpty()) {
                throw new IllegalArgumentException("Parameter name cannot be empty");
            }

            params.add(paramName);
            regexBuilder.append("(?<" + paramName + ">" + paramRegex + ")");
        }

        private String getDefaultPathParamRegex(final String paramName) {
            final Class<?> c = getPathParamType(paramName);

            if (c == int.class || c == long.class || c == short.class) {
                return "-?[0-9]+";
            }

            if (c == double.class || c == float.class) {
                return DOUBLE_REGEX;
            }

            if (c == UUID.class) {
                return "[0-9a-f]{8}-?[0-9a-f]{4}-?[0-9a-f]{4}-?[0-9a-f]{4}-?[0-9a-f]{12}";
            }

            return "[^/]+";
        }

        private Class<?> getPathParamType(final String paramName) {
            if (paramName.isEmpty()) {
                throw new IllegalArgumentException("Parameter name cannot be empty");
            }

            for (final Parameter param : method.getParameters()) {
                final PathParam annotation = param.getAnnotation(PathParam.class);
                if (annotation != null && annotation.value().equals(paramName)) {
                    return param.getType();
                }
            }

            Class<?> currentClass = method.getDeclaringClass();
            while (currentClass != null) {
                for (final Field field : currentClass.getDeclaredFields()) {
                    final PathParam annotation = field.getAnnotation(PathParam.class);
                    if (annotation != null && annotation.value().equals(paramName)) {
                        return field.getType();
                    }
                }
                currentClass = currentClass.getSuperclass();
            }

            throw new IllegalArgumentException("Missing parameter with name \"" + paramName + "\"");
        }
    }

    /**
     * See: https://docs.oracle.com/javase/7/docs/api/java/lang/Double.html#valueOf(java.lang.String)
     */
    private static String getDoubleRegex() {
        final String Digits     = "(\\p{Digit}+)";
        final String HexDigits  = "(\\p{XDigit}+)";
        // an exponent is 'e' or 'E' followed by an optionally
        // signed decimal integer.
        final String Exp        = "[eE][+-]?"+Digits;
        return
            ("[\\x00-\\x20]*"+  // Optional leading "whitespace"
             "[+-]?(" + // Optional sign character
             "NaN|" +           // "NaN" string
             "Infinity|" +      // "Infinity" string

             // A decimal floating-point string representing a finite positive
             // number without a leading sign has at most five basic pieces:
             // Digits . Digits ExponentPart FloatTypeSuffix
             //
             // Since this method allows integer-only strings as input
             // in addition to strings of floating-point literals, the
             // two sub-patterns below are simplifications of the grammar
             // productions from section 3.10.2 of
             // The Java™ Language Specification.

             // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
             "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+

             // . Digits ExponentPart_opt FloatTypeSuffix_opt
             "(\\.("+Digits+")("+Exp+")?)|"+

             // Hexadecimal strings
             "((" +
              // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
              "(0[xX]" + HexDigits + "(\\.)?)|" +

              // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
              "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

              ")[pP][+-]?" + Digits + "))" +
             "[fFdD]?))" +
             "[\\x00-\\x20]*");// Optional trailing "whitespace"
    }
}
