package org.alessipg.client.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.shared.dto.response.StatusResponse;
import org.alessipg.shared.dto.util.UserView;

import java.io.IOException;

public class UserEditViewController {

    @FXML
    private Label lbUser;
    @FXML
    private PasswordField pfNewPassword;

    @FXML
    public void initialize() throws IOException {
        lbUser.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obsWin, oldWin, newWin) -> {
                    if (newWin != null) {
                        Stage stage = (Stage) newWin;
                        UserView user = (UserView) stage.getUserData();
                        if (user == null)
                            lbUser.setText("");
                        else
                            lbUser.setText("ID: " + user.id() + " | Nome: " + user.nome());
                    }
                });
            }
        });
    }

    @FXML
    public void onCancel() throws IOException {
        Stage stage = (Stage) lbUser.getScene().getWindow();
        stage.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/alessipg/client/ui/AdminView.fxml"));
        Parent root = loader.load();
        Stage adminStage = new Stage();
        adminStage.setTitle("Administração");
        adminStage.setScene(new Scene(root));
        adminStage.show();
    }

    @FXML
    public void onSubmit() throws IOException {
        StatusResponse res = SessionManager.getInstance()
                .getUserClientService()
                .adminUpdateUser(pfNewPassword.getText(),
                        ((UserView) lbUser.getScene().getWindow().getUserData()).id());
        switch (res.status()) {
            case "200" -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setContentText("Usuário atualizado com sucesso!");
                alert.showAndWait();

                Stage stage = (Stage) lbUser.getScene().getWindow();
                stage.close();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/alessipg/client/ui/AdminView.fxml"));
                Parent root = loader.load();
                Stage adminStage = new Stage();
                adminStage.setTitle("Administração");
                adminStage.setScene(new Scene(root));
                adminStage.show();
            }
            default -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Algo deu errado");
                alert.setContentText(res.mensagem());
                alert.showAndWait();
            }
        }
    }
}
