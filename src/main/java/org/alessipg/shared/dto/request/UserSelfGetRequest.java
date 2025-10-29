package org.alessipg.shared.dto.request;

public record UserSelfGetRequest(String operacao, String token) {
    public UserSelfGetRequest(String token){
        this("LISTAR_PROPRIO_USUARIO", token);
    }
}
