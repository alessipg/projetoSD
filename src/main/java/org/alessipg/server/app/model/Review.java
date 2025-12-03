package org.alessipg.server.app.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Review implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private int rating;
    private String title;
    private String description;
    private Date submitDate;
    private boolean edited;

    public String getFormatedDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(submitDate);
    }
}
