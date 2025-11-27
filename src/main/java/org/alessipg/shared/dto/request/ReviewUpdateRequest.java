package org.alessipg.shared.dto.request;

import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.shared.dto.util.ReviewRecord;

public record ReviewUpdateRequest(String operacao, ReviewRecord review, String token) {
    public ReviewUpdateRequest(String id,
                               String title,
                               String description,
                               int rating,
                               String token) {
        this("EDITAR_REVIEW",
                new ReviewRecord(id, null, null, title, description, null, String.valueOf(rating),null),
                token);
    }
}
