package org.alessipg.shared.dto.request;

public record MovieGetAllRequest(String operacao) {
    public MovieGetAllRequest(){
        this("LISTAR_FILMES");
    }
}
