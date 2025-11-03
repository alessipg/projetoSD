package org.alessipg.client.app.clientservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;
import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.client.infra.tcp.TcpClient;
import org.alessipg.shared.dto.request.*;
import org.alessipg.shared.dto.response.UserGetAllResponse;
import org.alessipg.shared.dto.util.UserView;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.dto.response.StatusResponse;
import org.alessipg.shared.dto.response.UserSelfGetResponse;
import org.alessipg.shared.dto.util.UserRecord;

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
        } catch (Exception e) {
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
        try {
            UserSelfGetResponse res = gson.fromJson(response, UserSelfGetResponse.class);
            if (res != null) {
                return res;
            } else {
                return new UserSelfGetResponse(StatusTable.INTERNAL_SERVER_ERROR, null);
            }
        } catch (Exception e) {
            return new UserSelfGetResponse(StatusTable.INTERNAL_SERVER_ERROR, null);
        }
    }

    public StatusResponse update(String newPassword) throws IOException {
        UserRecord user = new UserRecord(null, newPassword);
        UserUpdateRequest msg = new UserUpdateRequest(user, SessionManager.getInstance().getToken());
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

    public UserGetAllResponse getAll() throws IOException {
        UserGetAllRequest msg = new UserGetAllRequest(SessionManager.getInstance().getToken());
        String json = gson.toJson(msg);
        client.send(json);
        String response = client.receive();
        if (response != null) {
            JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
            String status = jsonObject.has("status") ? jsonObject.get("status").getAsString() : "";
            switch (status) {
                case "200":
                    if (!jsonObject.has("usuarios"))
                        return new UserGetAllResponse(StatusTable.UNPROCESSABLE_ENTITY,
                                StatusTable.UNPROCESSABLE_ENTITY.getMessage(), null);
                    JsonArray usersResponse = jsonObject.getAsJsonArray("usuarios");
                    List<UserView> users = new ArrayList<>();
                    for (JsonElement m : usersResponse) {// TODO: Throw se não conseguir serializar
                        users.add(gson.fromJson(m, UserView.class));
                    }
                    return new UserGetAllResponse(StatusTable.OK, StatusTable.OK.getMessage(), users);
                case "400":
                    return new UserGetAllResponse(StatusTable.BAD, StatusTable.BAD.getMessage(), null);
                case "401":
                    return new UserGetAllResponse(StatusTable.UNAUTHORIZED, StatusTable.UNAUTHORIZED.getMessage(), null);
                case "422":
                    return new UserGetAllResponse(StatusTable.UNPROCESSABLE_ENTITY, StatusTable.BAD.getMessage(), null);
                case "404":
                    return new UserGetAllResponse(StatusTable.NOT_FOUND, StatusTable.NOT_FOUND.getMessage(), null);
                default:
                    return new UserGetAllResponse(StatusTable.INTERNAL_SERVER_ERROR,
                            StatusTable.INTERNAL_SERVER_ERROR.getMessage(), null);
            }
        }
        return new UserGetAllResponse(StatusTable.INTERNAL_SERVER_ERROR, StatusTable.INTERNAL_SERVER_ERROR.getMessage(), null);

    }

    public StatusResponse adminUpdateUser(String newPass, String id) throws IOException {
        AdminUpdateUserRequest msg = new AdminUpdateUserRequest(newPass, SessionManager.getInstance().getToken(), id);
        String json = gson.toJson(msg);
        client.send(json);
        String response = client.receive();
        if (response != null) {
            JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
            String status = jsonObject.has("status") ? jsonObject.get("status").getAsString() : "";
            return switch (status) {
                case "200" -> new StatusResponse(StatusTable.OK);
                case "400" -> new StatusResponse(StatusTable.BAD);
                case "401" -> new StatusResponse(StatusTable.UNAUTHORIZED);
                case "403" -> new StatusResponse(StatusTable.FORBIDDEN);
                case "404" -> new StatusResponse(StatusTable.NOT_FOUND);
                case "405" -> new StatusResponse(StatusTable.INVALID_INPUT);
                case "422" -> new StatusResponse(StatusTable.UNPROCESSABLE_ENTITY);
                default -> new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
            };
        }
        return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
    }
}
