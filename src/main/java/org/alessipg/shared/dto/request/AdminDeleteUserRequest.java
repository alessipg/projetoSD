package org.alessipg.shared.dto.request;

public record AdminDeleteUserRequest(String operacao, String id, String token) {
    public AdminDeleteUserRequest(String id, String token) {
        this("ADMIN_EXCLUIR_USUARIO", id, token);
    }
}
