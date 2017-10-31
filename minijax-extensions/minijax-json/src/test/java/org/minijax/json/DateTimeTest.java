package org.minijax.json;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DateTimeTest {
    private static final ZoneId PST = ZoneId.of("America/Los_Angeles");
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = MinijaxObjectMapper.getInstance();
    }

    @Test
    public void testWriteLocalDate() throws IOException {
        assertEquals(
                "\"2017-10-31\"",
                mapper.writeValueAsString(LocalDate.of(2017, 10, 31)));
    }

    @Test
    public void testReadLocalDate() throws IOException {
        assertEquals(
                LocalDate.of(2017, 10, 31),
                mapper.readValue("\"2017-10-31\"", LocalDate.class));
    }

    @Test
    public void testWriteZonedDateTime() throws IOException {
        assertEquals(
                "\"2017-10-31T04:38:00-07:00\"",
                mapper.writeValueAsString(ZonedDateTime.of(2017, 10, 31, 4, 38, 0, 0, PST)));
    }

    @Test
    public void testReadZonedDateTime() throws IOException {
        assertEquals(
                ZonedDateTime.of(2017, 10, 31, 4, 38, 0, 0, PST).toInstant(),
                mapper.readValue("\"2017-10-31T04:38:00-07:00\"", ZonedDateTime.class).toInstant());
    }

    @Test
    public void testWriteInstant() throws IOException {
        assertEquals(
                "\"2017-10-31T11:38:00Z\"",
                mapper.writeValueAsString(ZonedDateTime.of(2017, 10, 31, 4, 38, 0, 0, PST).toInstant()));
    }

    @Test
    public void testReadInstant() throws IOException {
        assertEquals(
                ZonedDateTime.of(2017, 10, 31, 4, 38, 0, 0, PST).toInstant(),
                mapper.readValue("\"2017-10-31T11:38:00Z\"", Instant.class));
    }
}
