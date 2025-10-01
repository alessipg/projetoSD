package org.alessipg.shared.enums;

public enum Genero {
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

    private final String displayName;

    Genero(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
