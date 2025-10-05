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
    private Movie movie;
    @ManyToOne
    private User user;
    private int rating;
    private String title;
    private String description;
    private Date submitDate;

    public Review(int id, Movie movie, User user, int rating, String title, String description, Date submitDate) {
        this.id = id;
        this.movie = movie;
        this.user = user;
        this.rating = rating;
        this.title = title;
        this.description = description;
        this.submitDate = submitDate;
    }

    public Review() {

    }

    public String getFormatedDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(submitDate);
    }
}
