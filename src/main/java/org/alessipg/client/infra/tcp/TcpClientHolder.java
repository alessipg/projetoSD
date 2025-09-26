package org.alessipg.client.infra.tcp;

public class TcpClientHolder {
    private static TcpClient instance;

    public static void set(TcpClient client) {
        instance = client;
    }

    public static TcpClient get() {
        return instance;
    }
}
