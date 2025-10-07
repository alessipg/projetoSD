package org.alessipg.shared.records.response;

import org.alessipg.shared.enums.StatusTable;

public record StatusResponse(String status) {
    public StatusResponse(StatusTable status) {
        this(String.valueOf(status.getCode()));
    }
}
