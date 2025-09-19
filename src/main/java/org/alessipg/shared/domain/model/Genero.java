package org.alessipg.shared.domain.model;

public enum Genero {
    ACAO("Ação"),
    AVENTURA("Aventura"),
    COMEDIA("Comédia"),
    DRAMA("Drama"),
    FANTASIA("Fantasia"),
    FICCAO_CIENTIFICA("Ficção Científica"),
    TERROR("Terror"),
    ROMANCE("Romance"),
    DOCUMENTARIO_MUSICAL("Documentário/Musical"),
    ANIMACAO("Animação");

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
