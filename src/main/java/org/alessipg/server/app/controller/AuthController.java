package org.alessipg.server.app.controller;

import org.alessipg.server.app.service.AuthService;
import org.alessipg.server.util.JwtUtil;
import org.alessipg.shared.dto.response.StatusResponse;
import org.alessipg.shared.dto.response.UserLoginResponse;
import org.alessipg.shared.enums.StatusTable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class AuthController {
    private final AuthService authService;
    private final Gson gson;

    public AuthController(AuthService authService, Gson gson) {
        this.authService = authService;
        this.gson = gson;
    }

    public String login(JsonObject json, String clientAddress) {
        try {
            if (!json.has("usuario") || !json.has("senha")) {
                return gson.toJson(new UserLoginResponse(StatusTable.UNPROCESSABLE_ENTITY, null));
            }
            String usuario = json.get("usuario").getAsString();
            String senha = json.get("senha").getAsString();
            UserLoginResponse status = authService.login(usuario, senha, clientAddress);
            return gson.toJson(status);
        } catch (Exception e) {
            System.out.println("Controller - Erro ao fazer login: " + e.getMessage());
            return gson.toJson(new UserLoginResponse(StatusTable.INTERNAL_SERVER_ERROR, null));
        }
    }

    // TODO: a way to remove user when the client is closed
    public String logout(JsonObject json, String clientAddress) {
        try {
            if (!json.has("token")) {
                return gson.toJson(new StatusResponse(StatusTable.UNPROCESSABLE_ENTITY));
            }
            String token = json.get("token").getAsString();

            StatusResponse status = authService.logout(JwtUtil.validarToken(token), clientAddress);
            return gson.toJson(status);
        } catch (Exception e) {
            System.out.println("Controller - Erro ao fazer logout: " + e.getMessage());
            return gson.toJson(new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR));
        }
    }
}
