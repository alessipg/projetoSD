package org.alessipg.shared.dto.util;

import java.util.List;
import java.util.stream.Collectors;

import org.alessipg.server.app.model.Review;
import org.alessipg.shared.enums.Genre;

public record MovieRecord(String id, String titulo, String diretor, String ano, List<String> genero, String nota,
                          String qtd_avaliacoes, String sinopse, List<ReviewRecord> reviews) {

    public static MovieRecord fromGenres(int id, String titulo, String diretor, int ano, List<Genre> genero,
                                         double nota, int qtd_avaliacoes, String sinopse, List<Review> reviews) {
        List<String> generoStrings = genero == null
                ? List.of()
                : genero.stream()
                .map(Genre::getDisplayName)
                .collect(Collectors.toList());
        List<ReviewRecord> reviewRecords = reviews == null
                ? null
                : reviews.stream()
                .map(r -> new ReviewRecord(String.valueOf(r.getId()), String.valueOf(id), r.getUser().getName(),
                        r.getTitle(), r.getDescription(), r.getFormatedDate(), String.valueOf(r.getRating()),String.valueOf(r.isEdited())))
                .collect(Collectors.toList());
        return new MovieRecord(String.valueOf(id), titulo, diretor, String.valueOf(ano), generoStrings,
                String.valueOf(nota), String.valueOf(qtd_avaliacoes), sinopse, reviewRecords);
    }
}
