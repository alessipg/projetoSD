package org.alessipg.shared.records;

import java.util.List;



public record MovieCreateRequest(String operacao, MovieRecord filme,String token) {

    public MovieCreateRequest(String titulo, String diretor, Integer ano, List<String> genero,
            String sinopse, String token) {
        this("CRIAR_FILME", new MovieRecord(titulo, diretor, ano != null ? ano.toString() : null, genero, sinopse), token);
    }
}
