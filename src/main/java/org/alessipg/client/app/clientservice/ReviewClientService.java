package org.alessipg.client.app.clientservice;

import com.google.gson.Gson;
import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.client.infra.tcp.TcpClient;
import org.alessipg.server.app.model.Review;
import org.alessipg.shared.dto.request.OwnReviewsRequest;
import org.alessipg.shared.dto.request.ReviewCreateRequest;
import org.alessipg.shared.dto.request.ReviewDeleteRequest;
import org.alessipg.shared.dto.request.ReviewUpdateRequest;
import org.alessipg.shared.dto.response.StatusResponse;
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
        return null;
    }

    public OwnReviewsResponse getOwnReviews(OwnReviewsRequest request) {
    }
}
