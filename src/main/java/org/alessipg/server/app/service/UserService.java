package org.alessipg.server.app.service;

import org.alessipg.shared.domain.model.User;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.records.StatusResponse;
import org.alessipg.shared.records.UserSelfGetResponse;
import org.alessipg.server.infra.repo.UserRepository;
import org.alessipg.server.util.JwtUtil;

import java.util.Optional;

public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository usuarioRepository) {
    this.userRepository = usuarioRepository;
  }

  public StatusResponse create(String name, String password) {
    if (name == null || name.isBlank() || password == null || password.isBlank()) {
      return new StatusResponse(StatusTable.BAD);
    }

    if (findByName(name).isPresent()) {
      return new StatusResponse(StatusTable.ALREADY_EXISTS);
    }

    try {
      persist(new User(name, password));
      return new StatusResponse(StatusTable.CREATED);
    } catch (Exception e) {
      return new StatusResponse(StatusTable.BAD);
    }
  }

  public Optional<User> findByName(String name) {
    return userRepository.findByNome(name);
  }

  private User persist(User user) {
    return userRepository.save(user);
  }

  public UserSelfGetResponse selfGet(String token) {
    String user = JwtUtil.validarToken(token)
    .getClaim("usuario").asString();
    if(user != null){
      return new UserSelfGetResponse(StatusTable.OK,user);
    }
    return new UserSelfGetResponse(StatusTable.UNPROCESSABLE_ENTITY, null);
  }
}
