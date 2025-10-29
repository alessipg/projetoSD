package org.alessipg.server.infra.tcp;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.alessipg.shared.enums.StatusTable;

public class ClientHandler implements Runnable {

    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)) {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received from client: " + message);

                try {
                    String response = JsonRouter.parse(message);
                    if (response != null && !response.isEmpty()) {
                        System.out.println("Sending response: " + response);
                        out.println(response); // Envia a resposta JSON do JsonRouter
                    } else {
                        String errorResponse = "{\"status\":\""+StatusTable.INTERNAL_SERVER_ERROR.getStatus()+"\",\"mensagem\":\"Erro interno, resposta não gerada\"}";
                        System.out.println("Sending error response: " + errorResponse);
                        out.println(errorResponse);
                    }
                    if (isLogout(message)) {
                        break; // sai do while; o finally fechará o socket
                    }
                } catch (Exception e) {
                    System.err.println("Error processing message: " + e.getMessage());
                    out.println("{\"status\":\""+StatusTable.INTERNAL_SERVER_ERROR.getStatus()+"\",\"mensagem\":\"Internal server error\"}"); // Resposta de erro genérica
                }
            }

        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        } finally {
            try {
                socket.close();
                System.out.println("Client disconnected.");
            } catch (IOException ignored) {
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
