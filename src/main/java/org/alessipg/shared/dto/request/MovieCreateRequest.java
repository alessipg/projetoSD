package org.alessipg.shared.dto.request;

import java.util.List;

import org.alessipg.shared.dto.util.MovieCreateDto;
import org.alessipg.shared.enums.Genre;


public record MovieCreateRequest(String operacao, MovieCreateDto filme, String token) {

    public MovieCreateRequest(String titulo, String diretor, Integer ano, List<Genre> genero,
            String sinopse, String token) {
        this("CRIAR_FILME", MovieCreateDto.fromGenres(titulo, diretor, (ano != null)? ano.toString(): null, genero, sinopse), token);
    }
}
