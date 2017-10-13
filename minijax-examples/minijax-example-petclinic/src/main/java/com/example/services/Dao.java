package com.example.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.minijax.data.BaseDao;

import com.example.model.Owner;

public class Dao extends BaseDao {

    public Dao(final EntityManagerFactory emf) {
        super(emf);
    }

    public List<Owner> findOwners(final String name) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            return em.createNamedQuery("Owner.findByName", Owner.class)
                    .setParameter("name", "%" + name.toLowerCase() + "%")
                    .getResultList();

        } finally {
            closeQuietly(em);
        }
    }
}
