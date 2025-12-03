package org.alessipg.shared.dto.response;

import org.alessipg.shared.dto.util.ReviewRecord;
import org.alessipg.shared.enums.StatusTable;

import java.util.List;

public record OwnReviewsResponse(String status, String mensagem, List<ReviewRecord> reviews) {
    public OwnReviewsResponse(StatusTable status, List<ReviewRecord> reviews) {
        this(String.valueOf(status.getStatus()), status.getMessage(), reviews);
    }

}
