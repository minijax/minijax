package org.minijax.entity;

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
