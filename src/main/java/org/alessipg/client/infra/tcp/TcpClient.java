package org.alessipg.client.infra.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.records.UserLoginRequest;
import org.alessipg.shared.util.IntegerAsStringAdapter;

public class TcpClient {
    private Socket socket;
    private String host;
    private int port;
    private Gson gson;
    private PrintWriter out;
    private BufferedReader in;
    private String token;

    public TcpClient(String host, int port) {
        this.host = host;
        this.port = port;
        gson = new GsonBuilder()
                .registerTypeAdapter(Integer.class, new IntegerAsStringAdapter())
                .create();
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

    public StatusTable login(String usuario, String senha) throws IOException {
        UserLoginRequest msg = new UserLoginRequest(usuario, senha);
        String json = gson.toJson(msg);
        System.out.println("Sending: " + json);
        out.println(json);
        
        // Aguarda resposta do servidor
        String response = in.readLine();
        System.out.println("Server response received: " + response);
        if (response != null) {
            var jsonObject = gson.fromJson(response, com.google.gson.JsonObject.class);
            String status = jsonObject.has("status") ? jsonObject.get("status").getAsString() : "";
            switch (status) {
                case "200":
                    this.token = jsonObject.has("token") ? jsonObject.get("token").getAsString() : null;
                    System.out.println("Login successful. Token: " + this.token);
                    return StatusTable.OK;
                case "401":
                    System.out.println("Unauthorized: " + (jsonObject.has("message") ? jsonObject
                            .get("message").getAsString() : "No message"));
                    return StatusTable.UNAUTHORIZED;
                case "500":
                    System.out.println("Falha no login: " + (jsonObject.has("message") ? jsonObject.get("message").getAsString() : "Motivo desconhecido"));
                    return StatusTable.INTERNAL_SERVER_ERROR;
                default:
                    return StatusTable.IM_TEAPOT;
            }
        } else {
            System.out.println("Nenhuma resposta do servidor.");
            return StatusTable.INTERNAL_SERVER_ERROR;
        }
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