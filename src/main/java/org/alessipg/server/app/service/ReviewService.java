package org.alessipg.server.app.service;

import org.alessipg.server.app.model.Movie;
import org.alessipg.server.app.model.Review;
import org.alessipg.server.app.model.User;
import org.alessipg.server.infra.repo.ReviewRepository;
import org.alessipg.shared.dto.response.OwnReviewsResponse;
import org.alessipg.shared.dto.response.StatusResponse;
import org.alessipg.shared.dto.util.ReviewRecord;
import org.alessipg.shared.enums.StatusTable;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
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
            if(reviewRepository.existsByUserIdAndMovieId(id, movie.getId()))
                return new StatusResponse(StatusTable.ALREADY_EXISTS);
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

            // Atualizar rating do filme e salvar
            movie.updateRating(review.getRating(), false);
            movieService.updateEntity(movie);

            // Salvar a review - JPA gerenciará o relacionamento automaticamente
            reviewRepository.save(review);
            return new StatusResponse(StatusTable.OK);
        } catch (Exception e) {
            e.printStackTrace();
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
            existingReview.setEdited(true);
            existingReview.setRating(newRating);
            movie.updateRating(newRating, false);
            movieService.updateEntity(movie);
            reviewRepository.save(existingReview);
            return new StatusResponse(StatusTable.OK);
        } catch (Exception e) {
            e.printStackTrace();
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
        if (newReview.descricao() == null || newReview.descricao().isEmpty())
            return true;
        return newReview.descricao().length() > 250;
    }


    public StatusResponse delete(String reviewId, int id) {
        try {
            System.out.println("Solicitação para deletar review id: " + reviewId + " pelo usuário id: " + id);
            Optional<Review> rev = reviewRepository.findById(Integer.parseInt(reviewId));
            if (rev.isEmpty())
                return new StatusResponse(StatusTable.NOT_FOUND);
            System.out.println("Chegou");
            Review existingReview = rev.get();
            User user = existingReview.getUser();
            User userFromRequest = userService.getById(id);
            System.out.println("Chegou 2");
            if (userFromRequest == null){
                System.out.println("Chegou 3");
                return new StatusResponse(StatusTable.NOT_FOUND);
            }
            if(!userFromRequest.isAdmin() && user.getId() != id){
                System.out.println("Chegou 4");
                return new StatusResponse(StatusTable.UNAUTHORIZED);
            }
            System.out.println("Passou tudo");

            // Guardar o rating e o movie ID antes de deletar
            int reviewRating = existingReview.getRating();
            int movieId = existingReview.getMovie().getId();

            // Deletar a review primeiro
            reviewRepository.delete(existingReview);

            // Buscar o movie novamente com uma instância gerenciada e atualizar o rating
            Movie movie = movieService.getEntityById(movieId);
            if (movie != null) {
                movie.updateRating(reviewRating, true);
                movieService.updateEntity(movie);
            }

            System.out.println("Review deletada com sucesso: " + reviewId);
            return new StatusResponse(StatusTable.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
        }
    }

    public OwnReviewsResponse getByUser(int id) {
        try {
            User user = userService.getById(id);
            if (user == null)
                return new OwnReviewsResponse(StatusTable.NOT_FOUND, null);
            List<Review> reviews = reviewRepository.getByUserId(id);
            List<ReviewRecord> reviewRecords = reviews.stream().map(review -> new ReviewRecord(
                    String.valueOf(review.getId()),
                    String.valueOf(review.getMovie().getId()),
                    user.getName(),
                    review.getTitle(),
                    review.getDescription(),
                    review.getFormatedDate(),
                    String.valueOf(review.getRating()),
                    String.valueOf(review.isEdited())
            )).toList();
            return new OwnReviewsResponse(StatusTable.OK, reviewRecords);
        } catch (Exception e) {
            e.printStackTrace();
            return new OwnReviewsResponse(StatusTable.INTERNAL_SERVER_ERROR, null);
        }
    }
}
