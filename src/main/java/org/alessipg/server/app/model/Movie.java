package org.alessipg.server.app.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.NoArgsConstructor;
import org.alessipg.shared.dto.util.MovieRecord;
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
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Review> reviews;

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

    public void updateRating(int newScore) {
        if(this.ratingCount == 0){
            this.score = newScore;
            this.ratingCount = 1;
            return;
        }
        this.score = (this.score * this.ratingCount + newScore) / this.ratingCount+1;
        this.ratingCount += 1;

    }
}
