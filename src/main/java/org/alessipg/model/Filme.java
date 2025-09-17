package org.alessipg.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.ElementCollection;

@Entity
public class Filme implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String titulo;
    private String diretor;
    private int ano;
    @ElementCollection
    private List<String> generos;
    private float nota;
    private int qtdAvaliacoes;
    private String sinopse;

    public Filme(String titulo, String diretor, int ano, List<String> generos, String sinopse, Integer qtdAvaliacoes, Float nota) {
        this.titulo = titulo;
        this.ano = ano;
        this.generos = generos;
        this.diretor = diretor;
        this.qtdAvaliacoes = (qtdAvaliacoes != null) ? qtdAvaliacoes : 0;
        this.sinopse = sinopse;
        this.nota = (nota != null) ? nota : 0f;
    }

    public Filme() {

    }

    private Filme criar(String titulo, String diretor, int ano, List<String> generos, String sinopse){
        return new Filme(titulo, diretor, ano, generos, sinopse, null, null);
    }



    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDiretor() {
        return diretor;
    }

    public void setDiretor(String diretor) {
        this.diretor = diretor;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public List<String> getGeneros() {
        return generos;
    }

    public void setGeneros(List<String> generos) {
        this.generos = generos;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public int getQtdAvaliacoes() {
        return qtdAvaliacoes;
    }

    public void setQtdAvaliacoes(int qtdAvaliacoes) {
        this.qtdAvaliacoes = qtdAvaliacoes;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }
}
