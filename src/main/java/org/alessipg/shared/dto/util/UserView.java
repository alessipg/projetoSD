package org.alessipg.shared.dto.util;

public record UserView(String id, String nome) {
    public UserView(int id, String nome) {
        this(String.valueOf(id), nome);
    }
}
