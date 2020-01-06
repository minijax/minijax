package org.minijax.rs.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtils {


    LocaleUtils() {
        throw new UnsupportedOperationException();
    }


    /**
     * Parses an "Accept-Languages" HTTP header as a list of <code>Locale</code> objects.
     *
     * Example: "en-ca,en;q=0.8,en-us;q=0.6,de-de;q=0.4,de;q=0.2";
     *
     * @param header The "Accepted-Languages" HTTP header.
     * @return The list of locales.
     */
    public static List<Locale> parseAcceptLanguage(final String header) {
        if (header == null || header.isEmpty()) {
            return Collections.emptyList();
        }

        final List<Locale> result = new ArrayList<>();
        for (final String str : header.split(",")) {
            final String[] arr = str.trim().replace("-", "_").split(";");

            final Locale locale;
            final String[] l = arr[0].split("_");
            switch (l.length) {
            case 2:
                locale = new Locale(l[0], l[1]);
                break;
            case 3:
                locale = new Locale(l[0], l[1], l[2]);
                break;
            default:
                locale = new Locale(l[0]);
                break;
            }

            result.add(locale);
        }

        return result;
    }
}
