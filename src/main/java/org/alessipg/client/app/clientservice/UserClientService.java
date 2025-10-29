package org.alessipg.client.app.clientservice;

import java.io.IOException;

import com.google.gson.JsonSyntaxException;
import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.client.infra.tcp.TcpClient;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.dto.request.UserDeleteRequest;
import org.alessipg.shared.dto.request.UserCreateRequest;
import org.alessipg.shared.dto.request.UserSelfGetRequest;
import org.alessipg.shared.dto.request.UserUpdateRequest;
import org.alessipg.shared.dto.response.StatusResponse;
import org.alessipg.shared.dto.response.UserSelfGetResponse;
import org.alessipg.shared.dto.util.UserRecord;

import com.google.gson.Gson;

public class UserClientService {

    private final Gson gson;
    private final TcpClient client;

    public UserClientService(Gson gson) {
        this.gson = gson;
        this.client = SessionManager.getClient();
    }

    public StatusResponse create(String usuario, String senha) throws IOException {
        UserCreateRequest msg = new UserCreateRequest(usuario, senha);
        String json = gson.toJson(msg);
        client.send(json);
        String response = client.receive();
        if (response == null) {
            return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
        }
        try {
            StatusResponse res = gson.fromJson(response, StatusResponse.class);
            if (res != null) {
                return res;
            } else {
                return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
            }
        } catch(Exception e) {
            return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
        }
    }

    public UserSelfGetResponse selfGet() throws IOException {
        String token = SessionManager.getInstance().getToken();
        UserSelfGetRequest msg = new UserSelfGetRequest(token);
        String json = gson.toJson(msg);
    
        client.send(json);
        String response = client.receive();
        if (response == null) {
            return new UserSelfGetResponse(StatusTable.INTERNAL_SERVER_ERROR, null);
        }
        try{
            UserSelfGetResponse res = gson.fromJson(response, UserSelfGetResponse.class);
            if (res != null) {
                return res;
            } else {
                return new UserSelfGetResponse(StatusTable.INTERNAL_SERVER_ERROR, null);
            }
        } catch(Exception e) {
            return new UserSelfGetResponse(StatusTable.INTERNAL_SERVER_ERROR, null);
        }
    }

    public StatusResponse update(String newPassword) throws IOException {
        UserRecord user = new UserRecord(null,newPassword);
        UserUpdateRequest msg = new UserUpdateRequest(user,SessionManager.getInstance().getToken());
        String json = gson.toJson(msg);
        client.send(json);
        String response = client.receive();
        try {
            if (response == null) {
                return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
            }
            StatusResponse res = gson.fromJson(response, StatusResponse.class);
            if (res != null) {
                return res;
            } else {
                return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
        }
    }

    public StatusResponse delete() throws IOException {
        UserDeleteRequest msg = new UserDeleteRequest(SessionManager.getInstance().getToken());
        String json = gson.toJson(msg);
        client.send(json);
        String response = client.receive();
        if (response == null) {
            return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
        }
        try {
            StatusResponse res = gson.fromJson(response, StatusResponse.class);
            if (res != null) {
                return res;
            } else {
                return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
            }
        } catch (JsonSyntaxException e) {
            return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
        }
    }
}
