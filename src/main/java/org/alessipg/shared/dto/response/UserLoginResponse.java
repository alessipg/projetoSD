package org.alessipg.shared.dto.response;

import org.alessipg.shared.enums.StatusTable;

public record UserLoginResponse(String status, String token, String mensagem) {
    public UserLoginResponse(StatusTable status, String token) {
        this(String.valueOf(status.getStatus()), token, status.getMessage());
    }
}
