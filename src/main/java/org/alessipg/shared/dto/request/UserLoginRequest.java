package org.alessipg.shared.dto.request;

public record UserLoginRequest(String operacao, String usuario, String senha) {
    public UserLoginRequest(String usuario, String senha) {
        this("LOGIN", usuario, senha);
    }
}