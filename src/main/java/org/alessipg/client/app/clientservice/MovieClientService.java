package org.alessipg.client.app.clientservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.client.infra.tcp.TcpClient;
import org.alessipg.server.app.model.Movie;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.dto.request.MovieCreateRequest;
import org.alessipg.shared.dto.request.MovieDeleteRequest;
import org.alessipg.shared.dto.request.MovieUpdateRequest;
import org.alessipg.shared.dto.request.MovieGetAllRequest;
import org.alessipg.shared.dto.response.MovieGetAllResponse;
import org.alessipg.shared.dto.util.MovieRecord;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class MovieClientService {

    private final Gson gson;
    private final TcpClient client;

    public MovieClientService(Gson gson) {
        this.gson = gson;
        this.client = SessionManager.getClient();
    }

    public StatusTable create(MovieCreateRequest msg) throws IOException {
        String json = gson.toJson(msg);
        client.send(json);
        String response = client.receive();
        if (response != null) {
            JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
            String status = jsonObject.has("status") ? jsonObject.get("status").getAsString() : "";
            switch (status) {
                case "201":
                    return StatusTable.CREATED;
                case "400":
                    return StatusTable.BAD;
                case "401":
                    return StatusTable.UNAUTHORIZED;
                case "403":
                    return StatusTable.FORBIDDEN;
                case "409":
                    return StatusTable.ALREADY_EXISTS;
                case "422":
                    return StatusTable.UNPROCESSABLE_ENTITY;
                default:
                    return StatusTable.INTERNAL_SERVER_ERROR;
            }
        }
        return StatusTable.INTERNAL_SERVER_ERROR;
    }

    public MovieGetAllResponse getAll() throws IOException {
        MovieGetAllRequest msg = new MovieGetAllRequest(SessionManager.getInstance().getToken());
        String json = gson.toJson(msg);
        client.send(json);
        String response = client.receive();
        if (response != null) {
            JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
            String status = jsonObject.has("status") ? jsonObject.get("status").getAsString() : "";
            switch (status) {
                case "200":
                    if (!jsonObject.has("filmes")) {
                        return new MovieGetAllResponse(StatusTable.UNPROCESSABLE_ENTITY, null);
                    }
                    JsonArray moviesResponse = jsonObject.getAsJsonArray("filmes");
                    List<MovieRecord> movies = new ArrayList<>();
                    for (JsonElement m : moviesResponse) {
                        MovieRecord movie = gson.fromJson(m, MovieRecord.class);
                        if (movie == null)
                            return new MovieGetAllResponse(StatusTable.UNPROCESSABLE_ENTITY, null);
                        movies.add(movie);
                    }
                    return new MovieGetAllResponse(StatusTable.OK, movies);
                case "400":
                    return new MovieGetAllResponse(StatusTable.BAD, null);
                case "401":
                    return new MovieGetAllResponse(StatusTable.UNAUTHORIZED, null);
                case "403":
                    return new MovieGetAllResponse(StatusTable.FORBIDDEN, null);
                case "422":
                    return new MovieGetAllResponse(StatusTable.UNPROCESSABLE_ENTITY, null);
                case "404":
                    return new MovieGetAllResponse(StatusTable.NOT_FOUND, null);
                default:
                    return new MovieGetAllResponse(StatusTable.INTERNAL_SERVER_ERROR, null);
            }
        }
        return null;
    }

    public StatusTable edit(MovieRecord movie) {
        MovieUpdateRequest msg = new MovieUpdateRequest(movie, SessionManager.getInstance().getToken());
        String json = gson.toJson(msg);
        try {
            client.send(json);
            String response = client.receive();
            if (response != null) {
                JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
                String status = jsonObject.has("status") ? jsonObject.get("status").getAsString() : "";
                switch (status) {
                    case "200":
                        return StatusTable.OK;
                    case "400":
                        return StatusTable.BAD;
                    case "401":
                        return StatusTable.UNAUTHORIZED;
                    case "403":
                        return StatusTable.FORBIDDEN;
                    case "404":
                        return StatusTable.NOT_FOUND;
                    case "422":
                        return StatusTable.UNPROCESSABLE_ENTITY;
                    default:
                        return StatusTable.INTERNAL_SERVER_ERROR;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public StatusTable delete(String id) {
        MovieDeleteRequest msg = new MovieDeleteRequest(id, SessionManager.getInstance().getToken());
        String json = gson.toJson(msg);
        try {
            client.send(json);
            String response = client.receive();
            if (response != null) {
                JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
                String status = jsonObject.has("status") ? jsonObject.get("status").getAsString() : "";
                switch (status) {
                    case "200":
                        return StatusTable.OK;
                    case "400":
                        return StatusTable.BAD;
                    case "401":
                        return StatusTable.UNAUTHORIZED;
                    case "403":
                        return StatusTable.FORBIDDEN;
                    case "404":
                        return StatusTable.NOT_FOUND;
                    case "422":
                        return StatusTable.UNPROCESSABLE_ENTITY;
                    default:
                        return StatusTable.INTERNAL_SERVER_ERROR;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
