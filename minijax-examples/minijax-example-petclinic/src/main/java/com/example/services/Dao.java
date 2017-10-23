package com.example.services;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

import org.minijax.data.BaseDao;

import com.example.model.Owner;

public class Dao extends BaseDao {

    @Inject
    public Dao(final EntityManagerFactory emf) {
        super(emf);
    }

    public List<Owner> findOwners(final String name) {
        return em.createNamedQuery("Owner.findByName", Owner.class)
                .setParameter("name", "%" + name.toLowerCase() + "%")
                .getResultList();
    }
}
