package org.alessipg.shared.records.request;

public record UserRegisterRequest(String operacao, Usuario usuario) {
    public UserRegisterRequest(String nome, String senha) {
        this("CRIAR_USUARIO", new Usuario(nome, senha));
    }

    public record Usuario(String nome, String senha) {}
}