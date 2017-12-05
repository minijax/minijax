package org.minijax.cloudwatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.logs.AWSLogs;
import com.amazonaws.services.logs.AWSLogsClientBuilder;
import com.amazonaws.services.logs.model.CreateLogGroupRequest;
import com.amazonaws.services.logs.model.CreateLogStreamRequest;
import com.amazonaws.services.logs.model.DataAlreadyAcceptedException;
import com.amazonaws.services.logs.model.InputLogEvent;
import com.amazonaws.services.logs.model.InvalidSequenceTokenException;
import com.amazonaws.services.logs.model.PutLogEventsRequest;
import com.amazonaws.services.logs.model.PutLogEventsResult;
import com.amazonaws.services.logs.model.ResourceAlreadyExistsException;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.layout.EchoLayout;
import ch.qos.logback.core.status.ErrorStatus;
import ch.qos.logback.core.status.InfoStatus;
import ch.qos.logback.core.status.WarnStatus;

/**
 * SLF4J appender for AWS CloudWatch Logs.
 */
public class CloudWatchAppender extends AppenderBase<ILoggingEvent> {
    private static final long PERIOD = 5000L;
    private final Object lockObject = new Object();
    private final List<InputLogEvent> eventQueue = Collections.synchronizedList(new ArrayList<>());
    private Layout<ILoggingEvent> layout;
    private String logGroupName;
    private String logStreamName;
    private AWSLogs awsLogs;
    private String sequenceToken;

    public Layout<ILoggingEvent> getLayout() {
        return layout;
    }

    public void setLayout(final Layout<ILoggingEvent> layout) {
        this.layout = layout;
    }

    public String getLogGroupName() {
        return logGroupName;
    }

    public void setLogGroupName(final String logGroupName) {
        this.logGroupName = logGroupName;
    }

    public String getLogStreamName() {
        return logStreamName;
    }

    public void setLogStreamName(final String logStreamName) {
        this.logStreamName = logStreamName;
    }

    public AWSLogs getAwsLogs() {
        return awsLogs;
    }

    public void setAwsLogs(final AWSLogs awsLogs) {
        this.awsLogs = awsLogs;
    }

    @Override
    public synchronized void start() {
        if (isStarted()) {
            return;
        }

        if (layout == null) {
            layout = new EchoLayout<>();
            addStatus(new WarnStatus("No layout, default to " + layout, this));
        }
        if (logGroupName == null) {
            logGroupName = getClass().getSimpleName();
            addStatus(new WarnStatus("No logGroupName, default to " + logGroupName, this));
        }
        if (logStreamName == null) {
            logStreamName = new SimpleDateFormat("yyyyMMdd'T'HHmmss").format(new Date());
            addStatus(new WarnStatus("No logGroupName, default to " + logStreamName, this));
        }
        try {
            if (awsLogs == null) {
                awsLogs = AWSLogsClientBuilder.defaultClient();
            }
            createLogGroup();
            createLogStream();
        } catch (final AmazonClientException e) {
            awsLogs = null;
            addStatus(new ErrorStatus(e.getMessage(), this, e));
        }

        // Start a new daemon time thread to periodically upload events
        // Because this is a deamon thread, it will not block shutdown
        new DaemonTimerThread().start();

        // Add a shutdown hook to catch any final events at shutdown
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());

        layout.start();
        super.start();
    }

    @Override
    public synchronized void stop() {
        if (!isStarted()) {
            return;
        }

        uploadEvents(getBatch());

        if (awsLogs != null) {
            awsLogs.shutdown();
            awsLogs = null;
        }

        super.stop();
        layout.stop();
    }

    @Override
    protected void append(final ILoggingEvent event) {
        synchronized (lockObject) {
            eventQueue.add(new InputLogEvent()
                    .withTimestamp(event.getTimeStamp())
                    .withMessage(layout.doLayout(event)));
        }
    }

    private void createLogGroup() {
        try {
            awsLogs.createLogGroup(new CreateLogGroupRequest().withLogGroupName(logGroupName));
        } catch (final ResourceAlreadyExistsException e) {
            addStatus(new InfoStatus(e.getMessage(), this));
        }
    }

    private void createLogStream() {
        try {
            awsLogs.createLogStream(new CreateLogStreamRequest().withLogGroupName(logGroupName).withLogStreamName(logStreamName));
        } catch (final ResourceAlreadyExistsException e) {
            addStatus(new InfoStatus(e.getMessage(), this));
        }
    }

    private Collection<InputLogEvent> getBatch() {
        synchronized (lockObject) {
            final Collection<InputLogEvent> result = new ArrayList<>(eventQueue);
            eventQueue.clear();
            return result;
        }
    }

    private void uploadEvents(final Collection<InputLogEvent> events) {
        if (events.isEmpty()) {
            return;
        }

        try {
            final PutLogEventsRequest request = new PutLogEventsRequest()
                    .withLogGroupName(logGroupName)
                    .withLogStreamName(logStreamName)
                    .withSequenceToken(sequenceToken)
                    .withLogEvents(events);
            final PutLogEventsResult result = awsLogs.putLogEvents(request);
            sequenceToken = result.getNextSequenceToken();
        } catch (final DataAlreadyAcceptedException e) {
            sequenceToken = e.getExpectedSequenceToken();
        } catch (final InvalidSequenceTokenException e) {
            sequenceToken = e.getExpectedSequenceToken();
            uploadEvents(events);
        }
    }


    /**
     * The DaemonTimerThread is a thread that periodically gets a batch of events
     * and uploads them to AWS.
     *
     * Because this is a daemon thread, it will not block shutdown.
     * However, it will not catch any remaining events at shutdown.
     * For those events, see the ShutdownHook class below.
     */
    private class DaemonTimerThread extends Thread {
        public DaemonTimerThread() {
            setDaemon(true);
        }

        @Override
        public void run() {
            while (true) {
                uploadEvents(getBatch());
                try {
                    Thread.sleep(PERIOD);
                } catch (final InterruptedException ex) {
                    // NOSONAR - Ignore
                }
            }
        }
    }


    /**
     * The ShutdownHook class is a thread that is executed at shutdown.
     *
     * The thread uploads all remaining events in the queue.
     */
    private class ShutdownHook extends Thread {
        @Override
        public void run() {
            uploadEvents(getBatch());
        }
    }
}
