package org.minijax.nio;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

class DateHeader {
    private static final Timer TIMER = new Timer();
    private static byte[] value = "".getBytes();

    DateHeader() {
        throw new UnsupportedOperationException();
    }

    public static void start() {
        TIMER.scheduleAtFixedRate(new UpdateTask(), 0L, 1000L);
    }

    public static void stop() {
        TIMER.cancel();
    }

    public static byte[] get() {
        return value;
    }

    private static class UpdateTask extends TimerTask {
        private final SimpleDateFormat format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        @Override
        public void run() {
            value = format.format(new Date()).getBytes();
        }
    }
}
