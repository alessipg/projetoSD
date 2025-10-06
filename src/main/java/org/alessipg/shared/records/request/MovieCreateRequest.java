package org.alessipg.shared.records.request;

import java.util.List;

import org.alessipg.shared.records.util.MovieRecord;



public record MovieCreateRequest(String operacao, MovieRecord filme,String token) {

    public MovieCreateRequest(String titulo, String diretor, Integer ano, List<String> genero,
            String sinopse, String token) {
        this("CRIAR_FILME", new MovieRecord(titulo, diretor, ano != null ? ano.toString() : null, genero, sinopse), token);
    }
}
