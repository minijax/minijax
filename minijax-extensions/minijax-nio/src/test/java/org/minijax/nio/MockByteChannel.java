//package org.minijax.nio;
//
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.nio.channels.ByteChannel;
//
//public class MockByteChannel implements ByteChannel {
//    private final ByteBuffer inputBuffer;
//    private final ByteBuffer outputBuffer;
//    private boolean open;
//
//    public MockByteChannel() {
//        inputBuffer = ByteBuffer.allocate(1000);
//        outputBuffer = ByteBuffer.allocate(1000);
//        open = true;
//    }
//
//    public MockByteChannel(final String input) {
//        this();
//        inputBuffer.put(input.getBytes());
//        inputBuffer.flip();
//    }
//
//    @Override
//    public boolean isOpen() {
//        return open;
//    }
//
//    @Override
//    public void close() throws IOException {
//        open = false;
//    }
//
//    @Override
//    public int read(final ByteBuffer dst) throws IOException {
//        final int bytesRead = inputBuffer.remaining();
//        dst.put(inputBuffer);
//        return bytesRead;
//    }
//
//    @Override
//    public int write(final ByteBuffer src) throws IOException {
//        final int bytesWritten = src.remaining();
//        outputBuffer.put(src);
//        return bytesWritten;
//    }
//
//    public String getOutputAsString() {
//        outputBuffer.flip();
//        final StringBuilder b = new StringBuilder();
//        while (outputBuffer.hasRemaining()) {
//            b.append((char) outputBuffer.get());
//        }
//        return b.toString();
//    }
//}
