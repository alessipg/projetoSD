package org.alessipg.client.ui;

import java.io.IOException;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.shared.dto.request.OwnReviewsRequest;
import org.alessipg.shared.dto.response.OwnReviewsResponse;
import org.alessipg.shared.dto.response.StatusResponse;
import org.alessipg.shared.dto.response.UserSelfGetResponse;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import org.alessipg.shared.dto.util.ReviewRecord;

public class ProfileViewController {

    @FXML
    private Label lblUser;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Button btnPassword;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnBack;
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
        listReviews.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ReviewRecord>() {
            @Override
            public void changed(ObservableValue<? extends ReviewRecord> observable, ReviewRecord oldValue,
                                ReviewRecord newValue) {
                System.out.println("ListView selection changed from oldValue = "
                        + oldValue + " to newValue = " + newValue);
                selectedReview = newValue;
            }
        });
        try {
            UserSelfGetResponse res = SessionManager.getInstance().getUserClientService().selfGet();
            if (res.status().equals("200")) {
                String name = res.usuario();
                lblUser.setText(name != null && !name.isEmpty() ? "Olá, " + name + "!" : "Olá!");
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Algo deu errado");
                alert.setContentText(res.mensagem());
                alert.showAndWait();
            }
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro de conexão");
            alert.setContentText("Não foi possível buscar informações do usuário: " + e.getMessage());
            alert.showAndWait();
        }
        try{
            OwnReviewsRequest request = new OwnReviewsRequest(SessionManager.getInstance().getToken());
            OwnReviewsResponse response = SessionManager.getInstance().getReviewClientService().getOwnReviews(request);
        }
    }

    @FXML
    private void onChangePassword() {

        try {
            StatusResponse res = SessionManager.getInstance().getUserClientService().update(pfPassword.getText());
            if (res.status().equals("200")) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setContentText(res.status());
                alert.showAndWait();
                pfPassword.clear();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Falha");
                alert.setContentText(res.mensagem());
                alert.showAndWait();
            }
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro de conexão");
            alert.setContentText("Não foi possível alterar a senha: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/alessipg/client/ui/MoviesView.fxml"));
        Parent novaTelaRoot = loader.load();
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.setScene(new Scene(novaTelaRoot));
    }

    @FXML
    private void onDelete() {
        try {
            Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
            confirmAlert.setHeaderText("Confirmação");
            confirmAlert.setHeaderText("Deseja excluir sua conta? Esta ação não pode ser desfeita.");
            Optional<ButtonType> buttonType = confirmAlert.showAndWait();
            if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
                StatusResponse res = SessionManager.getInstance().getUserClientService().delete();
                if (res.status().equals("200")) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Sucesso");
                    alert.setContentText(res.mensagem());
                    alert.showAndWait();
                    btnDelete.getScene().getWindow().hide();
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Falha");
                    alert.setContentText(res.mensagem());
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro de conexão");
            alert.setContentText("Não foi possível excluir a conta: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
