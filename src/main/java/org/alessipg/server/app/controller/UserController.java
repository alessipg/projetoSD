package org.alessipg.server.app.controller;

import org.alessipg.server.app.service.UserService;
import org.alessipg.shared.dto.response.StatusResponse;
import org.alessipg.shared.dto.response.UserGetAllResponse;
import org.alessipg.shared.dto.response.UserSelfGetResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.alessipg.shared.dto.util.UserRecord;

public class UserController {
    private final UserService userService;
    private final Gson gson;

    public UserController(UserService usuarioService, Gson gson) {
        this.userService = usuarioService;
        this.gson = gson;
    }
    public String create(JsonObject json) {
        if(!json.has("usuario")){
            return gson.toJson(new StatusResponse(org.alessipg.shared.enums.StatusTable.UNPROCESSABLE_ENTITY));
        }
        UserRecord user = gson.fromJson(json.get("usuario"), UserRecord.class);
        if (user == null)
            return gson.toJson(new StatusResponse(org.alessipg.shared.enums.StatusTable.UNPROCESSABLE_ENTITY));

        StatusResponse status = userService.create(user);
        return gson.toJson(status);
    }

    public String selfGet(JsonObject json) {
        if (!json.has("token")) {
            return gson.toJson(new StatusResponse(org.alessipg.shared.enums.StatusTable.UNPROCESSABLE_ENTITY));
        }
        String token = json.get("token").getAsString();
        UserSelfGetResponse user = userService.selfGet(token);
        return gson.toJson(user);
    }

    public String update(JsonObject json) {
        if(!json.has("usuario") || !json.has("token")){
            return gson.toJson(new StatusResponse(org.alessipg.shared.enums.StatusTable.UNPROCESSABLE_ENTITY));
        }
        JsonObject usuario = json.get("usuario").getAsJsonObject();
        if(!usuario.has("senha")){
            return gson.toJson(new StatusResponse(org.alessipg.shared.enums.StatusTable.UNPROCESSABLE_ENTITY));
        }
        String password = usuario.get("senha").getAsString();
        String token = json.get("token").getAsString();
        StatusResponse status = userService.update(token, password);
        return gson.toJson(status);
    }

    public String selfDelete(JsonObject json) {
        String token = json.has("token") ? json.get("token").getAsString() : "";
        StatusResponse status = userService.selfDelete(token);
        return gson.toJson(status);
    }

    public String getAll(JsonObject json) {
        String token = json.has("token") ? json.get("token").getAsString() : "";
        UserGetAllResponse status = userService.getAll(token);
        return gson.toJson(status);
    }

    public String adminUpdate(JsonObject json) {
        String token = json.has("token") ? json.get("token").getAsString() : "";
        JsonObject usuario = json.get("usuario").getAsJsonObject();
        String password = usuario.get("senha").getAsString();
        int userId = json.has("id")? json.get("id").getAsInt() : -1;
        if(userId == -1){
            return gson.toJson(new StatusResponse(org.alessipg.shared.enums.StatusTable.UNPROCESSABLE_ENTITY));
        }
        StatusResponse status = userService.adminUpdate(token, userId, password);
        return gson.toJson(status);
    }

    public String adminDelete(JsonObject json) {
        String token = json.has("token") ? json.get("token").getAsString() : "";
        int userId = json.has("id")? json.get("id").getAsInt() : -1;
        if(userId == -1){
            return gson.toJson(new StatusResponse(org.alessipg.shared.enums.StatusTable.UNPROCESSABLE_ENTITY));
        }
        StatusResponse status = userService.adminDelete(token, userId);
        return gson.toJson(status);
    }
}
