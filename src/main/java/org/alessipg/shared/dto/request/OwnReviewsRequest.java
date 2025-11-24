package org.alessipg.shared.dto.request;

public record OwnReviewsRequest(String operacao, String token) {
    public OwnReviewsRequest(String token) {
        this("LISTAR_REVIEWS_USUARIO", token);
    }
}
