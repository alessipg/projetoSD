package org.alessipg.shared.records.util;

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
