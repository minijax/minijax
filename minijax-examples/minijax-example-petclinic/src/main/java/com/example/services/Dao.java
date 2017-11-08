package com.example.services;

import java.util.List;

import org.minijax.db.DefaultBaseDao;

import com.example.model.Owner;

public class Dao extends DefaultBaseDao {

    public List<Owner> findOwners(final String name) {
        return em.createNamedQuery("Owner.findByName", Owner.class)
                .setParameter("name", "%" + name.toLowerCase() + "%")
                .getResultList();
    }
}
