package org.alessipg.shared.dto.response;

import java.util.List;

import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.dto.util.MovieRecord;

public record MovieGetAllResponse(String status, List<MovieRecord> filmes, String mensagem) {
    public MovieGetAllResponse(StatusTable status, List<MovieRecord> filmes){
        this(String.valueOf(status.getStatus()),filmes, status.getMessage());
    }
}
