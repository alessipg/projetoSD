package org.alessipg.server.infra.repo;

import org.alessipg.shared.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository {
  Usuario save(Usuario usuario);
  Optional<Usuario> findByNome(String nome);
} 
    
