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
import org.alessipg.shared.domain.model.Movie;

public class FilmesViewController {

    @FXML
    private ListView<Movie> listFilmes;

    @FXML
    public void initialize() {
        // Custom cell factory
        listFilmes.setCellFactory(new Callback<ListView<Movie>, ListCell<Movie>>() {
            @Override
            public ListCell<Movie> call(ListView<Movie> param) {
                return new FilmeListCell();
            }
        });

        // Exemplo de dados mockados
        listFilmes.getItems().addAll();
    }

    @FXML
    private void onMyAccount() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/alessipg/client/ui/ProfileView.fxml"));
        Parent novaTelaRoot = loader.load();
        Stage stage = (Stage) listFilmes.getScene().getWindow();
        stage.setScene(new Scene(novaTelaRoot));
    }
    
    @FXML
    private void onAdmin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/alessipg/client/ui/AdminView.fxml"));
        Parent novaTelaRoot = loader.load();
        Stage stage = (Stage) listFilmes.getScene().getWindow();
        stage.setScene(new Scene(novaTelaRoot));
    }

    @FXML
    private void onLogout() throws IOException {
        SessionManager.getInstance().getAuthClientService().logout();
        listFilmes.getScene().getWindow().hide();
    }
}       
