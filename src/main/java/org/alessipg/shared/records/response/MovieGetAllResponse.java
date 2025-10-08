package org.alessipg.shared.records.response;

import java.util.List;

import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.records.util.MovieRecord;

public record MovieGetAllResponse(String status, List<MovieRecord> filmes) {
    public MovieGetAllResponse(StatusTable status, List<MovieRecord> filmes){
        this(String.valueOf(status.getCode()),filmes);
    }
}
