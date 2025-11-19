package org.alessipg.shared.dto.response;

import org.alessipg.shared.dto.util.ReviewRecord;

import java.util.List;

public record OwnReviewsResponse(String status, String mensagem, List<ReviewRecord> reviews) {

}
