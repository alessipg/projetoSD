package org.alessipg.server.app.controller;

import org.alessipg.server.app.service.UserService;
import org.alessipg.shared.records.response.StatusResponse;
import org.alessipg.shared.records.response.UserSelfGetResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class UserController {
  private final UserService userService;
  private final Gson gson;

  public UserController(UserService usuarioService, Gson gson) {
    this.userService = usuarioService;
    this.gson = gson;
  }

  // TODO: implement throws for inexistent values
  public String create(JsonObject json) {
    JsonObject usuario = json.get("usuario").getAsJsonObject();
    String nome = usuario.get("nome").getAsString();
    String senha = usuario.get("senha").getAsString();

    StatusResponse status = userService.create(nome, senha);
    return gson.toJson(status);
  }

  public String selfGet(JsonObject json) {
    String token = json.get("token").getAsString();
    UserSelfGetResponse user = userService.selfGet(token);
    return gson.toJson(user);
  }

  public String update(JsonObject json) {
    JsonObject usuario = json.get("usuario").getAsJsonObject();
    String password = usuario.get("senha").getAsString();
    String token = json.get("token").getAsString();
    StatusResponse status = userService.update(token, password);
    return gson.toJson(status);
  }

  public String selfDelete(JsonObject json) {
    String token = json.get("token").getAsString();
    StatusResponse status = userService.selfDelete(token);
    return gson.toJson(status);
  }

}
