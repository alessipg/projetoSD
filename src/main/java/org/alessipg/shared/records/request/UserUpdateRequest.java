package org.alessipg.shared.records.request;

import org.alessipg.shared.records.util.UserRecord;

public record UserUpdateRequest(String operacao, UserRecord usuario,String token) {
    public UserUpdateRequest(UserRecord usuario,String token){
        this("EDITAR_PROPRIO_USUARIO",usuario,token);
    }
}
