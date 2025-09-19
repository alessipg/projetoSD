package org.alessipg.server.infra.tcp;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            out.println("Welcome! You are connected to the server.");

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received from client: " + message);

                // Example: echo protocol (just send back what client says)
                out.println("Echo: " + message);

                // You can add here your custom protocol parsing:
                // e.g. if message starts with "LOGIN:", call a login service
            }

        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        } finally {
            try {
                socket.close();
                System.out.println("Client disconnected.");
            } catch (IOException ignored) {}
        }
    }
}
