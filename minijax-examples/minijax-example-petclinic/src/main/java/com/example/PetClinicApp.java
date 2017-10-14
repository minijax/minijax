package com.example;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.minijax.Minijax;

import com.example.model.Owner;
import com.example.model.Pet;
import com.example.model.Vet;
import com.example.model.Visit;
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

            final String[][] pets = new String[][] {
                { "Leo", "2000-09-07", "cat", "George Franklin" },
                { "Basil", "2002-08-06", "hamster", "Betty Davis" },
                { "Rosy", "2001-04-17", "dog", "Eduardo Rodriquez" },
                { "Jewel", "2000-03-07", "dog", "Eduardo Rodriquez" },
                { "Iggy", "2000-11-30", "lizard", "Harold Davis" },
                { "George", "2000-01-20", "snake", "Peter McTavish" },
                { "Samantha", "1995-09-04", "cat", "Jean Coleman" },
                { "Max", "1995-09-04", "cat", "Jean Coleman" },
                { "Lucky", "1999-08-06", "bird", "Jeff Black" },
                { "Mulligan", "1997-02-24", "dog", "Maria Escobito" },
                { "Freddy", "2000-03-09", "bird", "David Schroeder" },
                { "Lucky", "2000-06-24", "dog", "Carlos Estaban" },
                { "Sly", "2002-06-08", "cat", "Carlos Estaban" }
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

            for (final String[] pet : pets) {
                final List<Owner> ownersList = dao.findOwners(pet[3]);
                if (ownersList.isEmpty()) {
                    throw new RuntimeException("owner not found");
                }
                if (ownersList.size() > 1) {
                    throw new RuntimeException("multiple owners");
                }

                final Pet entity = new Pet();
                entity.setOwner(ownersList.get(0));
                entity.setName(pet[0]);
                entity.setBirthDate(pet[1]);
                entity.setPetType(pet[2]);
                entity.generateHandle();
                dao.create(entity);

                ownersList.get(0).getPets().add(entity);
                dao.update(ownersList.get(0));

                if (pet[0].equals("Samantha")) {
                    final Visit v1 = new Visit();
                    v1.setPet(entity);
                    v1.setName("Rabies shot");
                    v1.setDate(LocalDate.of(2010, 3, 4));
                    v1.generateHandle();
                    dao.create(v1);

                    final Visit v4 = new Visit();
                    v4.setPet(entity);
                    v4.setName("Spayed");
                    v4.setDate(LocalDate.of(2008, 9, 4));
                    v4.generateHandle();
                    dao.create(v4);

                    entity.getVisits().add(v1);
                    entity.getVisits().add(v4);
                    dao.update(entity);
                }

                if (pet[0].equals("Max")) {
                    final Visit v2 = new Visit();
                    v2.setPet(entity);
                    v2.setName("Rabies shot");
                    v2.setDate(LocalDate.of(2011, 3, 4));
                    v2.generateHandle();
                    dao.create(v2);

                    final Visit v3 = new Visit();
                    v3.setPet(entity);
                    v3.setName("Neutered");
                    v3.setDate(LocalDate.of(2009, 6, 4));
                    v3.generateHandle();
                    dao.create(v3);

                    entity.getVisits().add(v2);
                    entity.getVisits().add(v3);
                    dao.update(entity);
                }
            }
        }

        new Minijax()
                .register(new DefaultMustacheFactory(), MustacheFactory.class)
                .register(dao)
                .packages("com.example")
                .run(8081);
    }
}
