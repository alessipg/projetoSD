package org.alessipg.server.app.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.NoArgsConstructor;
import org.alessipg.shared.enums.Genre;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Movie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "varchar(30)")
    private String title;
    private String director;
    private int year;
    @ElementCollection(targetClass = Genre.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "movie_genre", joinColumns = @JoinColumn(name = "movie_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private List<Genre> genres = new ArrayList<>();
    private float score;
    private int ratingCount;
    @Column(columnDefinition = "varchar(250)")
    private String synopsis;
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    public Movie(String title, String director, int year, List<Genre> genres, String synopsis, Integer ratingCount,
                 Float score) {
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.director = director;
        this.ratingCount = (ratingCount != null) ? ratingCount : 0;
        this.synopsis = synopsis;
        this.score = (score != null) ? score : 0f;
    }

    public void updateRating(int newScore, boolean isRemoving) {
        System.out.println("Atualizando pontuacao de filme: " + this.title+", pontuacao atual: " + this.score + ", quantidade de avaliacoes: " + this.ratingCount);
        if (this.ratingCount == 0) {
            System.out.println("Primeiro if");
            this.score = newScore;
            this.ratingCount = 1;
            return;
        }
        if (isRemoving) {
            System.out.println("Removendo avaliacao");
            if (this.ratingCount == 1) {
                System.out.println("Ultima avaliacao removida");
                this.score = 0;
                this.ratingCount = 0;
                return;
            }
            System.out.println("Antes: " + this.score + ", " + this.ratingCount);
            this.score = (this.score * this.ratingCount - newScore) / (this.ratingCount - 1);
            this.ratingCount -= 1;
            System.out.println("Depois: " + this.score + ", " + this.ratingCount);
            return;
        }
        System.out.println("Conta: ("+this.score+" * "+ this.ratingCount +" + " + newScore +") / " + (this.ratingCount + 1));
        this.score = (this.score * this.ratingCount + newScore) / (this.ratingCount + 1);
        System.out.println("Nova pontuacao: " + this.score);
        this.ratingCount += 1;
    }
}
