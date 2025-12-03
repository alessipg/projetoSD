package org.alessipg.client.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.shared.dto.request.ReviewCreateRequest;
import org.alessipg.shared.dto.request.ReviewUpdateRequest;
import org.alessipg.shared.dto.response.StatusResponse;
import org.alessipg.shared.dto.util.MovieRecord;
import org.alessipg.shared.dto.util.ReviewRecord;
import org.alessipg.shared.enums.Genre;

import java.io.IOException;

public class ReviewFormViewController {

    @FXML
    private TextField tfTitle;

    @FXML
    private TextArea tfDescription;

    @FXML
    private ToggleGroup nota;

    @FXML
    private RadioButton rbRating1;

    @FXML
    private RadioButton rbRating2;

    @FXML
    private RadioButton rbRating3;

    @FXML
    private RadioButton rbRating4;

    @FXML
    private RadioButton rbRating5;
    @FXML
    private Button btnSubmit;
    @FXML
    private Label lbTitle;
    private Integer currentRating = 5;
    private boolean isEditMode = false;
    private ReviewRecord existingReview;
    @FXML
    public void initialize() {
        // Add listener to track rating changes
        nota.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                RadioButton selected = (RadioButton) newValue;
                Object userData = selected.getUserData();
                if (userData != null) {
                    currentRating = Integer.parseInt(userData.toString());
                }
            }
        });
        tfTitle.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obsWin, oldWin, newWin) -> {
                    if (newWin != null) {
                        Stage stage = (Stage) newWin;
                        ReviewRecord review = (ReviewRecord) stage.getUserData();
                        existingReview = review;
                        if (review != null) {
                            isEditMode = true;
                            btnSubmit.setText("Salvar");
                            lbTitle.setText("Editar Review");
                            tfTitle.setText(review.titulo());
                            tfDescription.setText(review.descricao());
                        }
                    }
                });
            }
        });
    }

    @FXML
    public void onSubmit() throws IOException {

        String title = tfTitle.getText();
        String description = tfDescription.getText();
        if (isEditMode) {
            ReviewUpdateRequest request = new ReviewUpdateRequest(
                    existingReview.id(),
                    title,
                    description,
                    currentRating,
                    SessionManager.getInstance().getToken()
            );
            StatusResponse response = SessionManager.getInstance().getReviewClientService().update(request);
            Alert alert;
            if(response.status().equals("200")){
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Review atualizada");
                alert.setHeaderText(null);
                alert.setContentText("Sua review foi atualizada com sucesso!");
                alert.showAndWait();
                onBack();
            } else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro ao atualizar review");
                alert.setHeaderText(response.status());
                alert.setContentText(response.mensagem());
                alert.showAndWait();
            }
        } else {
            ReviewCreateRequest request = new ReviewCreateRequest(
                    title,
                    description,
                    currentRating,
                    Integer.parseInt(SessionManager.getInstance().getCurrentMovie().id()),
                    SessionManager.getInstance().getToken()
            );
            StatusResponse response = SessionManager.getInstance().getReviewClientService().create(request);
            Alert alert;
            if(response.status().equals("200")){
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Review criada");
                alert.setHeaderText(null);
                alert.setContentText("Sua review foi criada com sucesso!");
                onBack();
            } else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro ao criar review");
                alert.setHeaderText(response.status());
                alert.setContentText(response.mensagem());
            }
            alert.showAndWait();
        }
        if (currentRating != null) {
            System.out.println("Título: " + title);
            System.out.println("Descrição: " + description);
            System.out.println("Nota: " + currentRating);
        }
    }

    @FXML
    public void onBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/alessipg/client/ui/MovieInfoView.fxml"));
        Parent novaTelaRoot = loader.load();
        Stage stage = (Stage) lbTitle.getScene().getWindow();
        // Retrieve current movie from SessionManager to pass back to MovieInfoView
        MovieRecord currentMovie = SessionManager.getInstance().getCurrentMovie();
        stage.setUserData(currentMovie);
        stage.setScene(new Scene(novaTelaRoot));
    }
}
