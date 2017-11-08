package org.minijax.db;

import java.net.URI;
import java.security.Principal;
import java.util.Collections;
import java.util.List;

/**
 * The NamedEntity class is a base class for web entities with names.
 */
public interface NamedEntity extends BaseEntity, Principal {

    public String getHandle();

    @Override
    public String getName();

    public Avatar getAvatar();

    public void setAvatar(Avatar avatar);

    public URI getUri();


    /**
     * Sorts a list of ID objects by name (ascending).
     *
     * @param list The list of named entities (modified in place).
     */
    public static <T extends NamedEntity> void sortByName(final List<T> list) {
        Collections.sort(list, (o1, o2) -> o1.getName().compareTo(o2.getName()));
    }
}
