package org.minijax.nio;

class Config {
    /**
     * Server socket "receive size".
     *
     * @see java.net.SocketOptions#SO_RCVBUF
     */
    public static final int LISTENER_BUFFER_SIZE = 16 * 1024;

    /**
     * Server socket "backlog" size.
     *
     * @see java.net.ServerSocket#bind(java.net.SocketAddress, int)
     */
    public static final int MAX_CONNECTIONS = 16 * 1024;

    /**
     * Server socket select timeout.
     *
     * If positive, block for up to milliseconds, more or less, while waiting for a channel to become
     * ready; if zero, block indefinitely; must not be negative
     *
     * @see java.nio.channels.Selector#select
     */
    public static final int SELECT_TIMEOUT = 20;

    /**
     * Enable/disable socket timeout with the specified timeout, in milliseconds.
     * @see java.net.SocketOptions#SO_TIMEOUT SO_TIMEOUT
     */
    public static final int READ_TIMEOUT = 1_000;
    public static final int KEEP_ALIVE_TIMEOUT = 20_000;

    Config() {
        throw new UnsupportedOperationException();
    }
}
