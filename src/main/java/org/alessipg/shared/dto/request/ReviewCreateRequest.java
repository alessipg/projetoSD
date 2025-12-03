package org.alessipg.shared.dto.request;

import org.alessipg.shared.dto.util.ReviewRecord;

public record ReviewCreateRequest(String operacao, ReviewRecord review, String token) {
    public ReviewCreateRequest(String title, String description, Integer currentRating, int id_filme, String token) {
        this("CRIAR_REVIEW", new ReviewRecord(null, String.valueOf(id_filme),null, title, description, null,String.valueOf(currentRating),null), token);
    }
}
