package org.alessipg.shared.records;

import java.util.List;

public record MovieRecord(String titulo, String diretor, String ano, List<String> genero, String sinopse) {
}
