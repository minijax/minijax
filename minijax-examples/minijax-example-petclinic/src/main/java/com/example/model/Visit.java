package com.example.model;

import java.time.Instant;

import javax.persistence.Cacheable;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.minijax.entity.BaseEntity;
import org.minijax.entity.InstantAdapter;
import org.minijax.entity.InstantConverter;

@Entity
@Cacheable
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Visit extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String description;

    @Convert(converter = InstantConverter.class)
    @XmlJavaTypeAdapter(InstantAdapter.class)
    @SuppressWarnings("squid:S3437")
    private Instant date;

    @ManyToOne
    private Pet pet;

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(final Instant date) {
        this.date = date;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(final Pet pet) {
        this.pet = pet;
    }

    public String getUrl() {
        return "/visits/" + getId();
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}

