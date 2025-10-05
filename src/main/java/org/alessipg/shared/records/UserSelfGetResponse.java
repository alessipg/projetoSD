package org.alessipg.shared.records;

import org.alessipg.shared.enums.StatusTable;

public record UserSelfGetResponse(String status, String usuario) {
    public UserSelfGetResponse(StatusTable status, String usuario){
        this(String.valueOf(status.getCode()),usuario);
    }
}
