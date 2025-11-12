package org.alessipg.shared.dto.request;

import org.alessipg.shared.dto.util.UserRecord;

public record AdminUpdateUserRequest(String operacao, UserRecord usuario, String token, String id) {
    public AdminUpdateUserRequest(String newPass, String token, String id) {
        this("ADMIN_EDITAR_USUARIO", new UserRecord(null,newPass), token, id);
    }
}
