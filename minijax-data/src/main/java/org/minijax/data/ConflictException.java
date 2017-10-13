/*
 * AJIBOT CONFIDENTIAL
 * __________________
 *
 *  2017 Ajibot Inc
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Ajibot Inc and its suppliers, if any.
 * The intellectual and technical concepts contained herein
 * are proprietary to Ajibot Inc and its suppliers and may
 * be covered by U.S. and Foreign Patents, patents in process,
 * and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this
 * material is strictly forbidden unless prior written
 * permission is obtained from Ajibot Inc.
 */
package org.minijax.data;

/**
 * The ConflictException represents a HTTP 409 error.
 */
public class ConflictException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public static final String DEFAULT_MESSAGE = "Conflict";
    private final String key;
    private final String value;

    public ConflictException() {
        super(DEFAULT_MESSAGE);
        key = null;
        value = null;
    }

    public ConflictException(final String key, final String value) {
        super("The " + key + " '" + value + "' already exists");
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
