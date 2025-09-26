package org.alessipg.server.app.service;

import java.util.Optional;

import org.alessipg.server.util.JwtUtil;
import org.alessipg.shared.domain.model.Usuario;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.records.UserLoginResponse;

public class AuthService {

    private final UsuarioService usuarioService;
    public AuthService(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }
      public UserLoginResponse login(String usuario, String senha) {
    Optional<Usuario> usuarioOpt = usuarioService.buscarPorNome(usuario);

    if (!usuarioOpt.isPresent()) {
      System.out.println("User doesn't exists.");
      return new UserLoginResponse(StatusTable.UNAUTHORIZED, null);
    }

    Usuario userOpt = usuarioOpt.get();
    if (!userOpt.getSenha().equals(senha)) {
      System.out.println("Incorrect password.");
      return new UserLoginResponse(StatusTable.UNAUTHORIZED, null);
    }

    String token = JwtUtil.gerarToken(
        userOpt.getId(),
        userOpt.getNome(),
        "usuario");
    System.out.println("Login successful. Token: " + token);
    return new UserLoginResponse(StatusTable.OK, token);
  }
}
