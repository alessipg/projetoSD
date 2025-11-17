package org.alessipg.shared.dto.response;

import org.alessipg.shared.dto.util.MovieRecord;
import org.alessipg.shared.enums.StatusTable;

public record MovieGetByIdResponse(String status, String mensagem, MovieRecord filme) {
    public MovieGetByIdResponse(StatusTable status, MovieRecord movie){
        this(status.name(), status.getMessage(), movie);
    }
}
