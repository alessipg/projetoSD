package org.alessipg.shared.records;

public record UserLoginRequest(String operacao, String user, String senha) {
    // Construtor de conveniência
    public UserLoginRequest(String user, String senha) {
        this("LOGIN", user, senha);
    }
}