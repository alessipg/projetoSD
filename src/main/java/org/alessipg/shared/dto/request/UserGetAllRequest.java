package org.alessipg.shared.dto.request;

public record UserGetAllRequest(String operacao, String token) {
    public UserGetAllRequest(String token) {
        this("LISTAR_USUARIOS", token);
    }
}
