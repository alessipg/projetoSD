package org.alessipg.server.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import lombok.Getter;
import org.alessipg.server.infra.repo.MovieRepository;
import org.alessipg.server.app.model.Movie;
import org.alessipg.shared.dto.response.MovieGetByIdResponse;
import org.alessipg.shared.enums.Genre;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.dto.response.MovieGetAllResponse;
import org.alessipg.shared.dto.response.StatusResponse;
import org.alessipg.shared.dto.util.MovieCreateDto;
import org.alessipg.shared.dto.util.MovieRecord;

public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public StatusResponse create(MovieCreateDto movieRecord) {
        try {
            // validate all input fields first
            validateMovieInput(movieRecord.titulo(), movieRecord.diretor(), movieRecord.ano(), movieRecord.genero(), movieRecord.sinopse());
            Optional<Movie> m = movieRepository.findByTitleDirectorYear(movieRecord.titulo(),
                    movieRecord.diretor(), Integer.parseInt(movieRecord.ano()));
            if (m.isPresent())
                return new StatusResponse(StatusTable.ALREADY_EXISTS);
            Movie movie = new Movie(
                    movieRecord.titulo().trim(),
                    movieRecord.diretor().trim(),
                    Integer.parseInt(movieRecord.ano()),
                    mapGenres(movieRecord.genero()),
                    normalizeSynopsis(movieRecord.sinopse()),
                    0,
                    0.0f);
            persist(movie);
            return new StatusResponse(StatusTable.CREATED);
        } catch (IllegalArgumentException e) {
            return new StatusResponse(StatusTable.INVALID_INPUT);
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
                            m.getSynopsis(),
                            null));
        }

        return new MovieGetAllResponse(StatusTable.OK, formmatedMovies);
    }

    public StatusResponse update(MovieRecord movieRecord) {
        try {
            // validate id separately (must be digits only)
            if (movieRecord.id() == null || !movieRecord.id().matches("\\d+")) {
                throw new IllegalArgumentException("id");
            }
            // validate other fields (excluding id)
            validateMovieInput(movieRecord.titulo(), movieRecord.diretor(), movieRecord.ano(), movieRecord.genero(), movieRecord.sinopse());

            Optional<Movie> m = movieRepository.findByTitleDirectorYear(movieRecord.titulo(),
                    movieRecord.diretor(), Integer.parseInt(movieRecord.ano()));
            if (m.isPresent() && m.get().getId() != Integer.parseInt(movieRecord.id()))
                return new StatusResponse(StatusTable.ALREADY_EXISTS);
            Optional<Movie> movieById = movieRepository.findById(Integer.parseInt(movieRecord.id()));
            if (movieById.isEmpty())
                return new StatusResponse(StatusTable.NOT_FOUND);
            Movie movie = movieById.get();
            movie.setTitle(movieRecord.titulo().trim());
            movie.setDirector(movieRecord.diretor().trim());
            movie.setYear(Integer.parseInt(movieRecord.ano()));
            movie.setGenres(mapGenres(movieRecord.genero()));
            movie.setSynopsis(normalizeSynopsis(movieRecord.sinopse()));
            persist(movie);
            return new StatusResponse(StatusTable.OK);
        } catch (IllegalArgumentException e) {
            return new StatusResponse(StatusTable.INVALID_INPUT);
        } catch (Exception e) {
            return new StatusResponse(StatusTable.BAD);
        }
    }

    List<Genre> mapGenres(List<String> genres) throws IllegalArgumentException {
        try {
            List<Genre> mappedGenres = new ArrayList<>();
            for (String g : genres) {
                mappedGenres.add(Genre.from(g));
            }
            return mappedGenres;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public StatusResponse delete(int id) {
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isEmpty())
            return new StatusResponse(StatusTable.NOT_FOUND);
        try {

            movieRepository.delete(movie.get());
            return new StatusResponse(StatusTable.OK);
        } catch (Exception e) {
            return new StatusResponse(StatusTable.BAD);
        }
    }

    private void validateMovieInput(String titulo, String diretor, String ano, List<String> genero, String sinopse) {
        // normalize nulls to empty strings
        String ntitulo = titulo == null ? "" : titulo.trim();
        String ndiretor = diretor == null ? "" : diretor.trim();
        String nano = ano == null ? "" : ano.trim();
        String nsinopse = sinopse == null ? "" : sinopse; // synopsis preserves internal spaces; length counts spaces

        // titulo: 3-30 chars
        if (ntitulo.length() < 3 || ntitulo.length() > 30) {
            throw new IllegalArgumentException("titulo");
        }
        // diretor: 3-30 chars
        if (ndiretor.length() < 3 || ndiretor.length() > 30) {
            throw new IllegalArgumentException("diretor");
        }
        // ano: digits only, length 3-4
        if (nano.length() < 3 || nano.length() > 4 || !nano.matches("\\d+")) {
            throw new IllegalArgumentException("ano");
        }
        // genero: must have at least one
        if (genero == null || genero.isEmpty()) {
            throw new IllegalArgumentException("genero");
        }
        // validate each genre display name (exact match)
        for (String g : genero) {
            Genre.from(g); // will throw if invalid
        }
        // sinopse: optional, but if present length must be <= 250 (count spaces)
        if (nsinopse.length() > 250) {
            throw new IllegalArgumentException("sinopse");
        }
    }

    private String normalizeSynopsis(String sinopse) {
        return sinopse == null ? "" : sinopse; // maintain spaces, already length-validated
    }

    public MovieGetByIdResponse getById(int id) {
        try {
            Optional<Movie> m = movieRepository.findById(id);
            if (m.isEmpty()) {
                return new MovieGetByIdResponse(StatusTable.NOT_FOUND, null);
            }
            Movie movie = m.get();
            MovieRecord movieRecord = MovieRecord
                    .fromGenres(movie.getId(),
                            movie.getTitle(),
                            movie.getDirector(),
                            movie.getYear(),
                            movie.getGenres(),
                            movie.getScore(),
                            movie.getRatingCount(),
                            movie.getSynopsis(),
                            movie.getReviews());
            return new MovieGetByIdResponse(StatusTable.OK, movieRecord);
        } catch (Exception e) {
            return new MovieGetByIdResponse(StatusTable.BAD, null);
        }
    }
    public void updateEntity(Movie movie) {
        movieRepository.save(movie);
    }
    public Movie getEntityById(int id) {
        return movieRepository.findById(id).orElse(null);
    }
}
