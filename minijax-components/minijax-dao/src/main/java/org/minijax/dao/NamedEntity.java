package org.minijax.dao;

import java.net.URI;
import java.security.Principal;
import java.util.Collections;
import java.util.List;

/**
 * The NamedEntity class is a base class for web entities with names.
 */
public interface NamedEntity extends BaseEntity, Principal {

    String getHandle();

    @Override
    String getName();

    void setName(String name);

    Avatar getAvatar();

    void setAvatar(Avatar avatar);

    default URI getUri() {
        return null;
    }

    /**
     * Sorts a list of ID objects by name (ascending).
     *
     * @param list The list of named entities (modified in place).
     */
    static <T extends NamedEntity> void sortByName(final List<T> list) {
        Collections.sort(list, (o1, o2) -> o1.getName().compareTo(o2.getName()));
    }
}
