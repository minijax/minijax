package org.minijax.avatar;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.ws.rs.client.Client;

import org.junit.Before;
import org.junit.Test;
import org.minijax.MinijaxRequestContext;
import org.minijax.s3.MockUploadService;
import org.minijax.s3.UploadService;
import org.minijax.test.MinijaxTest;

public class AvatarServiceTest extends MinijaxTest {
    private BufferedImage image;

    @Before
    public void setUp() {
        image = mock(BufferedImage.class);
        register(mock(Client.class), Client.class);
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
    public void testGravatar() throws Exception {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final User user = new User();
            user.setEmail("reshma.khilnani@gmail.com");

            ctx.get(AvatarService.class).tryGravatar(user);
        }
    }
}
