package org.alessipg.shared.dto.request;

public record UserCreateRequest(String operacao, Usuario usuario) {
    public UserCreateRequest(String nome, String senha) {
        this("CRIAR_USUARIO", new Usuario(nome, senha));
    }

    public record Usuario(String nome, String senha) {}
}