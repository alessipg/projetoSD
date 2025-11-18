package org.alessipg.client.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.shared.dto.response.MovieGetByIdResponse;
import org.alessipg.shared.dto.util.MovieRecord;
import org.alessipg.shared.dto.util.ReviewRecord;

import java.io.IOException;

public class MovieInfoViewController {
    @FXML
    private Label lbTitle;

    @FXML
    private Label lbDirector;

    @FXML
    private Label lbScore;

    @FXML
    private Label lbRatingCount;

    @FXML
    private Label lbGenres;

    @FXML
    private Label lbYear;

    @FXML
    private TextArea lbSynopsis;

    @FXML
    private ListView<ReviewRecord> listReviews;

    private ReviewRecord selectedReview;

    @FXML
    public void initialize() {
        listReviews.setCellFactory(new Callback<ListView<ReviewRecord>, ListCell<ReviewRecord>>() {
            @Override
            public ListCell<ReviewRecord> call(ListView<ReviewRecord> param) {
                return new ReviewListCell();
            }
        });
        // Aguarda o Stage estar disponível para obter o userData
        lbTitle.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obs, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        Stage stage = (Stage) newWindow;
                        MovieRecord movie = (MovieRecord) stage.getUserData();
                        if (movie != null) {
                            loadMovieData(movie);
                        }
                    }
                });
            }
        });
        listReviews.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ReviewRecord>() {
            @Override
            public void changed(ObservableValue<? extends ReviewRecord> observable, ReviewRecord oldValue,
                                ReviewRecord newValue) {
                System.out.println("ListView selection changed from oldValue = "
                        + oldValue + " to newValue = " + newValue);
                selectedReview = newValue;
            }
        });
    }

    private void loadMovieData(MovieRecord selectedMovie) {
        MovieRecord movie= SessionManager.getInstance().getMovieClientService().getMovieById(selectedMovie.id());
        SessionManager.getInstance().setCurrentMovie(movie);
        lbTitle.setText(movie.titulo());
        lbDirector.setText("Diretor: " + movie.diretor());
        lbScore.setText("Avaliação: " + movie.nota());
        lbRatingCount.setText(movie.qtd_avaliacoes() + " avaliações");
        lbGenres.setText("Gêneros: " + String.join(", ", movie.genero()));
        lbYear.setText("Ano: " + movie.ano());
        lbSynopsis.setText(movie.sinopse());
        if (movie.reviews() != null && !movie.reviews().isEmpty()) {
            listReviews.getItems().addAll(movie.reviews());
        }
    }

    @FXML
    private void onNew() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/alessipg/client/ui/ReviewFormView.fxml"));
        Parent novaTelaRoot = loader.load();
        Stage stage = (Stage) lbSynopsis.getScene().getWindow();
        stage.setUserData(null);
        stage.setScene(new Scene(novaTelaRoot));
    }

    @FXML
    private void onEdit() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/alessipg/client/ui/ReviewFormView.fxml"));
        Parent novaTelaRoot = loader.load();
        Stage stage = (Stage) lbSynopsis.getScene().getWindow();
        stage.setUserData(selectedReview);
        stage.setScene(new Scene(novaTelaRoot));
    }

    @FXML
    private void onDelete() {

    }

    @FXML
    private void onBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/alessipg/client/ui/MoviesView.fxml"));
        Parent novaTelaRoot = loader.load();
        Stage stage = (Stage) lbSynopsis.getScene().getWindow();
        // Clear current movie from session when going back to movies list
        SessionManager.getInstance().setCurrentMovie(null);
        stage.setScene(new Scene(novaTelaRoot));
    }
}

