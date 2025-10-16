package org.alessipg.client.infra.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;


public class TcpClient {
    private Socket socket;
    private String host;
    private int port;
    private PrintWriter out;
    private BufferedReader in;

    public TcpClient(String host, int port) {
        this.host = host;
        this.port = port;

    }

    public void connect() throws IOException, UnknownHostException {
        socket = new Socket();
        // Set connect and read timeouts to avoid indefinite blocking
        socket.connect(new InetSocketAddress(host, port), 5000);
        socket.setSoTimeout(5000);
        setupStreams();
        System.out.println("Connected to server " + host + ":" + port);
    }

    public void setupStreams() throws IOException {
        this.in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), "UTF-8"));
        this.out = new PrintWriter(socket.getOutputStream(), true, Charset.forName("UTF-8"));

        }

    public void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            System.out.println("Disconnected from server.");
        }
    }
    
    public void send(String json) throws IOException {
        System.out.println("Sending: "+ json);
        out.println(json);
    }

    public String receive() throws IOException {
        // Prefer a single line per message if the server sends newline-terminated JSON
        String line = in.readLine();
        if (line == null) {
            return null;
        }
        System.out.println("Received: " + line);
        return line;
    }
}