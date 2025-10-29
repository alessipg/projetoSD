package org.alessipg.shared.dto.util;

public record ReviewRecord (
    int id,
    int id_filme,
    String nome_usuario,
    String titulo,
    String descricao,
    String data,
    int nota
){
}
