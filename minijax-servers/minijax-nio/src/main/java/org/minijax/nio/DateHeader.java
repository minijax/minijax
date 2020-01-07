package org.minijax.nio;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

class DateHeader {
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
    private static final Timer timer = new Timer();
    private static byte[] value = "".getBytes();

    DateHeader() {
        throw new UnsupportedOperationException();
    }

    public static void start() {
        update();
        timer.scheduleAtFixedRate(new UpdateTask(), 0L, 1000L);
    }

    public static void stop() {
        timer.cancel();
    }

    public static byte[] get() {
        return value;
    }

    private static void update() {
        value = FORMAT.format(new Date()).getBytes();
    }

    private static class UpdateTask extends TimerTask {
        @Override
        public void run() {
            update();
        }
    }
}
