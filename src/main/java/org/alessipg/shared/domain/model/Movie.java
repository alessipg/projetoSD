package org.alessipg.shared.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.alessipg.shared.enums.Genre;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Movie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "varchar(30)")
    private String title;
    private String director;
    private int year;
    @ElementCollection(targetClass = Genre.class)
    @CollectionTable(name = "movie_genre", joinColumns = @JoinColumn(name = "movie_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private List<Genre> genres = new ArrayList<>();
    private float score;
    private int ratingCount;
    @Column(columnDefinition = "varchar(250)")
    private String synopsis;

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

    public Movie() {
    }
}
