package org.alessipg.server.app.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
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
    @Column(columnDefinition = "boolean default false")
    private boolean edited;

    public String getFormatedDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(submitDate);
    }
}
