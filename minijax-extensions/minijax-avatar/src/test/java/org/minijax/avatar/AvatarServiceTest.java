package org.minijax.avatar;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.junit.Before;
import org.junit.Test;
import org.minijax.MinijaxRequestContext;
import org.minijax.db.Avatar;
import org.minijax.s3.MockUploadService;
import org.minijax.s3.UploadService;
import org.minijax.test.MinijaxTest;

public class AvatarServiceTest extends MinijaxTest {
    private BufferedImage image;

    @Before
    public void setUp() {
        image = mock(BufferedImage.class);
        register(ClientBuilder.newClient(), Client.class);
        register(new MockUploadService(), UploadService.class);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateThumbnailNullImage() {
        AvatarService.createThumbnail(null, Color.white, 256);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateThumbnailNullColor() {
        AvatarService.createThumbnail(image, null, 256);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateThumbnailZeroTarget() {
        AvatarService.createThumbnail(image, Color.white, 0);
    }

    @Test
    public void testCreateThumbnail() {
        final BufferedImage original = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        final BufferedImage result = AvatarService.createThumbnail(original, Color.white, 32);

        assertNotNull(result);
        assertEquals(32, result.getWidth());
        assertEquals(32, result.getHeight());
    }

    @Test
    public void testCreateThumbnailFromWideImage() {
        final BufferedImage original = new BufferedImage(200, 100, BufferedImage.TYPE_INT_ARGB);
        final BufferedImage result = AvatarService.createThumbnail(original, Color.white, 32);

        assertNotNull(result);
        assertEquals(32, result.getWidth());
        assertEquals(32, result.getHeight());
    }

    @Test
    public void testCreateThumbnailFromTallImage() {
        final BufferedImage original = new BufferedImage(100, 200, BufferedImage.TYPE_INT_ARGB);
        final BufferedImage result = AvatarService.createThumbnail(original, Color.white, 32);

        assertNotNull(result);
        assertEquals(32, result.getWidth());
        assertEquals(32, result.getHeight());
    }

    @Test
    public void testGenerateAvatar() {
        final BufferedImage result = AvatarService.generateAvatarImage();
        assertNotNull(result);
        assertEquals(256, result.getWidth());
        assertEquals(256, result.getHeight());
    }

    @Test
    public void testGenerateAvatarForUser() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final User user = new User();

            final AvatarService avatarService = ctx.get(AvatarService.class);
            avatarService.generateAvatarImage(user);

            assertNotNull(user.getAvatar());
            assertNotNull(user.getAvatar().getImageUrl());
        }
    }

    @Test
    public void testGravatar() throws Exception {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final User user = new User();
            user.setEmail("reshma.khilnani@gmail.com");

            ctx.get(AvatarService.class).tryGravatar(user);
        }
    }

    @Test
    public void testRemoteImageNotImage() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final User user = new User();

            final AvatarService avatarService = ctx.get(AvatarService.class);
            avatarService.tryRemotePicture(user, "https://minijax.org/test/hello.txt", Avatar.IMAGE_TYPE_MANUAL);

            assertNull(user.getAvatar());
        }
    }

    @Test
    public void testUploadNullFile() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final User user = new User();

            final AvatarService avatarService = ctx.get(AvatarService.class);
            avatarService.handleFileUpload(user, null);

            assertNull(user.getAvatar());
        }
    }

    @Test
    public void testUploadNonImage() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final User user = new User();

            final AvatarService avatarService = ctx.get(AvatarService.class);
            avatarService.handleFileUpload(user, AvatarServiceTest.class.getClassLoader().getResourceAsStream("hello.txt"));

            assertNull(user.getAvatar());
        }
    }

    @Test
    public void testUploadImage() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final User user = new User();

            final AvatarService avatarService = ctx.get(AvatarService.class);
            avatarService.handleFileUpload(user, AvatarServiceTest.class.getClassLoader().getResourceAsStream("image.png"));

            assertNotNull(user.getAvatar());
        }
    }
}
