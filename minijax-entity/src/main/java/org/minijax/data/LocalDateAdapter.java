package org.minijax.data;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * The InstantAdapter class implements the JAXB adapter from
 * Java LocalDate to ISO-8601 formatted string.
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    @Override
    public String marshal(final LocalDate dateTime) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.toString();
    }


    @Override
    public LocalDate unmarshal(final String str) {
        if (str == null) {
            return null;
        }

        return LocalDate.parse(str);
    }
}
