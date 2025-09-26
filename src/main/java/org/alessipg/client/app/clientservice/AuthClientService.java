package org.alessipg.client.app.clientservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.alessipg.client.infra.tcp.TcpClient;
import org.alessipg.client.infra.tcp.TcpClientHolder;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.records.UserLoginRequest;
import org.alessipg.shared.util.IntegerAsStringAdapter;
import org.alessipg.shared.records.UserRegisterRequest;

import java.io.IOException;

public class AuthClientService {
    private final TcpClient client;
    private final Gson gson;

    public AuthClientService() {
        this.client = TcpClientHolder.get();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Integer.class, new IntegerAsStringAdapter())
                .create();
    }

    public StatusTable login(String usuario, String senha) throws IOException {
        UserLoginRequest msg = new UserLoginRequest(usuario, senha);
        String json = gson.toJson(msg);
        client.send(json);

        String response = client.receive();
        if (response != null) {
            JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
            String status = jsonObject.has("status") ? jsonObject.get("status").getAsString() : "";
            switch (status) {
                case "200": return StatusTable.OK;
                case "401": return StatusTable.UNAUTHORIZED;
                case "500": return StatusTable.INTERNAL_SERVER_ERROR;
                default: return StatusTable.IM_TEAPOT;
            }
        } else {
            return StatusTable.INTERNAL_SERVER_ERROR;
        }
    }

    public StatusTable criarUsuario(String usuario, String senha) throws IOException {
        UserRegisterRequest msg = new UserRegisterRequest(usuario, senha);

        String json = gson.toJson(msg);
        client.send(json);
        String response = client.receive();
        if (response != null) {
            JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
            String status = jsonObject.has("status") ? jsonObject.get("status").getAsString() : "";
            switch (status) {
                case "201": return StatusTable.OK;
                case "400": return StatusTable.BAD;
                case "409": return StatusTable.ALREADY_EXISTS;
                case "500": return StatusTable.INTERNAL_SERVER_ERROR;
                default: return StatusTable.IM_TEAPOT;
            }
        } else {
            return StatusTable.INTERNAL_SERVER_ERROR;
        }
    }
}
