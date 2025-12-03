package org.alessipg.shared.dto.response;

import org.alessipg.shared.dto.util.MovieRecord;
import org.alessipg.shared.dto.util.ReviewRecord;
import org.alessipg.shared.enums.StatusTable;

import java.util.List;

public record MovieGetByIdResponse(String status, String mensagem, MovieRecord filme, List<ReviewRecord> reviews) {
    public MovieGetByIdResponse(StatusTable status, MovieRecord movie){
        this(String.valueOf(status.getStatus()),
             status.getMessage(),
             new MovieRecord(
                     movie.id(),
                     movie.titulo(),
                     movie.diretor(),
                     movie.ano(),
                     movie.genero(),
                     movie.nota(),
                     movie.qtd_avaliacoes(),
                     movie.sinopse(),
                     null
             ),
             movie.reviews());
    }
}
