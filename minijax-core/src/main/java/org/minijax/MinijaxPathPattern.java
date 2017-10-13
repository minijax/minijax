package org.minijax;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.ws.rs.PathParam;

public class MinijaxPathPattern {
    private static final String DOUBLE_REGEX = getDoubleRegex();
    private final String patternString;
    private final Pattern pattern;
    private final List<String> params;

    private MinijaxPathPattern(final String patternString, final List<String> params) {
        this.patternString = patternString;
        this.params = params;
        pattern = Pattern.compile(patternString);
    }

    public String getPatternString() {
        return patternString;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public List<String> getParams() {
        return params;
    }

    public static MinijaxPathPattern parse(final Method method, final String path) {
        final StringBuilder regexBuilder = new StringBuilder();
        final StringBuilder paramBuilder = new StringBuilder();
        final List<String> params = new ArrayList<>();
        int curlyDepth = 0;

        for (int i = 0; i < path.length(); i++) {
            final char c = path.charAt(i);

            if (c == '{') {
                if (curlyDepth > 0) {
                    paramBuilder.append(c);
                }
                curlyDepth++;
            } else if (c == '}') {
                curlyDepth--;
                if (curlyDepth < 0) {
                    throw new IllegalArgumentException("Unexpected '}' character at position " + i);
                } else if (curlyDepth > 0) {
                    paramBuilder.append(c);
                } else {
                    final String paramStr = paramBuilder.toString();
                    paramBuilder.setLength(0);

                    final int colonIndex = paramStr.indexOf(':');
                    if (colonIndex == 0) {
                        throw new IllegalArgumentException("Unexpected ':' character at position " + i);
                    }
                    final String paramName;
                    final String paramRegex;
                    if (colonIndex > 0) {
                        paramName = paramStr.substring(0, colonIndex).trim();
                        paramRegex = paramStr.substring(colonIndex + 1).trim();
                    } else {
                        paramName = paramStr.trim();
                        paramRegex = getDefaultPathParamRegex(method, paramName);
                    }
                    if (paramName.isEmpty()) {
                        throw new IllegalArgumentException("Parameter name cannot be empty");
                    }
                    params.add(paramName);
                    regexBuilder.append("(?<" + paramName + ">" + paramRegex + ")");
                }
            } else {
                if (curlyDepth > 0) {
                    paramBuilder.append(c);
                } else {
                    regexBuilder.append(c);
                }
            }
        }

        if (curlyDepth != 0) {
            throw new IllegalArgumentException("Unexpected end of input, missing '}'");
        }

        return new MinijaxPathPattern(regexBuilder.toString(), params);
    }


    private static String getDefaultPathParamRegex(final Method method, final String paramName) {
        final Class<?> c = getPathParamType(method, paramName);

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


    private static Class<?> getPathParamType(final Method method, final String paramName) {
        if (paramName.isEmpty()) {
            throw new IllegalArgumentException("Parameter name cannot be empty");
        }

        for (final Parameter param : method.getParameters()) {
            final PathParam annotation = param.getAnnotation(PathParam.class);
            if (annotation != null && annotation.value().equals(paramName)) {
                return param.getType();
            }
        }

        for (final Field field : method.getDeclaringClass().getDeclaredFields()) {
            final PathParam annotation = field.getAnnotation(PathParam.class);
            if (annotation != null && annotation.value().equals(paramName)) {
                return field.getType();
            }
        }

        throw new IllegalArgumentException("Missing parameter with name \"" + paramName + "\"");
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
