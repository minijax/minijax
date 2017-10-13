package com.example;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.minijax.Minijax;

import com.example.model.Owner;
import com.example.model.Vet;
import com.example.services.Dao;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;

public class PetClinicApp {

    public static void main(final String[] args) throws IOException {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("petclinic");
        final Dao dao = new Dao(emf);

        if (dao.countAll(Owner.class) == 0) {
            final String[][] owners = new String[][] {
                { "George Franklin", "110 W. Liberty St.", "Madison", "6085551023" },
                { "Betty Davis", "638 Cardinal Ave.", "Sun Prairie", "6085551749" },
                { "Eduardo Rodriquez", "2693 Commerce St.", "McFarland", "6085558763" },
                { "Harold Davis", "563 Friendly St.", "Windsor", "6085553198" },
                { "Peter McTavish", "2387 S. Fair Way", "Madison", "6085552765" },
                { "Jean Coleman", "105 N. Lake St.", "Monona", "6085552654" },
                { "Jeff Black", "1450 Oak Blvd.", "Monona", "6085555387" },
                { "Maria Escobito", "345 Maple St.", "Madison", "6085557683" },
                { "David Schroeder", "2749 Blackhawk Trail", "Madison", "6085559435" },
                { "Carlos Estaban", "2335 Independence La.", "Waunakee", "6085555487" }
            };

            final String[][] vets = new String[][] {
                { "James Carter", "none" },
                { "Helen Leary", "radiology" },
                { "Linda Douglas", "dentistry" },
                { "Rafael Ortega", "surgery" },
                { "Henry Stevens", "radiology" },
                { "Sharon Jenkins", "none" }
            };

            for (final String[] owner : owners) {
                final Owner entity = new Owner();
                entity.setName(owner[0]);
                entity.setAddress(owner[1]);
                entity.setCity(owner[2]);
                entity.setTelephone(owner[3]);
                entity.generateHandle();
                dao.create(entity);
            }

            for (final String[] vet : vets) {
                final Vet entity = new Vet();
                entity.setName(vet[0]);
                entity.setSpecialty(vet[1]);
                entity.generateHandle();
                dao.create(entity);
            }
        }

        new Minijax()
                .register(new DefaultMustacheFactory(), MustacheFactory.class)
                .register(dao)
                .packages("com.example")
                .run(8081);
    }
}
