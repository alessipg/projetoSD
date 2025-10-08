package org.alessipg.shared.records.util;

import java.util.List;
import java.util.stream.Collectors;
import org.alessipg.shared.enums.Genre;

public record MovieCreateDto(String titulo, String diretor, String ano, List<String> genero, String sinopse) {

    // Factory para aceitar List<Genre> sem conflitar com o construtor canônico
    public static MovieCreateDto fromGenres(String titulo, String diretor, String ano, List<Genre> genero,
            String sinopse) {
        List<String> generoStrings = genero == null
                ? List.of()
                : genero.stream()
                        // troque para g.name() se não houver getDisplayName()
                        .map(g -> String.valueOf(g.getDisplayName()))
                        .collect(Collectors.toList());

        return new MovieCreateDto(titulo, diretor, ano, generoStrings, sinopse);
    }
}
