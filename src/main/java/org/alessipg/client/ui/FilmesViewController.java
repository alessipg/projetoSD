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
import java.util.Arrays;

import org.alessipg.shared.domain.model.Filme;

public class FilmesViewController {

    @FXML
    private ListView<Filme> listFilmes;

    @FXML
    public void initialize() {
        // Custom cell factory
        listFilmes.setCellFactory(new Callback<ListView<Filme>, ListCell<Filme>>() {
            @Override
            public ListCell<Filme> call(ListView<Filme> param) {
                return new FilmeListCell();
            }
        });

        // Exemplo de dados mockados
        listFilmes.getItems().addAll(
                new Filme("O Poderoso Chefão", "Francis Ford Coppola", 1972,
                        Arrays.asList("Crime", "Drama"), "A saga da família Corleone...", 2000,
                        4.8f),
                new Filme("Interestelar", "Christopher Nolan", 2014,
                        Arrays.asList("Ficção Científica", "Aventura"),
                        "Um grupo de exploradores viaja através de um buraco de minhoca...", 5000,
                        4.5f));
    }

    @FXML
    private void onAdmin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/alessipg/client/ui/AdminView.fxml"));
        Parent novaTelaRoot = loader.load();
        Stage stage = (Stage) listFilmes.getScene().getWindow();
        stage.setScene(new Scene(novaTelaRoot));
    }
}
