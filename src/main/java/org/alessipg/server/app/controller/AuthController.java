package org.alessipg.server.app.controller;

import org.alessipg.server.app.service.AuthService;
import org.alessipg.shared.records.UserLoginResponse;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'logout'");
    }
}
