package org.alessipg.shared.records.request;

public record MovieGetAllRequest(String operacao) {
    public MovieGetAllRequest(){
        this("LISTAR_FILMES");
    }
}
