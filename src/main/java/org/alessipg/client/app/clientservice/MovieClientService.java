package org.alessipg.client.app.clientservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.client.infra.tcp.TcpClient;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.records.request.MovieCreateRequest;
import org.alessipg.shared.records.request.MovieGetAllRequest;
import org.alessipg.shared.records.response.MovieGetAllResponse;
import org.alessipg.shared.records.util.MovieRecord;

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
        MovieGetAllRequest msg = new MovieGetAllRequest();
        String json = gson.toJson(msg);
        client.send(json);
        String response = client.receive();
        if (response != null) {
            JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
            String status = jsonObject.has("status") ? jsonObject.get("status").getAsString() : "";
            switch (status) {
                case "200":
                    JsonArray moviesResponse = jsonObject.getAsJsonArray("filmes");
                    List<MovieRecord> movies = new ArrayList<>();
                    for (JsonElement m : moviesResponse) {
                        movies.add(gson.fromJson(m, MovieRecord.class));
                    }
                    return new MovieGetAllResponse(StatusTable.OK, movies);
                case "400":
                case "401":
                case "409":
                case "422":
                case "404":
                default:

            }
        }
        return null;
    }
}
