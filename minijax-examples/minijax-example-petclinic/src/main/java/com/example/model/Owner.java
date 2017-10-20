package com.example.model;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.minijax.entity.BaseEntity;

@Entity
@Cacheable
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name = "Owner.findByName", query = "SELECT o FROM Owner o WHERE LOWER(o.name) LIKE :name AND o.deletedDateTime IS NULL")
public class Owner extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String name;
    private String address;
    private String city;
    private String telephone;

    @OneToMany(mappedBy = "owner")
    private List<Pet> pets;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(final String telephone) {
        this.telephone = telephone;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(final List<Pet> pets) {
        this.pets = pets;
    }

    public String getUrl() {
        return "/owners/" + getId();
    }
}

