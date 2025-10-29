package org.alessipg.shared.dto.request;

public record UserDeleteRequest(String operacao,String token) {
    public UserDeleteRequest(String token){
        this("EXCLUIR_PROPRIO_USUARIO",token);
    }
}
