package org.alessipg.server.app.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.alessipg.server.app.service.ReviewService;
import org.alessipg.server.util.JwtUtil;
import org.alessipg.shared.dto.response.StatusResponse;
import org.alessipg.shared.dto.util.ReviewRecord;
import org.alessipg.shared.enums.StatusTable;

public class ReviewController {
    private final ReviewService reviewService;
    private final Gson gson;
    public ReviewController(ReviewService reviewService, Gson gson) {
        this.reviewService = reviewService;
        this.gson = gson;
    }
    public String create(JsonObject json) {
        String token = json.has("token") ? json.get("token").getAsString() : null;
        if (token == null)
            return gson.toJson(new StatusResponse(StatusTable.UNPROCESSABLE_ENTITY));
        int id = JwtUtil.validarToken(token).getClaim("id").asInt();
        JsonObject jsonReview = json.has("review") ? json.get("review").getAsJsonObject() : null;
        ReviewRecord review = gson.fromJson(jsonReview, ReviewRecord.class);
        if(review == null)
            return gson.toJson(new StatusResponse(StatusTable.UNPROCESSABLE_ENTITY));
        StatusResponse response = reviewService.create(review, id);
        return gson.toJson(response);
    }

    public String update(JsonObject json) {
        String token = json.has("token") ? json.get("token").getAsString() : null;
        if (token == null)
            return gson.toJson(new StatusResponse(StatusTable.UNPROCESSABLE_ENTITY));
        int id = JwtUtil.validarToken(token).getClaim("id").asInt();
        JsonObject jsonReview = json.has("review") ? json.get("review").getAsJsonObject() : null;
        ReviewRecord review = gson.fromJson(jsonReview, ReviewRecord.class);
        if(review == null)
            return gson.toJson(new StatusResponse(StatusTable.UNPROCESSABLE_ENTITY));
        StatusResponse response = reviewService.update(review, id);
        return gson.toJson(response);
    }

    public String delete(JsonObject json) {
        String token = json.has("token") ? json.get("token").getAsString() : null;
        if (token == null)
            return gson.toJson(new StatusResponse(StatusTable.UNPROCESSABLE_ENTITY));
        int id = JwtUtil.validarToken(token).getClaim("id").asInt();
        String reviewId = json.has("id") ? json.get("id").getAsString() : null;
        if(reviewId == null)
            return gson.toJson(new StatusResponse(StatusTable.UNPROCESSABLE_ENTITY));
        StatusResponse response = reviewService.delete(reviewId, id);
        return gson.toJson(response);
    }
}
