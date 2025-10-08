package org.alessipg.client.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.shared.records.response.MovieGetAllResponse;
import org.alessipg.shared.records.util.MovieRecord;

public class AdminViewController {

    @FXML
    private ListView<MovieRecord> listMovies;

    @FXML
    public void initialize() {
        // Custom cell factory
        listMovies.setCellFactory(new Callback<ListView<MovieRecord>, ListCell<MovieRecord>>() {
            @Override
            public ListCell<MovieRecord> call(ListView<MovieRecord> param) {
                return new FilmeListCell();
            }
        });

        try {
            MovieGetAllResponse movies = SessionManager.getInstance().getMovieClientService().getAll();
            listMovies.getItems().addAll(movies.filmes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
        @FXML
    private void onNew() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/alessipg/client/ui/MovieNewView.fxml"));
        Parent novaTelaRoot = loader.load();
        Stage stage = (Stage) listMovies.getScene().getWindow();
        stage.setScene(new Scene(novaTelaRoot));
    }
            @FXML
    private void onBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/alessipg/client/ui/MoviesView.fxml"));
        Parent novaTelaRoot = loader.load();
        Stage stage = (Stage) listMovies.getScene().getWindow();
        stage.setScene(new Scene(novaTelaRoot));
    }
}
