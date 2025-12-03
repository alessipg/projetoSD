package org.alessipg.shared.dto.request;

public record ReviewDeleteRequest(String operacao, String id, String token) {
    public ReviewDeleteRequest(String id, String token) {
        this("EXCLUIR_REVIEW", id, token);
    }
}
