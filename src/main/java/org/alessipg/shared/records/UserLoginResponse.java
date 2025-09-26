package org.alessipg.shared.records;

import org.alessipg.shared.enums.StatusTable;

public record UserLoginResponse(String status, String token) {
    public UserLoginResponse(StatusTable status, String token) {
        this(String.valueOf(status.getCode()), token);
    }
}
