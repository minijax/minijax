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
public class Vet extends NamedEntity {
    private static final long serialVersionUID = 1L;
    private String specialty;

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(final String specialty) {
        this.specialty = specialty;
    }

    @Override
    public String getUrl() {
        return "/vets/" + getId();
    }
}

