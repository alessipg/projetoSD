package org.alessipg.server.app.controller;

import org.alessipg.server.app.service.UsuarioService;

public class UsuarioController {
    private final UsuarioService usuarioService;
  
    public UsuarioController(UsuarioService usuarioService) {
      this.usuarioService = usuarioService;
    }
  
//    public UsuarioResponse criar(UsuarioCreateRequest req) {
//      // validar req (nome não vazio, senha com política)
//      Usuario usuario = usuarioService.criarUsuario(req.getNome(), req.getSenha());
//      return new UsuarioResponse(usuario.getId(), usuario.getNome());
//    }
  } 
    

