package com.example;

import java.io.InputStream;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.minijax.Minijax;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.sns.message.SnsMessage;
import com.amazonaws.services.sns.message.SnsMessageManager;
import com.amazonaws.services.sns.message.SnsNotification;
import com.amazonaws.services.sns.message.SnsSubscriptionConfirmation;
import com.amazonaws.services.sns.message.SnsUnsubscribeConfirmation;

@Path("/snsexample")
public class SnsExample {
    private static final Logger LOG = LoggerFactory.getLogger(SnsExample.class);

    @Inject
    private SnsMessageManager sns;

    @GET
    public Response handle() {
        return Response.ok().build();
    }

    @POST
    public Response handle(final InputStream inputStream) {
        final SnsMessage message = sns.parseMessage(inputStream);

        if (message instanceof SnsSubscriptionConfirmation) {
            return handleSubscribe((SnsSubscriptionConfirmation) message);

        } else if (message instanceof SnsUnsubscribeConfirmation) {
            return handleUnsubscribe((SnsUnsubscribeConfirmation) message);

        } else if (message instanceof SnsNotification) {
            return handleNotification((SnsNotification) message);

        } else {
            throw new BadRequestException("Unrecognized SNS message");
        }
    }

    private static Response handleSubscribe(final SnsSubscriptionConfirmation message) {
        LOG.info("Confirm subscription to topic {}", message.getTopicArn());
        message.confirmSubscription();
        return Response.ok().build();
    }

    private static Response handleUnsubscribe(final SnsUnsubscribeConfirmation message) {
        LOG.info("Unsubscribed from topic {}", message.getTopicArn());
        return Response.ok().build();
    }

    private static Response handleNotification(final SnsNotification message) {
        LOG.info("Received notification for topic {}", message.getTopicArn());
        LOG.info("  Message ID: {}", message.getMessageId());
        LOG.info("  Timestamp:  {}", message.getTimestamp());
        LOG.info("  Subject:    {}", message.getSubject());
        LOG.info("  Message:    {}", message.getMessage());
        return Response.ok().build();
    }

    public static void main(final String[] args) {
        new Minijax()
                .register(new SnsMessageManager())
                .register(SnsExample.class)
                .start();
    }
}
