package org.minijax.util;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;

/**
 * URL encoder that is "template aware" for curly-brace template handling.
 */
public class UrlEncoder {
    private static final BitSet URI_WHITELIST_CHARS;
    private final byte[] byteArray;
    private final boolean ignoreTemplates;
    private final boolean ignoreSlashes;
    private final StringBuilder result;
    private int index;
    private int curlyDepth;

    static {
        URI_WHITELIST_CHARS = new BitSet(256);
        int i;
        for (i = 'a'; i <= 'z'; i++) {
            URI_WHITELIST_CHARS.set(i);
        }
        for (i = 'A'; i <= 'Z'; i++) {
            URI_WHITELIST_CHARS.set(i);
        }
        for (i = '0'; i <= '9'; i++) {
            URI_WHITELIST_CHARS.set(i);
        }
        URI_WHITELIST_CHARS.set('-');
        URI_WHITELIST_CHARS.set('_');
        URI_WHITELIST_CHARS.set('.');
        URI_WHITELIST_CHARS.set('*');
    }

    public UrlEncoder(final String str, final boolean ignoreTemplates, final boolean ignoreSlashes) {
        this.ignoreTemplates = ignoreTemplates;
        this.ignoreSlashes = ignoreSlashes;
        byteArray = str.getBytes(StandardCharsets.UTF_8);
        result = new StringBuilder();
    }

    public String encode() {
        while (index < byteArray.length) {
            encodeNext();
        }
        return result.toString();
    }

    private void encodeNext() {
        final int b = byteArray[index] & 0xFF;
        final char c = (char) b;
        if (ignoreTemplates && b == '{') {
            copyChar(c);
            curlyDepth++;
        } else if (curlyDepth > 0) {
            copyChar(c);
            if (b == '}') {
                curlyDepth--;
            }
        } else if (ignoreSlashes && b == '/') {
            copyChar(c);
        } else if (b < 256 && URI_WHITELIST_CHARS.get(b)) {
            copyChar(c);
        } else if (b >= 0xF0) {
            escapeBytes(index, 4);
        } else if (b >= 0xE0) {
            escapeBytes(index, 3);
        } else if (b >= 0xC0) {
            escapeBytes(index, 2);
        } else {
            escapeByte(b);
        }
    }

    private void copyChar(final char c) {
        result.append(c);
        index++;
    }

    private void escapeBytes(final int offset, final int length) {
        for (int i = 0; i < length; i++) {
            escapeByte(byteArray[offset + i]);
        }
    }

    private void escapeByte(final int b) {
        result.append('%');
        result.append(Character.forDigit((b >> 4) & 0x0F, 16));
        result.append(Character.forDigit(b & 0x0F, 16));
        index++;
    }
}
