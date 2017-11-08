/*
 * AJIBOT CONFIDENTIAL
 * __________________
 *
 *  2017 Ajibot Inc
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Ajibot Inc and its suppliers, if any.
 * The intellectual and technical concepts contained herein
 * are proprietary to Ajibot Inc and its suppliers and may
 * be covered by U.S. and Foreign Patents, patents in process,
 * and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this
 * material is strictly forbidden unless prior written
 * permission is obtained from Ajibot Inc.
 */
package org.minijax.security;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.Validate;
import org.eclipse.persistence.annotations.CacheIndex;
import org.minijax.entity.DefaultBaseEntity;

/**
 * The ApiKey class represents a single API key for a user.
 */
@Entity
@Cacheable
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
    @NamedQuery(
            name = "ApiKey.findByUser",
            query = "SELECT k FROM ApiKey k" +
                    //" WHERE k.user = :user" +
                    " WHERE k.user.id = :userId" +
                    " AND k.deletedDateTime IS NULL"),
    @NamedQuery(
            name = "ApiKey.findByValue",
            query = "SELECT k FROM ApiKey k" +
                    " WHERE k.value = :value")
})
public class ApiKey extends DefaultBaseEntity {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private SecurityUser user;

    private String name;

    @Column(length = 64, unique = true)
    @CacheIndex
    private String value;

    public SecurityUser getUser() {
        return user;
    }

    public void setUser(final SecurityUser user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    @Override
    public void validate() {
        Validate.notNull(user, "API key user must not be null.");
        Validate.notEmpty(value, "API key value must not be null or empty.");
    }
}
