package org.alessipg.server.app.service;

import org.alessipg.shared.domain.model.Usuario;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.records.StatusResponse;
import org.alessipg.shared.records.UserLoginResponse;
import org.alessipg.server.infra.repo.UsuarioRepository;
import org.alessipg.server.util.JwtUtil;

import java.util.Optional;

public class UsuarioService {
  private final UsuarioRepository usuarioRepository;

  public UsuarioService(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  public StatusResponse criar(String nome, String senha) {
    Optional<Usuario> usuarioOpt = buscarPorNome(nome);
    if (usuarioOpt.isPresent()) {
      return new StatusResponse(StatusTable.ALREADY_EXISTS);
    }

    Usuario novoUsuario = new Usuario();
    novoUsuario.setNome(nome);
    novoUsuario.setSenha(senha);

    if (criar(novoUsuario))
      return new StatusResponse(StatusTable.CREATED);
    else
      return new StatusResponse(StatusTable.BAD);

  }

  public Optional<Usuario> buscarPorNome(String nome) {
    return usuarioRepository.findByNome(nome);
  }

  public boolean criar(Usuario novoUsuario) {
    return usuarioRepository.save(novoUsuario) != null;
  }
  
}
