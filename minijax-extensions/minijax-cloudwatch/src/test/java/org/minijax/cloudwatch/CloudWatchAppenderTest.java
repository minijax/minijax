package org.minijax.cloudwatch;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.amazonaws.services.logs.AWSLogs;
import com.amazonaws.services.logs.model.PutLogEventsResult;
import com.amazonaws.services.logs.model.ResourceAlreadyExistsException;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.Layout;

@RunWith(MockitoJUnitRunner.class)
public class CloudWatchAppenderTest {

    @Test
    public void testGettersSetters() {
        @SuppressWarnings("unchecked")
        final Layout<ILoggingEvent> layout = mock(Layout.class);
        final AWSLogs awsLogs = mock(AWSLogs.class);
        final String logGroupName = "myGroup";
        final String logStreamName = "myStream";

        final CloudWatchAppender appender = new CloudWatchAppender();
        appender.setLayout(layout);
        appender.setAwsLogs(awsLogs);
        appender.setLogGroupName(logGroupName);
        appender.setLogStreamName(logStreamName);

        assertEquals(layout, appender.getLayout());
        assertEquals(awsLogs, appender.getAwsLogs());
        assertEquals(logGroupName, appender.getLogGroupName());
        assertEquals(logStreamName, appender.getLogStreamName());
    }


    @Test
    public void testDefaultValues() {
        final Context mockContext = mock(Context.class);

        final PutLogEventsResult mockResult = mock(PutLogEventsResult.class);
        when(mockResult.getNextSequenceToken()).thenReturn("2");

        final AWSLogs mockAwsLogs = mock(AWSLogs.class);
        when(mockAwsLogs.putLogEvents(any())).thenReturn(mockResult);

        final CloudWatchAppender appender = new CloudWatchAppender();
        appender.setContext(mockContext);
        appender.setAwsLogs(mockAwsLogs);
        appender.start();
        appender.doAppend(new LoggingEvent());
        appender.stop();
    }


    @Test
    public void testAlreadyExists() {
        final Context mockContext = mock(Context.class);

        final PutLogEventsResult mockResult = mock(PutLogEventsResult.class);
        when(mockResult.getNextSequenceToken()).thenReturn("2");

        final AWSLogs mockAwsLogs = mock(AWSLogs.class);
        when(mockAwsLogs.createLogGroup(any())).thenThrow(ResourceAlreadyExistsException.class);
        when(mockAwsLogs.createLogStream(any())).thenThrow(ResourceAlreadyExistsException.class);
        when(mockAwsLogs.putLogEvents(any())).thenReturn(mockResult);

        final CloudWatchAppender appender = new CloudWatchAppender();
        appender.setContext(mockContext);
        appender.setAwsLogs(mockAwsLogs);
        appender.start();
        appender.doAppend(new LoggingEvent());
        appender.stop();
    }
}
