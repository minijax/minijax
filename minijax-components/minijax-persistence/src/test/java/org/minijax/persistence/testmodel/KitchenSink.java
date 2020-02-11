package org.minijax.persistence.testmodel;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class KitchenSink {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    @Convert(converter = UuidConverter.class)
    private UUID id;

    @Convert(converter = InstantConverter.class)
    private Instant createdDateTime;

    @Convert(converter = InstantIntegerConverter.class)
    private Instant myIntTimestamp;

    @Convert(converter = UuidStringConverter.class)
    private UUID myStringUuid;

    private int myInt;
    private String myString;
    private byte[] myBytes;
    private Timestamp myTimestamp;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public Instant getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(final Instant createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Instant getMyIntTimestamp() {
        return myIntTimestamp;
    }

    public void setMyIntTimestamp(final Instant myIntTimestamp) {
        this.myIntTimestamp = myIntTimestamp;
    }

    public UUID getMyStringUuid() {
        return myStringUuid;
    }

    public void setMyStringUuid(final UUID myStringUuid) {
        this.myStringUuid = myStringUuid;
    }

    public int getMyInt() {
        return myInt;
    }

    public void setMyInt(final int myInt) {
        this.myInt = myInt;
    }

    public String getMyString() {
        return myString;
    }

    public void setMyString(final String myString) {
        this.myString = myString;
    }

    public byte[] getMyBytes() {
        return myBytes;
    }

    public void setMyBytes(final byte[] myBytes) {
        this.myBytes = myBytes;
    }

    public Timestamp getMyTimestamp() {
        return myTimestamp;
    }

    public void setMyTimestamp(final Timestamp myTimestamp) {
        this.myTimestamp = myTimestamp;
    }
}
