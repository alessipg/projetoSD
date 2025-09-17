package org.alessipg.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.ManyToOne;

@Entity
public class Review implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Filme filme;
    @ManyToOne
    private Usuario usuario;
    private int nota;
    private String titulo;
    private String descricao;
    private Date data;

    public Review(int id, Filme filme, Usuario usuario, int nota, String titulo, String descricao, Date data) {
        this.id = id;
        this.filme = filme;
        this.usuario = usuario;
        this.nota = nota;
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
    }

    public Review() {

    }

    private Review criar() {
        return new Review(id, filme, usuario, nota, titulo, descricao, new Date());
    }
    public int getId() {
        return id;
    }

    public Filme getFilme() {
        return filme;
    }

    public void setFilme(Filme filme) {
        this.filme = filme;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getData() {
        return data;
    }

    public String getDataFormatada() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(data);
    }

    public void setData(Date data) {
        this.data = data;
    }
}
