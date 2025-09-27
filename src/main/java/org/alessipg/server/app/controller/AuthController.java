package org.alessipg.server.app.controller;

import org.alessipg.server.app.service.AuthService;
import org.alessipg.server.util.JwtUtil;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.records.StatusResponse;
import org.alessipg.shared.records.UserLoginResponse;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class AuthController {
    private final AuthService authService;
    private final Gson gson;

    public AuthController(AuthService authService) {
        this.authService = authService;
        this.gson = new GsonBuilder().create();
    }

    public String login(JsonObject json) {
        String usuario = json.get("usuario").getAsString();
        String senha = json.get("senha").getAsString();
        System.out.println("Login attempt for user: " + usuario);

        UserLoginResponse status = authService.login(usuario, senha);
        System.out.println(status);
        System.out.println(gson.toJson(status));
        return gson.toJson(status);
    }

    public String logout(JsonObject json) {
        String token = json.get("token").getAsString();
        
        StatusResponse status = authService.logout(JwtUtil.validarToken(token));
        return gson.toJson(status);
    }
}
