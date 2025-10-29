package org.alessipg.shared.dto.util;

import java.util.List;
import java.util.stream.Collectors;
import org.alessipg.shared.enums.Genre;

public record MovieRecord(String id, String titulo, String diretor, String ano, List<String> genero, String nota,
        String qtd_avaliacoes, String sinopse) {

    // Factory para aceitar List<Genre> sem conflitar com o construtor canônico
    public static MovieRecord fromGenres(int id, String titulo, String diretor, int ano, List<Genre> genero,
            double nota, int qtd_avaliacoes, String sinopse) {
        List<String> generoStrings = genero == null
                ? List.of()
                : genero.stream()
                        .map(g -> g.getDisplayName())
                        .collect(Collectors.toList());

        return new MovieRecord(String.valueOf(id), titulo, diretor, String.valueOf(ano), generoStrings,
                String.valueOf(nota), String.valueOf(qtd_avaliacoes), sinopse);
    }
}
