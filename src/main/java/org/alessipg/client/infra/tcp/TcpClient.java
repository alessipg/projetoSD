package org.alessipg.client.infra.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;


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
        // Set connect timeout (5 seconds is reasonable for connection)
        socket.connect(new InetSocketAddress(host, port), 5000);
        // Set read timeout to 30 seconds (longer to allow server processing time)
        socket.setSoTimeout(30000);
        setupStreams();
        System.out.println("Connected to server " + host + ":" + port);
    }

    public void setupStreams() throws IOException {
        this.in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        // Enable autoflush (second parameter = true)
        this.out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
    }

    public void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            socket.close();
            System.out.println("Disconnected from server.");
        }
    }
    
    public void send(String json) throws IOException {
        if (out == null || socket.isClosed()) {
            throw new IOException("Socket is not connected");
        }
        System.out.println("Sending: " + json);
        out.println(json);
        out.flush(); // Explicit flush to ensure data is sent immediately
        if (out.checkError()) {
            throw new IOException("Error writing to socket");
        }
    }

    public String receive() throws IOException {
        String line = in.readLine();
        if (line == null) {
            return null;
        }
        System.out.println("Received: " + line);
        return line;
    }
}