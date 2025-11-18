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
        Movie movie = movieService.getEntityById(Integer.parseInt(newReview.id_filme()));
        User user = userService.getById(id);
        if(user == null)
            return new StatusResponse(StatusTable.NOT_FOUND);
        if(movie == null)
            return new StatusResponse(StatusTable.NOT_FOUND);
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
        movie.updateRating(review.getRating());
        movieService.updateEntity(movie);
        reviewRepository.save(review);
        return new StatusResponse(StatusTable.OK);
    }

}
