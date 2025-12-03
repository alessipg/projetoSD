package org.alessipg.server.infra.tcp;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.alessipg.server.ui.ServerView;
import org.alessipg.shared.enums.StatusTable;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final String clientAddress;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        // Store client address as IP:port
        this.clientAddress = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
    }

    @Override
    public void run() {
        System.out.println("[ClientHandler] Thread started for client: " + clientAddress);

        try {
            // Register connection when client connects (both in manager and UI)
            System.out.println("[ClientHandler] Registering connection in ConnectionManager: " + clientAddress);
            ConnectionManager.getInstance().registerConnection(clientAddress);

            System.out.println("[ClientHandler] Adding connection to ServerView: " + clientAddress);
            ServerView.addConnection(clientAddress);

            System.out.println("[ClientHandler] Connection registered successfully: " + clientAddress);
        } catch (Exception e) {
            System.err.println("[ClientHandler] ERROR during connection registration: " + e.getMessage());
            e.printStackTrace();
            return; // Exit if registration fails
        }

        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)) {

            System.out.println("[ClientHandler] Streams created, waiting for messages from: " + clientAddress);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("[ClientHandler] Received from client " + clientAddress + ": " + message);

                try {
                    String response = JsonRouter.parse(message, clientAddress);
                    if (response != null && !response.isEmpty()) {
                        System.out.println("[ClientHandler] Sending response to " + clientAddress + ": " + response);
                        out.println(response);
                        out.flush();
                    } else {
                        String errorResponse = "{\"status\":\""+StatusTable.INTERNAL_SERVER_ERROR.getStatus()+"\",\"mensagem\":\"Erro interno, resposta não gerada\"}";
                        System.out.println("[ClientHandler] Sending error response to " + clientAddress + ": " + errorResponse);
                        out.println(errorResponse);
                        out.flush();
                    }
                    if (isLogout(message)) {
                        System.out.println("[ClientHandler] Logout detected for: " + clientAddress);
                        break;
                    }
                } catch (Exception e) {
                    System.err.println("[ClientHandler] Error processing message from " + clientAddress + ": " + e.getMessage());
                    e.printStackTrace();
                    out.println("{\"status\":\""+StatusTable.INTERNAL_SERVER_ERROR.getStatus()+"\",\"mensagem\":\"Internal server error: " + e.getMessage() + "\"}");
                    out.flush();
                }
            }

            System.out.println("[ClientHandler] Client " + clientAddress + " closed connection (readLine returned null)");

        } catch (IOException e) {
            System.err.println("[ClientHandler] Connection error for " + clientAddress + ": " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("[ClientHandler] Unexpected error for " + clientAddress + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("[ClientHandler] Cleaning up connection: " + clientAddress);
            try {
                // Unregister connection when client disconnects
                ConnectionManager.getInstance().unregisterConnection(clientAddress);
                ServerView.removeConnection(clientAddress);
                socket.close();
                System.out.println("[ClientHandler] Client disconnected and cleaned up: " + clientAddress);
            } catch (Exception e) {
                System.err.println("[ClientHandler] Error during cleanup for " + clientAddress + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private boolean isLogout(String json) {
        try {
            com.google.gson.JsonObject obj = 
            com.google.gson.JsonParser.parseString(json).getAsJsonObject();
            String op = obj.has("operacao") ? 
            obj.get("operacao").getAsString() 
            : "";
            return "LOGOUT".equalsIgnoreCase(op);
        } catch (Exception e) {
            return false;
        }
    }
}
