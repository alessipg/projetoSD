package org.alessipg.shared.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.List;

import org.alessipg.shared.enums.Genero;

import jakarta.persistence.ElementCollection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Filme implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String titulo;
    private String diretor;
    private int ano;
    @ElementCollection(targetClass = Genero.class)
    @Enumerated(EnumType.STRING)
    private List<Genero> generos;
    private float nota;
    private int qtdAvaliacoes;
    private String sinopse;

    public Filme(String titulo, String diretor, int ano, List<String> generos, String sinopse, Integer qtdAvaliacoes, Float nota) {
        this.titulo = titulo;
        this.ano = ano;
        if (generos != null) {
            this.generos = generos.stream()
                .map(Genero::valueOf)
                .toList();
        } else {
            this.generos = null;
        }
        this.diretor = diretor;
        this.qtdAvaliacoes = (qtdAvaliacoes != null) ? qtdAvaliacoes : 0;
        this.sinopse = sinopse;
        this.nota = (nota != null) ? nota : 0f;
    }

    public Filme() {

    }
}
