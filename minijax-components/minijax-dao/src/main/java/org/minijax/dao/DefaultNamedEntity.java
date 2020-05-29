package org.minijax.dao;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

/**
 * The NamedEntity class is a base class for web entities with names.
 */
@MappedSuperclass
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "squid:S2160", "squid:S2637" })
public abstract class DefaultNamedEntity extends DefaultBaseEntity implements NamedEntity {
    private static final long serialVersionUID = 1L;
    public static final String HANDLE_SPECIAL_CHARS = "!#$%&'()*+,/:;=?@[\\]^`{|}~";
    public static final String HANDLE_SPECIAL_CHARS_REGEX = java.util.regex.Pattern.quote(HANDLE_SPECIAL_CHARS);
    public static final String HANDLE_REGEX = "[^\\." + HANDLE_SPECIAL_CHARS_REGEX + "][^" + HANDLE_SPECIAL_CHARS_REGEX + "]*";

    @Column(length = 32, unique = true)
    @Size(min = 1, max = 32)
    @Pattern(regexp = "[^\\.\\Q!#$%&'()*+,/:;=?@[\\]^`{|}~\\E][^\\Q!#$%&'()*+,/:;=?@[\\]^`{|}~\\E]*")
    private String handle;

    @NotNull
    @Size(min = 1, max = 128)
    private String name = "";

    @Embedded
    private Avatar avatar;

    protected DefaultNamedEntity() {
        super();
    }

    protected DefaultNamedEntity(final String name) {
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

    @Override
    public void setName(final String name) {
        if (name != null) {
            this.name = name.trim();
        } else {
            this.name = "";
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
        final String uuid = UUID.randomUUID().toString();
        if (name == null) {
            handle = uuid.substring(0, 16);
        } else {
            handle = (name.replaceAll("[^A-Za-z0-9]", "") + "-" + uuid.substring(0, 6)).toLowerCase();
        }
    }
}
