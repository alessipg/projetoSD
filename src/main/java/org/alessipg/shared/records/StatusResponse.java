package org.alessipg.shared.records;

import org.alessipg.shared.enums.StatusTable;

public record StatusResponse(String status) {
    public StatusResponse(StatusTable status) {
        this(String.valueOf(status.getCode()));
    }
}
