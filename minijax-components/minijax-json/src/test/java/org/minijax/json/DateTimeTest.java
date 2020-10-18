package org.minijax.json;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.json.bind.Jsonb;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class DateTimeTest {
    private static final ZoneId PST = ZoneId.of("America/Los_Angeles");
    private Jsonb mapper;

    @Before
    public void setUp() {
        mapper = Json.getObjectMapper();
    }

    @Test
    public void testWriteLocalDate() throws IOException {
        assertEquals(
                "\"2017-10-31\"",
                mapper.toJson(LocalDate.of(2017, 10, 31)));
    }

    @Test
    public void testReadLocalDate() throws IOException {
        assertEquals(
                LocalDate.of(2017, 10, 31),
                mapper.fromJson("\"2017-10-31\"", LocalDate.class));
    }

    @Test
    @Ignore("Yasson appends the display name of the time zone")
    public void testWriteZonedDateTime() throws IOException {
        assertEquals(
                "\"2017-10-31T04:38:00-07:00\"",
                mapper.toJson(ZonedDateTime.of(2017, 10, 31, 4, 38, 0, 0, PST)));
    }

    @Test
    public void testReadZonedDateTime() throws IOException {
        assertEquals(
                ZonedDateTime.of(2017, 10, 31, 4, 38, 0, 0, PST).toInstant(),
                mapper.fromJson("\"2017-10-31T04:38:00-07:00\"", ZonedDateTime.class).toInstant());
    }

    @Test
    public void testWriteInstant() throws IOException {
        assertEquals(
                "\"2017-10-31T11:38:00Z\"",
                mapper.toJson(ZonedDateTime.of(2017, 10, 31, 4, 38, 0, 0, PST).toInstant()));
    }

    @Test
    public void testReadInstant() throws IOException {
        assertEquals(
                ZonedDateTime.of(2017, 10, 31, 4, 38, 0, 0, PST).toInstant(),
                mapper.fromJson("\"2017-10-31T11:38:00Z\"", Instant.class));
    }
}
