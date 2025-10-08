package org.alessipg.shared.enums;

import lombok.Getter;

public enum Genre {
    ACAO("Ação"),
    ANIMACAO("Animação"),
    AVENTURA("Aventura"),
    COMEDIA("Comédia"),
    DOCUMENTARIO("Documentário"),
    DRAMA("Drama"),
    FANTASIA("Fantasia"),
    FICCAO_CIENTIFICA("Ficção Científica"),
    MUSICAL("Musical"),
    ROMANCE("Romance"),
    TERROR("Terror");

    @Getter
    private final String displayName;

    Genre(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static Genre from(String s) {
        for (Genre g : values()) {
            if (g.displayName.equals(s))
                return g;
        }
        throw new IllegalArgumentException(String.valueOf(s));
    }
}
