package org.alessipg.client.app.clientservice;

import java.io.IOException;

import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.client.infra.tcp.TcpClient;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.records.request.UserRegisterRequest;
import org.alessipg.shared.records.request.UserSelfGetRequest;
import org.alessipg.shared.records.request.UserUpdateRequest;
import org.alessipg.shared.records.response.UserSelfGetResponse;
import org.alessipg.shared.records.util.UserRecord;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class UserClientService {

    private final Gson gson;
    private final TcpClient client;

    public UserClientService(Gson gson) {
        this.gson = gson;
        this.client = SessionManager.getClient();
    }

    public StatusTable create(String usuario, String senha) throws IOException {
        UserRegisterRequest msg = new UserRegisterRequest(usuario, senha);

        String json = gson.toJson(msg);
        client.send(json);
        String response = client.receive();
        if (response != null) {
            JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
            String status = jsonObject.has("status") ? jsonObject.get("status").getAsString() : "";
            switch (status) {
                case "201":
                    return StatusTable.OK;
                case "400":
                    return StatusTable.BAD;
                case "409":
                    return StatusTable.ALREADY_EXISTS;
                case "500":
                    return StatusTable.INTERNAL_SERVER_ERROR;
                default:
                    return StatusTable.IM_TEAPOT;
            }
        } else {
            return StatusTable.INTERNAL_SERVER_ERROR;
        }
    }

    public UserSelfGetResponse selfGet() throws IOException {
        UserSelfGetRequest msg = new UserSelfGetRequest(SessionManager.getInstance().getToken());
        String json = gson.toJson(msg);
        client.send(json);
        String response = client.receive();
        if (response != null) {
            JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
            String status = jsonObject.has("status") ? jsonObject.get("status").getAsString() : "";
            String user = jsonObject.has("usuario") ? jsonObject.get("usuario").getAsString() : "";
            if (!user.isEmpty())
                return new UserSelfGetResponse(status, user);
            return new UserSelfGetResponse(status,null);
        }
        return new UserSelfGetResponse("500",null);
    }

    public StatusTable update(String newPassword) throws IOException {
        UserRecord user = new UserRecord(null,newPassword);
        UserUpdateRequest msg = new UserUpdateRequest(user,SessionManager.getInstance().getToken());
        String json = gson.toJson(msg);
        client.send(json);
        String response = client.receive();
        if (response != null) {
            JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
            String status = jsonObject.has("status") ? jsonObject.get("status").getAsString() : "";
            switch (status) {
            case "200":
                return StatusTable.OK;
            case "400":
                return StatusTable.BAD;
            case "404":
                return StatusTable.NOT_FOUND;
            default:
                return StatusTable.INTERNAL_SERVER_ERROR;
            }
        }
        else {
            return StatusTable.INTERNAL_SERVER_ERROR;
        }
    }
}
