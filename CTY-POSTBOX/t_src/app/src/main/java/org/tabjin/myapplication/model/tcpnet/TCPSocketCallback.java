package org.tabjin.myapplication.model.tcpnet;

public abstract interface TCPSocketCallback {
    public static final int TCP_DISCONNECTED = 0;
    public static final int TCP_CONNECTED = 1;
    public static final int TCP_DATA = 2;
    public abstract void tcp_connected();
    public abstract void tcp_receive(byte[] buffer);
    public abstract void tcp_disconnect();
}
