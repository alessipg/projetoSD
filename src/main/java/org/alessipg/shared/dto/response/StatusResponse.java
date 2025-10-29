package org.alessipg.shared.dto.response;

import org.alessipg.shared.enums.StatusTable;

public record StatusResponse(String status, String mensagem) {
    public StatusResponse(StatusTable status)
    {
        this(String.valueOf(status.getStatus()), status.getMessage());
    }
}
