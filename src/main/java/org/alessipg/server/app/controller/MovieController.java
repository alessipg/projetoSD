package org.alessipg.server.app.controller;

import java.util.List;

import org.alessipg.server.app.service.MovieService;
import org.alessipg.shared.records.response.MovieGetAllResponse;
import org.alessipg.shared.records.response.StatusResponse;
import org.alessipg.shared.records.util.MovieCreateDto;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.alessipg.shared.records.util.MovieRecord;

public class MovieController {

    private final MovieService movieService;
    private final Gson gson;

    public MovieController(MovieService movieService, Gson gson) {
        this.movieService = movieService;
        this.gson = gson;
    }

    public String create(JsonObject json) {
        JsonObject movie = json.get("filme").getAsJsonObject();
        String title = movie.get("titulo").getAsString();
        String director = movie.get("diretor").getAsString();
        String year = movie.get("ano").getAsString();
        List<String> genres = new java.util.ArrayList<>();
        JsonArray genresArray = movie.getAsJsonArray("genero");
        for (int i = 0; i < genresArray.size(); i++) {
            genres.add(genresArray.get(i).getAsString());
        }
        String synopsis = movie.get("sinopse").getAsString();
        StatusResponse status = movieService.create(new MovieCreateDto(title, director, year, genres, synopsis));
        return gson.toJson(status);
    }

    public String getAll() {
        MovieGetAllResponse movies = movieService.getAll();
        return gson.toJson(movies);
    }

    public String update(JsonObject json) {
        MovieRecord movie = gson.fromJson(json.get("filme"), MovieRecord.class);
        StatusResponse status = movieService.update(movie);
        System.out.println(status.status());
        return gson.toJson(status);
    }
}
