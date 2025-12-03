package org.alessipg.client.app.clientservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.client.infra.tcp.TcpClient;
import org.alessipg.shared.dto.request.*;
import org.alessipg.shared.dto.util.ReviewRecord;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.dto.response.MovieGetAllResponse;
import org.alessipg.shared.dto.util.MovieRecord;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

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
                case "405":
                    return StatusTable.INVALID_INPUT;
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
                case "422":
                    return new MovieGetAllResponse(StatusTable.UNPROCESSABLE_ENTITY, null);
                case "404":
                    return new MovieGetAllResponse(StatusTable.NOT_FOUND, null);
                default:
                    return new MovieGetAllResponse(StatusTable.INTERNAL_SERVER_ERROR, null);
            }
        }
        return new MovieGetAllResponse(StatusTable.INTERNAL_SERVER_ERROR, null);
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
                return switch (status) {
                    case "200" -> StatusTable.OK;
                    case "400" -> StatusTable.BAD;
                    case "401" -> StatusTable.UNAUTHORIZED;
                    case "403" -> StatusTable.FORBIDDEN;
                    case "404" -> StatusTable.NOT_FOUND;
                    case "405" -> StatusTable.INVALID_INPUT;
                    case "409" -> StatusTable.ALREADY_EXISTS;
                    case "422" -> StatusTable.UNPROCESSABLE_ENTITY;
                    default -> StatusTable.INTERNAL_SERVER_ERROR;
                };
            }
        } catch (IOException e) {
            return StatusTable.INTERNAL_SERVER_ERROR;
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
                return switch (status) {
                    case "200" -> StatusTable.OK;
                    case "400" -> StatusTable.BAD;
                    case "401" -> StatusTable.UNAUTHORIZED;
                    case "403" -> StatusTable.FORBIDDEN;
                    case "404" -> StatusTable.NOT_FOUND;
                    case "422" -> StatusTable.UNPROCESSABLE_ENTITY;
                    default -> StatusTable.INTERNAL_SERVER_ERROR;
                };
            }
        } catch (IOException e) {
            return StatusTable.INTERNAL_SERVER_ERROR;
        }
        return null;
    }

    public MovieRecord getMovieById(String id) {
        MovieGetByIdRequest msg = new MovieGetByIdRequest(id, SessionManager.getInstance().getToken());
        String json = gson.toJson(msg);
        try {
            client.send(json);
            String response = client.receive();
            if (response != null) {
                JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
                String status = jsonObject.has("status") ? jsonObject.get("status").getAsString() : "";
                if (status.equals("200")) {
                    if (!jsonObject.has("filme")) {
                        return null;
                    }
                    JsonElement movieElement = jsonObject.get("filme");
                    MovieRecord movie = gson.fromJson(movieElement, MovieRecord.class);

                    Type reviewListType = new TypeToken<List<ReviewRecord>>(){}.getType();
                    List<ReviewRecord> reviews = jsonObject.has("reviews")
                            ? gson.fromJson(jsonObject.get("reviews"), reviewListType)
                            : new ArrayList<>();

                    // Validar campo 'editado' das reviews
                    if (reviews != null) {
                        for (ReviewRecord review : reviews) {
                            String editadoStr = review.editado();
                            if (editadoStr == null) {
                                javafx.application.Platform.runLater(() -> {
                                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                                    alert.setTitle("Erro de dado");
                                    alert.setHeaderText("Campo 'editado' ausente");
                                    alert.setContentText("O campo 'editado' é obrigatório e deve ser 'true' ou 'false', mas não foi recebido do servidor.");
                                    alert.show();
                                });
                                break; // Mostra apenas uma vez
                            } else {
                                String s = editadoStr.trim();
                                if (!"true".equalsIgnoreCase(s) && !"false".equalsIgnoreCase(s)) {
                                    final String valorInvalido = editadoStr;
                                    javafx.application.Platform.runLater(() -> {
                                        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                                        alert.setTitle("Erro de dado");
                                        alert.setHeaderText("Campo 'editado' inválido");
                                        alert.setContentText("O valor de 'editado' precisa ser 'true' ou 'false'. Valor recebido: " + valorInvalido);
                                        alert.show();
                                    });
                                    break; // Mostra apenas uma vez
                                }
                            }
                        }
                    }

                    return new MovieRecord(
                            movie.id(),
                            movie.titulo(),
                            movie.diretor(),
                            movie.ano(),
                            movie.genero(),
                            movie.nota(),
                            movie.qtd_avaliacoes(),
                            movie.sinopse(),
                            reviews
                    );
                }
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }
}
