package org.minijax.persistence.testmodel;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private int id;

    private String name;

    @OneToMany
    private final Set<User> following;

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
}
