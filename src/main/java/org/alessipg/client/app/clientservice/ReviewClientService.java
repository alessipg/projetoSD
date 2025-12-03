package org.alessipg.client.app.clientservice;

import com.google.gson.Gson;
import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.client.infra.tcp.TcpClient;
import org.alessipg.shared.dto.request.OwnReviewsRequest;
import org.alessipg.shared.dto.request.ReviewCreateRequest;
import org.alessipg.shared.dto.request.ReviewDeleteRequest;
import org.alessipg.shared.dto.request.ReviewUpdateRequest;
import org.alessipg.shared.dto.response.OwnReviewsResponse;
import org.alessipg.shared.dto.response.StatusResponse;
import org.alessipg.shared.dto.util.ReviewRecord;
import org.alessipg.shared.enums.StatusTable;

public class ReviewClientService {
    private final Gson gson;
    private final TcpClient client;

    public ReviewClientService(Gson gson) {
        this.gson = gson;
        this.client = SessionManager.getClient();
    }

    public StatusResponse create(ReviewCreateRequest review) {
        try {
            String json = gson.toJson(review);
            client.send(json);
            String response = client.receive();
            if (response == null)
                return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
            return gson.fromJson(response, StatusResponse.class);
        } catch (Exception e) {
            return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
        }
    }

    public StatusResponse update(ReviewUpdateRequest request) {
        try {
            String json = gson.toJson(request);
            client.send(json);
            String response = client.receive();
            if (response == null)
                return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
            return gson.fromJson(response, StatusResponse.class);
        } catch (Exception e) {
            return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
        }
    }

    public StatusResponse delete(String id, String token) {
        try {
            ReviewDeleteRequest request = new ReviewDeleteRequest(id, token);
            String json = gson.toJson(request);
            client.send(json);
            String response = client.receive();
            if (response == null)
                return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
            return gson.fromJson(response, StatusResponse.class);
        } catch (Exception e) {
            return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
        }
    }

    public OwnReviewsResponse getOwnReviews(OwnReviewsRequest request) {
        try {
            String json = gson.toJson(request);
            client.send(json);
            String response = client.receive();
            if (response == null)
                return new OwnReviewsResponse(StatusTable.INTERNAL_SERVER_ERROR, null);

            OwnReviewsResponse ownReviewsResponse = gson.fromJson(response, OwnReviewsResponse.class);

            // Validar campo 'editado' das reviews
            if (ownReviewsResponse != null && ownReviewsResponse.reviews() != null) {
                for (ReviewRecord review : ownReviewsResponse.reviews()) {
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

            return ownReviewsResponse;
        } catch (Exception e) {
            return new OwnReviewsResponse(StatusTable.INTERNAL_SERVER_ERROR, null);
        }
    }
}
