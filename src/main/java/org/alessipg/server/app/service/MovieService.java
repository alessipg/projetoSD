package org.alessipg.server.app.service;

import java.util.ArrayList;
import java.util.List;

import org.alessipg.server.infra.repo.MovieRepository;
import org.alessipg.shared.domain.model.Movie;
import org.alessipg.shared.enums.Genre;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.records.response.MovieGetAllResponse;
import org.alessipg.shared.records.response.StatusResponse;
import org.alessipg.shared.records.util.MovieCreateDto;
import org.alessipg.shared.records.util.MovieRecord;

public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public StatusResponse create(MovieCreateDto movieRecord) {

        Movie movie = new Movie(
                movieRecord.titulo(),
                movieRecord.diretor(),
                Integer.parseInt(movieRecord.ano()),
                mapGenres(movieRecord.genero()),
                movieRecord.sinopse(),
                0,
                0.0f);
        try {
            persist(movie);
            return new StatusResponse(StatusTable.CREATED);
        } catch (Exception e) {
            return new StatusResponse(StatusTable.BAD);
        }
    }

    private void persist(Movie movie) {
        movieRepository.save(movie);
    }

    public MovieGetAllResponse getAll() {
        List<Movie> movies = movieRepository.getAll();
        List<MovieRecord> formmatedMovies = new ArrayList<>();
        for (Movie m : movies) {
            formmatedMovies.add(MovieRecord
                    .fromGenres(m.getId(),
                            m.getTitle(),
                            m.getDirector(),
                            m.getYear(),
                            m.getGenres(),
                            m.getScore(),
                            m.getRatingCount(),
                            m.getSynopsis()));
        }

        return new MovieGetAllResponse("200", formmatedMovies);
    }

    public StatusResponse update(MovieRecord m) {
        Movie movie = movieRepository.findById(Integer.parseInt(m.id()));
        if(movie == null)
            return new StatusResponse(StatusTable.NOT_FOUND);
        movie.setTitle(m.titulo());
        movie.setDirector(m.diretor());
        movie.setYear(Integer.parseInt(m.ano()));
        movie.setGenres(mapGenres(m.genero()));
        movie.setSynopsis(m.sinopse());
        try {
            persist(movie);
            return new StatusResponse(StatusTable.OK);
        } catch (Exception e) {
            return new StatusResponse(StatusTable.BAD);
        }
    }

    List<Genre> mapGenres(List<String> genres) {
        List<Genre> mappedGenres = new ArrayList<>();
        for (String g : genres)
            mappedGenres.add(Genre.from(g));
        return mappedGenres;
    }

    public StatusResponse delete(int id) {
        Movie movie = movieRepository.findById(id);
        if (movie == null) {
            return new StatusResponse(StatusTable.NOT_FOUND);
        }
        try {
            movieRepository.delete(movie);
            return new StatusResponse(StatusTable.OK);
        } catch (Exception e) {
            return new StatusResponse(StatusTable.BAD);
        }
    }
}
