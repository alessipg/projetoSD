package org.alessipg.client.ui;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

import javafx.beans.value.ChangeListener;

import lombok.Getter;
import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.dto.response.MovieGetAllResponse;
import org.alessipg.shared.dto.util.MovieRecord;

public class AdminViewController {

    @FXML
    private ListView<MovieRecord> listMovies;
    @Getter
    private MovieRecord selectedMovie;
    @FXML
    private Button btnEdit;
    @FXML
    public void initialize() {
        // Custom cell factory
        listMovies.setCellFactory(new Callback<ListView<MovieRecord>, ListCell<MovieRecord>>() {
            @Override
            public ListCell<MovieRecord> call(ListView<MovieRecord> param) {
                return new MovieListCell();
            }
        });

        loadMovies();

        listMovies.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MovieRecord>() {
            @Override
            public void changed(ObservableValue<? extends MovieRecord> observable, MovieRecord oldValue,
                    MovieRecord newValue) {
                System.out.println("ListView selection changed from oldValue = "
                        + oldValue + " to newValue = " + newValue);
            }
        });
    }

    @FXML
    private void onNew() throws IOException {
        listMovies.getSelectionModel().clearSelection();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/alessipg/client/ui/MovieForm.fxml"));
        Parent novaTelaRoot = loader.load();
        Stage stage = (Stage) listMovies.getScene().getWindow();
        stage.setScene(new Scene(novaTelaRoot));
    }

    @FXML
    private void onEdit() throws IOException {
        System.out.println("Chegou");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/alessipg/client/ui/MovieForm.fxml"));
        Parent novaTelaRoot = loader.load();
        Stage stage = (Stage) listMovies.getScene().getWindow();
        stage.setUserData(listMovies.getSelectionModel().getSelectedItem());
        System.out.println("Selected movie: " + listMovies.getSelectionModel().getSelectedItem());
        stage.setScene(new Scene(novaTelaRoot));
    }
    @FXML
    private void onDelete() {
        MovieRecord selectedMovie = listMovies.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Deleção");
            alert.setHeaderText("Você tem certeza que deseja excluir o filme: " + selectedMovie.titulo() + "?");
            alert.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    try {
                        StatusTable res = SessionManager.getInstance().getMovieClientService().delete(selectedMovie.id());
                        Alert a = new Alert(null);
                        switch(res){
                            case OK:
                                a.setAlertType(Alert.AlertType.INFORMATION);
                                a.setContentText("Filme excluído com sucesso!");
                                a.showAndWait();
                                break;
                            case UNAUTHORIZED:
                                a.setAlertType(Alert.AlertType.ERROR);
                                a.setContentText("Erro: Usuário não autorizado.");
                                a.showAndWait();
                                break;
                            case NOT_FOUND:
                                a.setAlertType(Alert.AlertType.ERROR);
                                a.setContentText("Erro: Filme não encontrado.");
                                a.showAndWait();
                                break;
                            case UNPROCESSABLE_ENTITY:
                                a.setAlertType(Alert.AlertType.ERROR);
                                a.setContentText("Erro: Entidade não processável.");
                                a.showAndWait();
                                break;
                            case INTERNAL_SERVER_ERROR:
                                a.setAlertType(Alert.AlertType.ERROR);
                                a.setContentText("Erro: Erro interno do servidor.");
                                a.showAndWait();
                                break;
                            default:
                                a.setAlertType(Alert.AlertType.ERROR);
                                a.setContentText("Erro: Erro desconhecido.");
                                a.showAndWait();
                                break;
                        }
                        loadMovies();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    private void loadMovies() {
        listMovies.getItems().clear();
        try {
            MovieGetAllResponse movies = SessionManager.getInstance().getMovieClientService().getAll();
            listMovies.getItems().addAll(movies.filmes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    private void onBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/alessipg/client/ui/MoviesView.fxml"));
        Parent novaTelaRoot = loader.load();
        Stage stage = (Stage) listMovies.getScene().getWindow();
        stage.setScene(new Scene(novaTelaRoot));
    }

}
