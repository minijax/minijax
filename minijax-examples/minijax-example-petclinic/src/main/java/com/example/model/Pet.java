package com.example.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.minijax.data.NamedEntity;

@Entity
@Cacheable
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Pet extends NamedEntity {
    private static final long serialVersionUID = 1L;
    private String petType;
    private String birthDate;

    public String getPetType() {
        return petType;
    }

    public void setPetType(final String petType) {
        this.petType = petType;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(final String birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String getUrl() {
        return "/pets/" + getId();
    }
}

