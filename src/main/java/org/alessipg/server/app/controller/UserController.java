package org.alessipg.server.app.controller;

import org.alessipg.server.app.service.UsuarioService;
import org.alessipg.shared.records.StatusResponse;
import org.alessipg.shared.records.UserLoginResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
public class UserController {
  private final UsuarioService usuarioService;
  private final Gson gson;

  public UserController(UsuarioService usuarioService) {
    this.usuarioService = usuarioService;
    this.gson = new GsonBuilder().create();
  }

  public String create(JsonObject json) {
    JsonObject usuario = json.get("usuario").getAsJsonObject();
    String nome = usuario.get("nome").getAsString();
    String senha = usuario.get("senha").getAsString();

    StatusResponse status = usuarioService.criar(nome, senha);
    return gson.toJson(status);
  }

}
