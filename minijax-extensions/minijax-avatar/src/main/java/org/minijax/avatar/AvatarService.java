package org.minijax.avatar;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import org.apache.commons.codec.digest.DigestUtils;
import org.minijax.db.Avatar;
import org.minijax.db.NamedEntity;
import org.minijax.s3.UploadService;
import org.minijax.security.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The AvatarUtils class provides utility methods for generating and uploading
 * avatar images.
 */
@Provider
@Singleton
public class AvatarService {
    private static final Logger LOG = LoggerFactory.getLogger(AvatarService.class);
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36";

    /**
     * Per the recommendations of the Java/Sun team, we do a multi step
     * image resize.  See more details in this blog post:
     * http://today.java.net/pub/a/today/2007/04/03/perils-of-image-getscaledinstance.html
     *
     * We're using a relatively slow and expensive resize factor of 0.5.
     * That means that we only resize the image to one half per resize.
     * If resizing from 2048 (common DX size) to 200 (common thumbnail size),
     * That means you will make 4 resizes (2048 to 1024 to 512 to 256 to 200).
     */
    public static final double RESIZE_FACTOR = 0.5;

    @Context
    private Configuration config;

    @Inject
    private Client client;

    @Inject
    private UploadService uploadService;


    /**
     * Tries to fetch a remote Gravatar image.
     *
     * On success, downloads the image, creates thumbnails, uploads to S3, and updates the user object.
     *
     * @param user The user (email must be populated).
     * @return True on success; false otherwise.
     */
    public boolean tryGravatar(final SecurityUser user) throws IOException {
        // For details on URL structure see:
        // https://en.gravatar.com/site/implement/images/
        final String hash = DigestUtils.md5Hex(user.getEmail().getBytes("CP1252"));
        final String url = "https://www.gravatar.com/avatar/" + hash + "?size=512&d=404";
        return tryRemotePicture(user, url, Avatar.IMAGE_TYPE_GRAVATAR);
    }


    /**
     * Tries to fetch a remote profile picture and set it as the user's avatar.
     *
     * Validates that the picture is actually a picture.
     *
     * Resizes and uploads the image to S3.
     *
     * @param user The user.
     * @param url The profile picture URL.
     * @param imageType The avatar image type (see ajibot.common.model.Avatar).
     */
    public boolean tryRemotePicture(
            final NamedEntity user,
            final String url,
            final int imageType)
                    throws IOException {

        final File tmpFile = File.createTempFile("temp-avatar", null);

        try {
            // Download the file
            downloadFile(url, tmpFile);

            // Open the file and make sure it's an image
            // If not an image, silently ignore
            final BufferedImage image = ImageIO.read(tmpFile);
            if (image == null) {
                return false;
            }

            // Generate thumbnails and upload everything to S3
            setAvatarImage(user, image);
            user.getAvatar().setImageType(imageType);
            return true;

        } catch (final IOException ex) {
            LOG.warn("Error processing remote profile picture: {}", ex.getMessage(), ex);
            return false;

        } finally {
            try {
                // Delete the temp file
                forceDelete(tmpFile);
            } catch (final IOException ex) {
                LOG.error("Error deleting temp file: {}", ex.getMessage(), ex);
            }
        }
    }


    /**
     * Returns a thumbnail of the input image.
     *
     * If the image is not square, the result will be the largest possible
     * square inside the rectangle.  I.e., it will crop the image.
     *
     * @param image The input image.
     * @param bg The background color.
     * @param target The target size.
     */
    public static BufferedImage createThumbnail(
            final BufferedImage image,
            final Color bg,
            final int target) {

        Objects.requireNonNull(image);
        Objects.requireNonNull(bg);

        final int width = image.getWidth();
        final int height = image.getHeight();
        int x;
        int y;
        int size;

        if (width > height) {
            x = (width - height) / 2;
            y = 0;
            size = height;
        } else {
            x = 0;
            y = (height - width) / 2;
            size = width;
        }

        int nextStep = (int) Math.round((size) * RESIZE_FACTOR);
        if (nextStep < target) {
            nextStep = target;
        }

        final BufferedImage dimg = new BufferedImage(
                nextStep,
                nextStep,
                BufferedImage.TYPE_INT_RGB);

        final Graphics2D g = dimg.createGraphics();
        g.setColor(bg);
        g.fillRect(0, 0, nextStep, nextStep);

        g.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        g.drawImage(image, 0, 0, nextStep, nextStep, x, y, x + size, y + size, bg, null);
        g.dispose();

        if (nextStep <= target) {
            return dimg;
        } else {
            return createThumbnail(dimg, bg, target);
        }
    }


