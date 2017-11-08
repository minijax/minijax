package org.minijax.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.Validate;
import org.eclipse.persistence.annotations.CacheIndex;

/**
 * The NamedEntity class is a base class for web entities with names.
 */
@MappedSuperclass
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("squid:S2160")
public abstract class DefaultNamedEntity extends DefaultBaseEntity implements NamedEntity {
    private static final long serialVersionUID = 1L;
    private static final String HANDLE_SPECIAL_CHARS = "!#$%&'()*+,/:;=?@[\\]^`{|}~";

    @Column(length = 32, unique = true)
    @CacheIndex
    private String handle;

    private String name;

    @Embedded
    private Avatar avatar;

    public DefaultNamedEntity() {
        super();
    }

    public DefaultNamedEntity(final String name) {
        setName(name);
    }

    @Override
    public String getHandle() {
        return handle;
    }

    public void setHandle(final String handle) {
        if (handle != null) {
            this.handle = handle.trim();
        } else {
            this.handle = null;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        if (name != null) {
            this.name = name.trim();
        } else {
            this.name = null;
        }
    }

    @Override
    public Avatar getAvatar() {
        return avatar;
    }

    @Override
    public void setAvatar(final Avatar avatar) {
        this.avatar = avatar;
    }


    public void generateHandle() {
        if (name == null) {
            handle = RandomStringUtils.randomAlphanumeric(16).toLowerCase();
        } else {
            handle = (name.replaceAll("[^A-Za-z0-9]", "") + "-" + RandomStringUtils.randomAlphanumeric(6)).toLowerCase();
        }
    }


    @Override
    public void validate() {
        super.validate();
        validateHandle(handle);
        validateName(name);
    }


    public static void validateHandle(final String handle) {
        if (handle == null) {
            // Null handles are ok
            return;
        }

        Validate.notEmpty(handle, "Handle must not be empty.");
        Validate.inclusiveBetween(1, 32, handle.length(), "Handle must be between 1 and 32 characters long");
        Validate.isTrue(handle.charAt(0) != '.', "Handle cannot start with a period");

        for (int i = 0; i < HANDLE_SPECIAL_CHARS.length(); i++) {
            final char c = HANDLE_SPECIAL_CHARS.charAt(i);
            Validate.isTrue(handle.indexOf(c) == -1, "Handle cannot contain any of the following special characters: %s", HANDLE_SPECIAL_CHARS);
        }
    }


    public static void validateName(final String name) {
        Validate.notEmpty(name, "Name must not be null or empty.");
        Validate.inclusiveBetween(1, 255, name.length(), "Name must be between 1 and 255 characters long");
    }
}
