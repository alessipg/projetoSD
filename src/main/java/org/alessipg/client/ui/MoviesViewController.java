package org.alessipg.client.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.dto.response.MovieGetAllResponse;
import org.alessipg.shared.dto.util.MovieRecord;

public class MoviesViewController {
    // TODO: Change all class names to english
    @FXML
    private ListView<MovieRecord> listMovies;

    @FXML
    public void initialize() {
        // Custom cell factory
        listMovies.setCellFactory(new Callback<ListView<MovieRecord>, ListCell<MovieRecord>>() {
            @Override
            public ListCell<MovieRecord> call(ListView<MovieRecord> param) {
                return new MovieListCell();
            }
        });

        try {
            MovieGetAllResponse movies = SessionManager.getInstance().getMovieClientService().getAll();
            switch (movies.status()) {
                case "200":
                    listMovies.getItems().addAll(movies.filmes());
                    break;
                default:
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao carregar filmes: " + movies.status());
                    alert.showAndWait();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao carregar filmes: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onMyAccount() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/alessipg/client/ui/ProfileView.fxml"));
        Parent novaTelaRoot = loader.load();
        Stage stage = (Stage) listMovies.getScene().getWindow();
        stage.setScene(new Scene(novaTelaRoot));
    }

    @FXML
    private void onAdmin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/alessipg/client/ui/AdminView.fxml"));
        Parent novaTelaRoot = loader.load();
        Stage stage = (Stage) listMovies.getScene().getWindow();
        stage.setScene(new Scene(novaTelaRoot));
    }

    @FXML
    private void onLogout() throws IOException {
        SessionManager.getInstance().getAuthClientService().logout();
        listMovies.getScene().getWindow().hide();
    }
}
