package org.minijax.dao;

import java.io.Serializable;

import javax.json.bind.annotation.JsonbTransient;

import jakarta.persistence.Embeddable;

/**
 * The Avatar class represents an avatar or profile picture for an entity.
 *
 * Entities that have an Avatar should implement the AvatarEntity interface.
 *
 * The avatar fields are intended to be "embedded" with the parent entity,
 * such that each of these columns are added to the entity's database table.
 */
@Embeddable
public class Avatar implements Serializable {
    private static final long serialVersionUID = 1L;

    /** The image is the original generated default. */
    public static final int IMAGE_TYPE_DEFAULT = 0;

    /** The image was manually uploaded by the user. */
    public static final int IMAGE_TYPE_MANUAL = 1;

    /** The image was pulled from Gravatar. */
    public static final int IMAGE_TYPE_GRAVATAR = 2;

    /** The image was pulled from Google Plus. */
    public static final int IMAGE_TYPE_GOOGLE = 3;

    private String imageUrl;
    private String thumbUrl;

    @JsonbTransient
    private int imageType;

    public Avatar() {
        this(null, null);
    }

    public Avatar(final String imageUrl, final String thumbUrl) {
        this.imageUrl = imageUrl;
        this.thumbUrl = thumbUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(final String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public int getImageType() {
        return imageType;
    }

    public void setImageType(final int imageType) {
        this.imageType = imageType;
    }
}
