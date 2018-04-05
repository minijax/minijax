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

    @Test
    public void testCreateThumbnailNullImage() {
        try {
            AvatarService.createThumbnail(null, Color.white, 256);
            fail("Expected NullPointerException");
        } catch (final NullPointerException ex) {
            assertNotNull(ex);
        }
    }

    @Test
    public void testCreateThumbnailNullColor() {
        try {
            AvatarService.createThumbnail(image, null, 256);
            fail("Expected NullPointerException");
        } catch (final NullPointerException ex) {
            assertNotNull(ex);
        }
    }

    @Test
    public void testCreateThumbnailZeroTarget() {
        try {
            AvatarService.createThumbnail(image, Color.white, 0);
            fail("Expected IllegalArgumentException");
        } catch (final IllegalArgumentException ex) {
            assertNotNull(ex);
        }
    }

    @Test
    public void testCreateThumbnail() {
        final BufferedImage result = AvatarService.createThumbnail(image, Color.white, 256);

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
