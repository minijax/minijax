package com.example;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Date;

import javax.ws.rs.client.Entity;

import org.junit.Before;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

import com.amazonaws.services.sns.message.SnsMessageManager;
import com.amazonaws.services.sns.message.SnsNotification;
import com.amazonaws.services.sns.message.SnsSubscriptionConfirmation;
import com.amazonaws.services.sns.message.SnsUnknownMessage;
import com.amazonaws.services.sns.message.SnsUnsubscribeConfirmation;

public class SnsExampleTest extends MinijaxTest {

    @Before
    public void setUp() {
        register(SnsExample.class);
    }

    @Test
    public void testGet() {
        assertEquals(200, target("/snsexample").request().get().getStatus());
    }

    @Test
    public void testPostSubscription() {
        final SnsSubscriptionConfirmation message = mock(SnsSubscriptionConfirmation.class);
        when(message.getTopicArn()).thenReturn("TEST TOPIC");

        final SnsMessageManager sns = mock(SnsMessageManager.class);
        when(sns.parseMessage(any())).thenReturn(message);

        resetServer();
        register(sns, SnsMessageManager.class);
        register(SnsExample.class);

        assertEquals(200, target("/snsexample").request().post(Entity.text("")).getStatus());
    }

    @Test
    public void testPostUnsubscribe() {
        final SnsUnsubscribeConfirmation message = mock(SnsUnsubscribeConfirmation.class);
        when(message.getTopicArn()).thenReturn("TEST TOPIC");

        final SnsMessageManager sns = mock(SnsMessageManager.class);
        when(sns.parseMessage(any())).thenReturn(message);

        resetServer();
        register(sns, SnsMessageManager.class);
        register(SnsExample.class);

        assertEquals(200, target("/snsexample").request().post(Entity.text("")).getStatus());
    }

    @Test
    public void testPostNotification() {
        final SnsNotification message = mock(SnsNotification.class);
        when(message.getTopicArn()).thenReturn("TEST TOPIC");
        when(message.getMessageId()).thenReturn("Test ID");
        when(message.getTimestamp()).thenReturn(new Date());
        when(message.getSubject()).thenReturn("Test Subject");
        when(message.getMessage()).thenReturn("Hello World");

        final SnsMessageManager sns = mock(SnsMessageManager.class);
        when(sns.parseMessage(any())).thenReturn(message);

        resetServer();
        register(sns, SnsMessageManager.class);
        register(SnsExample.class);

        assertEquals(200, target("/snsexample").request().post(Entity.text("")).getStatus());
    }

    @Test
    public void testPostUnrecognized() {
        final SnsUnknownMessage message = mock(SnsUnknownMessage.class);

        final SnsMessageManager sns = mock(SnsMessageManager.class);
        when(sns.parseMessage(any())).thenReturn(message);

        resetServer();
        register(sns, SnsMessageManager.class);
        register(SnsExample.class);

        assertEquals(400, target("/snsexample").request().post(Entity.text("")).getStatus());
    }
}
