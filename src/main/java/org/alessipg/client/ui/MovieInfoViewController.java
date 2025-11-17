package org.alessipg.client.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.shared.dto.util.MovieRecord;

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
    private ListView<?> listReviews;

    @FXML
    public void initialize() {
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
    }

    private void loadMovieData(MovieRecord movie) {
        MovieRecord selectedMovie = SessionManager.getInstance().getMovieClientService().getMovieById(movie.id());
        lbTitle.setText(movie.titulo());
        lbDirector.setText("Diretor: " + movie.diretor());
        lbScore.setText("Avaliação: " + movie.nota());
        lbRatingCount.setText(movie.qtd_avaliacoes() + " avaliações");
        lbGenres.setText("Gêneros: " + String.join(", ", movie.genero()));
        lbYear.setText("Ano: " + movie.ano());
        lbSynopsis.setText(movie.sinopse());
    }
    @FXML
    private void onNew(){

    }
    @FXML
    private void onEdit(){

    }
    @FXML
    private void onDelete(){

    }
    @FXML
    private void onBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/alessipg/client/ui/MoviesView.fxml"));
        Parent novaTelaRoot = loader.load();
        Stage stage = (Stage) lbSynopsis.getScene().getWindow();
        stage.setScene(new Scene(novaTelaRoot));
    }
}

