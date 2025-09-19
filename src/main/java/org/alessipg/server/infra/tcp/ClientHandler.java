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
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received from client: " + message);
                
                try {
                    String response = JsonRouter.parse(message);
                    if (response != null && !response.isEmpty()) {
                        System.out.println("Sending response: " + response);
                        out.println(response); // Envia a resposta JSON do JsonRouter
                    } else {
                        String errorResponse = "{\"status\":\"500\",\"message\":\"No response generated\"}";
                        System.out.println("Sending error response: " + errorResponse);
                        out.println(errorResponse);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing message: " + e.getMessage());
                    out.println("Error: " + e.getMessage());
                }
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
