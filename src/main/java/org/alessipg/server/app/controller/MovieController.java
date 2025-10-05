package org.alessipg.server.app.controller;

import java.util.List;

import org.alessipg.server.app.service.MovieService;
import org.alessipg.shared.records.MovieRecord;
import org.alessipg.shared.records.StatusResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
        StatusResponse status = movieService.create(new MovieRecord(title, director, year, genres, synopsis));
        return gson.toJson(status);
    }

}
