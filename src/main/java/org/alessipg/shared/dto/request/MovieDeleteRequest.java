package org.alessipg.shared.dto.request;

public record MovieDeleteRequest(String operacao, String id, String token) {
    public MovieDeleteRequest(String id, String token) {
        this("EXCLUIR_FILME", id, token);
    }
}
