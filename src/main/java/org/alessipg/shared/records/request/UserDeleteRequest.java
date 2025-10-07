package org.alessipg.shared.records.request;

public record UserDeleteRequest(String operacao,String token) {
    public UserDeleteRequest(String token){
        this("EXCLUIR_PROPRIO_USUARIO",token);
    }
}
