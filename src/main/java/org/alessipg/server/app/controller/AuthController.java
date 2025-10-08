package org.alessipg.server.app.controller;

import org.alessipg.server.app.service.AuthService;
import org.alessipg.server.util.JwtUtil;
import org.alessipg.shared.records.response.StatusResponse;
import org.alessipg.shared.records.response.UserLoginResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class AuthController {
    private final AuthService authService;
    private final Gson gson;

    public AuthController(AuthService authService, Gson gson) {
        this.authService = authService;
        this.gson = gson;
    }

    public String login(JsonObject json) {
        String usuario = json.get("usuario").getAsString();
        String senha = json.get("senha").getAsString();
        UserLoginResponse status = authService.login(usuario, senha);
        return gson.toJson(status);
    }

    // TODO: a way to remove user when the client is closed
    public String logout(JsonObject json) {
        String token = json.get("token").getAsString();
        
        StatusResponse status = authService.logout(JwtUtil.validarToken(token));
        return gson.toJson(status);
    }
}
