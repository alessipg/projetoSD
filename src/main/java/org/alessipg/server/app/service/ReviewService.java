package org.alessipg.server.app.service;

import org.alessipg.server.app.model.Movie;
import org.alessipg.server.app.model.Review;
import org.alessipg.server.app.model.User;
import org.alessipg.server.infra.repo.ReviewRepository;
import org.alessipg.shared.dto.response.MovieGetByIdResponse;
import org.alessipg.shared.dto.response.StatusResponse;
import org.alessipg.shared.dto.response.UserSelfGetResponse;
import org.alessipg.shared.dto.util.MovieRecord;
import org.alessipg.shared.dto.util.ReviewRecord;
import org.alessipg.shared.enums.StatusTable;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;

public class ReviewService {
    private final MovieService movieService;
    private final UserService userService;
    private final ReviewRepository reviewRepository;

    public ReviewService(MovieService movieService, UserService userService, ReviewRepository reviewRepository) {
        this.movieService = movieService;
        this.userService = userService;
        this.reviewRepository = reviewRepository;
    }

    public StatusResponse create(ReviewRecord newReview, int id) {
        try {
            Movie movie = movieService.getEntityById(Integer.parseInt(newReview.id_filme()));
            User user = userService.getById(id);
            if (user == null)
                return new StatusResponse(StatusTable.NOT_FOUND);
            if (movie == null)
                return new StatusResponse(StatusTable.NOT_FOUND);
            if (isInvalidReview(newReview,true))
                return new StatusResponse(StatusTable.INVALID_INPUT);
            if(user.isAdmin())
                return new StatusResponse(StatusTable.UNAUTHORIZED);
            Review review = new Review(
                    0,
                    movie,
                    user,
                    Integer.parseInt(newReview.nota()),
                    newReview.titulo(),
                    newReview.descricao(),
                    Date.from(Instant.now()),
                    false
            );
            movie.updateRating(review.getRating(), false);
            movieService.updateEntity(movie);
            reviewRepository.save(review);
            return new StatusResponse(StatusTable.OK);
        } catch (Exception e) {
            return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
        }
    }
    public StatusResponse update(ReviewRecord review, int id){
        try {
            Optional<Review> rev = reviewRepository.findByIdAndUserId(Integer.parseInt(review.id()), id);
            if (rev.isEmpty())
                return new StatusResponse(StatusTable.NOT_FOUND);
            if (isInvalidReview(review, false))
                return new StatusResponse(StatusTable.INVALID_INPUT);
            Movie movie = rev.get().getMovie();
            Review existingReview = rev.get();
            User user = existingReview.getUser();
            if(user.getId() != id)
                return new StatusResponse(StatusTable.UNAUTHORIZED);
            movie.updateRating(existingReview.getRating(), true);
            existingReview.setTitle(review.titulo());
            existingReview.setDescription(review.descricao());
            int newRating = Integer.parseInt(review.nota());
            existingReview.setRating(newRating);
            movie.updateRating(newRating, false);
            movieService.updateEntity(movie);
            reviewRepository.save(existingReview);
            return new StatusResponse(StatusTable.OK);
        } catch (Exception e) {
            return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
        }
    }
    private boolean isInvalidReview(ReviewRecord newReview, boolean isCreating) {


        if ((newReview.id_filme() == null || newReview.id_filme().isEmpty()) && isCreating)
            return true;
        if (newReview.nota() == null || newReview.nota().isEmpty())
            return true;
        int rating;
        try {
            rating = Integer.parseInt(newReview.nota());
        } catch (NumberFormatException e) {
            return true;
        }
        if (rating < 1 || rating > 5)
            return true;
        if (newReview.titulo() == null || newReview.titulo().isEmpty())
            return true;
        return newReview.descricao() == null || newReview.descricao().isEmpty();
    }


    public StatusResponse delete(String reviewId, int id) {
        try {
            Optional<Review> rev = reviewRepository.findByIdAndUserId(Integer.parseInt(reviewId), id);
            if (rev.isEmpty())
                return new StatusResponse(StatusTable.NOT_FOUND);
            Review existingReview = rev.get();
            Movie movie = existingReview.getMovie();
            User user = existingReview.getUser();
            if(!user.isAdmin() || user.getId() != id)
                return new StatusResponse(StatusTable.UNAUTHORIZED);
            movie.updateRating(existingReview.getRating(), true);
            movieService.updateEntity(movie);
            reviewRepository.delete(existingReview);
            return new StatusResponse(StatusTable.OK);
        } catch (Exception e) {
            return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
        }
    }
}
