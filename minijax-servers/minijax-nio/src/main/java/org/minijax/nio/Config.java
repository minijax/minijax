package org.minijax.nio;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

class Config {
    public static final SocketAddress ENDPOINT = new InetSocketAddress(8080);
    public static final int RECEIVE_BUFFER_SIZE = 16 * 1024;
    public static final int MAX_CONNECTIONS = 16 * 1024;
    public static final int SELECT_TIMEOUT = 20;
    public static final int READ_TIMEOUT = 1_000;
    public static final int KEEP_ALIVE_TIMEOUT = 20_000;
}
