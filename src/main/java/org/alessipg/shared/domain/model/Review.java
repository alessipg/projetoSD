package org.alessipg.shared.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    public String getDataFormatada() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(data);
    }
}
