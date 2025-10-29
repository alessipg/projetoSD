package org.alessipg.shared.enums;

import lombok.Getter;

@Getter
public enum StatusTable {
    OK(200, "Sucesso: operação realizada com sucesso"),
    CREATED(201, "Sucesso: Recurso cadastrado"),
    BAD(400, "Erro: Operação não encontrada ou inválida"),
    UNAUTHORIZED(401, "Erro: Token inválido"),
    FORBIDDEN(403, "Erro: sem permissão"),
    NOT_FOUND(404, "Erro: Recurso inexistente"),
    INVALID_INPUT(405, "Erro: Campos inválidos, verifique o tipo e quantidade de caracteres"),
    ALREADY_EXISTS(409, "Erro: Recurso ja existe"),
    UNPROCESSABLE_ENTITY(422, "Erro: Chaves faltantes ou invalidas"),
    INTERNAL_SERVER_ERROR(500, "Erro: Falha interna do servidor");

    private final int status;
    private final String message;
    StatusTable(int status, String message) {
        this.status = status;
        this.message = message;
    }
    

}
