package org.alessipg.shared.dto.request;

public record MovieGetAllRequest(String operacao, String token) {
    public MovieGetAllRequest(String token) {
        this("LISTAR_FILMES", token);
    }
}
