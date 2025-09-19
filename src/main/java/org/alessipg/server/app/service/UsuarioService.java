package org.alessipg.server.app.service;

import org.alessipg.shared.domain.model.Usuario;
import org.alessipg.server.infra.repo.UsuarioRepository;
import java.util.Optional;

public class UsuarioService {
  private final UsuarioRepository usuarioRepository;

  public UsuarioService(UsuarioRepository repo) {
    this.usuarioRepository = repo;
  }

  public Usuario criarUsuario(String nome, String senhaPlana) {
    Usuario usuario = new Usuario(nome, senhaPlana);
    return usuarioRepository.save(usuario);
  }

  public Optional<Usuario> buscarPorNome(String nome) {
    return usuarioRepository.findByNome(nome);
  }
}
