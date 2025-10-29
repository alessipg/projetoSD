package org.alessipg.shared.dto.response;

import org.alessipg.shared.enums.StatusTable;

public record UserSelfGetResponse(String status, String usuario, String mensagem) {
    public UserSelfGetResponse(StatusTable status, String usuario){
        this(String.valueOf(status.getStatus()),usuario, status.getMessage());
    }
}
