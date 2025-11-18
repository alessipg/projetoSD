package org.alessipg.server.app.controller;

import java.util.List;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.alessipg.server.app.service.AuthService;
import org.alessipg.server.app.service.MovieService;
import org.alessipg.server.util.JwtUtil;
import org.alessipg.shared.dto.response.MovieGetAllResponse;
import org.alessipg.shared.dto.response.MovieGetByIdResponse;
import org.alessipg.shared.dto.response.StatusResponse;
import org.alessipg.shared.dto.util.MovieCreateDto;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.alessipg.shared.dto.util.MovieRecord;
import org.alessipg.shared.enums.StatusTable;

public class MovieController {

    private final MovieService movieService;
    private final AuthService authService;
    private final Gson gson;

    public MovieController(MovieService movieService, Gson gson, AuthService authService) {
        this.authService = authService;
        this.movieService = movieService;
        this.gson = gson;
    }

    public String create(JsonObject json) {
        String token = json.has("token") ? json.get("token").getAsString() : null;
        if (token == null)
            return gson.toJson(new StatusResponse(StatusTable.UNPROCESSABLE_ENTITY));
        if(!authService.isAdmin(JwtUtil.validarToken(token)))
            return gson.toJson(new StatusResponse(StatusTable.FORBIDDEN));
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
        String token = json.has("token") ? json.get("token").getAsString() : null;
        if (token == null)
            return gson.toJson(new StatusResponse(StatusTable.UNPROCESSABLE_ENTITY));
        if(!authService.isAdmin(JwtUtil.validarToken(token)))
            return gson.toJson(new StatusResponse(StatusTable.FORBIDDEN));
        MovieRecord movie = gson.fromJson(json.get("filme"), MovieRecord.class);
        StatusResponse status = movieService.update(movie);
        System.out.println(status.status());
        return gson.toJson(status);
    }

    public String delete(JsonObject json) {
        String token = json.has("token") ? json.get("token").getAsString() : null;
        if (token == null)
            return gson.toJson(new StatusResponse(StatusTable.UNPROCESSABLE_ENTITY));
        if(!authService.isAdmin(JwtUtil.validarToken(token)))
            return gson.toJson(new StatusResponse(StatusTable.FORBIDDEN));
        int id = json.get("id").getAsString().isEmpty() ? 0 : Integer.parseInt(json.get("id").getAsString());
        StatusResponse status = movieService.delete(id);
        return gson.toJson(status);
        
    }

    public String getById(JsonObject json) {
        String token = json.has("token") ? json.get("token").getAsString() : null;
        if (token == null)
            return gson.toJson(new StatusResponse(StatusTable.UNPROCESSABLE_ENTITY));
        DecodedJWT decodedJWT= JwtUtil.validarToken(token);
        if(decodedJWT==null)
            return gson.toJson(new StatusResponse(StatusTable.UNAUTHORIZED));
        if(json.get("id_filme").getAsString().isEmpty())
            return gson.toJson(new StatusResponse(StatusTable.UNPROCESSABLE_ENTITY));

        int id = Integer.parseInt(json.get("id_filme").getAsString());
        MovieGetByIdResponse movie = movieService.getById(id);
        return gson.toJson(movie);
    }
}
