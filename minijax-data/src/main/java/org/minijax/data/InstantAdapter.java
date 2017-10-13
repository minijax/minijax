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

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * The InstantAdapter class implements the JAXB adapter from
 * Java Instant to ISO-8601 formatted string.
 */
public class InstantAdapter extends XmlAdapter<String, Instant> {

    @Override
    public String marshal(final Instant dateTime) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.toString();
    }


    @Override
    public Instant unmarshal(final String str) {
        if (str == null) {
            return null;
        }

        if (str.endsWith("Z")) {
            return Instant.parse(str);
        } else {
            return OffsetDateTime.parse(str, DateTimeFormatter.ISO_DATE_TIME).toInstant();
        }
    }
}
