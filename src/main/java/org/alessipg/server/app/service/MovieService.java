package org.alessipg.server.app.service;

import java.util.ArrayList;
import java.util.List;

import org.alessipg.server.infra.repo.MovieRepository;
import org.alessipg.shared.domain.model.Movie;
import org.alessipg.shared.enums.Genre;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.records.MovieRecord;
import org.alessipg.shared.records.StatusResponse;

public class MovieService {
    private final MovieRepository movieRepository;
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public StatusResponse create(MovieRecord movieRecord) {
        List<Genre> mappedGenres = new ArrayList<>();
        for(String g : movieRecord.genero())
            mappedGenres.add(Genre.from(g));
        
        Movie movie = new Movie(
            movieRecord.titulo(),
            movieRecord.diretor(),
            Integer.parseInt(movieRecord.ano()),
            mappedGenres,
            movieRecord.sinopse(),
            0,
            0.0f
        );
        try{
            persist(movie);
            return new StatusResponse(StatusTable.CREATED);
        } catch(Exception e){
            return new StatusResponse(StatusTable.BAD);
        }
    }

    private void persist(Movie movie) {
        movieRepository.save(movie);
    }

}
