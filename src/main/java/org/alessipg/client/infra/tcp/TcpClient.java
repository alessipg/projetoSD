package org.alessipg.client.infra.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


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
        socket = new Socket(host, port);
        setupStreams();
        System.out.println("Connected to server " + host + ":" + port);
    }

    public void setupStreams() throws IOException {
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
            if (line.trim().endsWith("}")) {
                break;
            }
        }
        String received = sb.toString();
        System.out.println("Received: " + received);
        return received;
    }
}