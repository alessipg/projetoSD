package org.alessipg.shared.dto.request;

public record MovieGetByIdRequest(String operacao, String id_filme, String token) {
    public MovieGetByIdRequest(String id, String token) {
        this("BUSCAR_FILME_ID", id, token);
    }
}

