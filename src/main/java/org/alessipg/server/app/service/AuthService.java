package org.alessipg.server.app.service;

import java.util.Optional;

import org.alessipg.server.ui.ServerView;
import org.alessipg.server.util.JwtUtil;
import org.alessipg.shared.domain.model.User;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.records.response.StatusResponse;
import org.alessipg.shared.records.response.UserLoginResponse;

import com.auth0.jwt.interfaces.DecodedJWT;

public class AuthService {

  private final UserService userService;

  public AuthService(UserService userService) {
    this.userService = userService;
  }

  public UserLoginResponse login(String user, String password) {
    if (user == null || user.isBlank() || password == null || password.isBlank()) {
      return new UserLoginResponse(StatusTable.UNPROCESSABLE_ENTITY, null);
    }

    Optional<User> usuarioOpt = userService.findByName(user);

    if (!usuarioOpt.isPresent()) {
      System.out.println("Invalid credentials.");
      return new UserLoginResponse(StatusTable.NOT_FOUND, null);
    }

    User userOpt = usuarioOpt.get();
    if (!userOpt.getPassword().equals(password)) {
      System.out.println("Invalid credentials.");
      return new UserLoginResponse(StatusTable.NOT_FOUND, null);
    }

    String token = JwtUtil.generateToken(
        userOpt.getId(),
        userOpt.getName(),
        userOpt.isAdmin() ? "admin" : "usuario");
    ServerView.addUser(userOpt.getName());
    return new UserLoginResponse(StatusTable.OK, token);
  }

  public StatusResponse logout(DecodedJWT decodedJwt) {
    String user = decodedJwt.getClaim("usuario").asString();
    System.out.println("Usuário " + user + " desconectando...");
    ServerView.removeUser(user);
    return new StatusResponse(StatusTable.OK);
  }
}