    /**
     * Generates a new avatar image.
     */
    public static BufferedImage generateAvatarImage() {
        final float hue = (float) Math.random();
        final Color background = Color.getHSBColor(hue, 0.66f, 0.67f);
        final Color dark = Color.getHSBColor(hue, 0.84f, 0.50f);
        final Color light = Color.getHSBColor(hue, 0.33f, 1.0f);

        final BufferedImage image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = image.createGraphics();

        g.setColor(background);
        g.fillRect(0, 0, 256, 256);

        for (int y = 1; y < 7; y++) {
            for (int x = 1; x < 4; x++) {
                final double p = Math.random();
                if (p > 0.5) {
                    g.setColor(p > 0.8 ? light : dark);
                    g.fillRect(x * 32, y * 32, 32, 32);
                    g.fillRect(224 - x * 32, y * 32, 32, 32);
                }
            }
        }

        g.dispose();
        return image;
    }


    /**
     * Uploads a generated profile picture for the user.
     *
     * @param owner The owner entity (user, organization, etc).
     */
    public void generateAvatarImage(final NamedEntity owner) throws IOException {
        setAvatarImage(owner, generateAvatarImage());
    }


    /**
     * Uploads a new profile picture.
     *
     * Validates that the picture is actually a picture.
     *
     * Resizes and uploads the image.
     *
     * @param entity The avatar entity (user, organization, device, etc).
     * @param file The alleged picture file.
     */
    public void handleFileUpload(final NamedEntity entity, final InputStream file)
            throws IOException {

        if (file == null) {
            return;
        }

        final BufferedImage originalImage = ImageIO.read(file);
        if (originalImage == null) {
            return;
        }

        setAvatarImage(entity, originalImage);
        entity.getAvatar().setImageType(Avatar.IMAGE_TYPE_MANUAL);
    }


    /**
     * Uploads a new user profile picture.
     *
     * Validates that the picture is actually a picture.
     *
     * Resizes and uploads the image.
     *
     * @param owner The owner entity (user, device, organization, etc).
     * @param originalImage The original uncropped image.
     */
    private void setAvatarImage(
            final NamedEntity owner,
            final BufferedImage originalImage)
                    throws IOException {

        Objects.requireNonNull(originalImage);

        Avatar avatar = owner.getAvatar();
        if (avatar == null) {
            avatar = new Avatar();
            owner.setAvatar(avatar);
        }

        final BufferedImage image = createThumbnail(originalImage, Color.white, 256);
        avatar.setImageUrl(uploadImage(image));

        final BufferedImage thumb = createThumbnail(image, Color.white, 64);
        avatar.setThumbUrl(uploadImage(thumb));
    }


    /**
     * Uploads an image to S3 and returns the URL.
     *
     * @param image The image.
     * @return The image URL.
     */
    private String uploadImage(final BufferedImage image) throws IOException {
        final File imageFile = File.createTempFile("picture-", ".jpg");
        ImageIO.write(image, "jpg", imageFile);

        final String bucketName = (String) config.getProperty("org.minijax.avatar.bucketName");
        final String keyName = String.format("avatars/%s.jpg", UUID.randomUUID());
        final String resultUrl = uploadService.upload(bucketName, keyName, imageFile);
        forceDelete(imageFile);
        return resultUrl;
    }


    private void downloadFile(final String url, final File destFile) throws IOException {
        final WebTarget target = client.target(url);
        if (target != null) {
            try (final InputStream in = target.request().header(HttpHeaders.USER_AGENT, USER_AGENT).get(InputStream.class)) {
                Files.copy(in, destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }


    private static void forceDelete(final File file) throws IOException {
        Files.delete(file.toPath());
    }
}
