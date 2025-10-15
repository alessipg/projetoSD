package org.alessipg.shared.records.request;

import org.alessipg.shared.records.util.MovieRecord;

public record MovieUpdateRequest(
        String operacao,
        MovieRecord filme,
        String token
) {
    public MovieUpdateRequest(MovieRecord movie, String token) {
        this(
                "EDITAR_FILME",
                new MovieRecord(
                        movie.id(),
                        movie.titulo(),
                        movie.diretor(),
                        movie.ano(),
                        movie.genero(),
                        null,
                        null,
                        movie.sinopse()
                ),
                token
        );
    }
}
