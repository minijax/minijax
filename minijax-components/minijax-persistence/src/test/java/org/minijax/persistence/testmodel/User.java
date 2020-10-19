package org.minijax.persistence.testmodel;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;

@Entity
@NamedQuery(name="User.findByName", query="SELECT u FROM User u WHERE u.name = :name")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private int id;

    private String name;

    @OneToMany
    private final Set<User> following;

    @Embedded
    private Address address;

    public User() {
        following = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Set<User> getFollowing() {
        return following;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }
}
