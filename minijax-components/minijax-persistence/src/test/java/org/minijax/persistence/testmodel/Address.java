package org.minijax.persistence.testmodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Address class represents a standard mailing address.
 */
@Embeddable
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(length = 128)
    private String line1;

    @Column(length = 128)
    private String line2;

    @Column(length = 64)
    private String city;

    @Column(length = 16)
    private String state;

    @Column(length = 16)
    private String postalCode;

    public String getLine1() {
        return line1;
    }

    public void setLine1(final String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(final String line2) {
        this.line2 = line2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }
}
