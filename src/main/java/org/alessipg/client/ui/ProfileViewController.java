package org.alessipg.client.ui;

import java.io.IOException;

import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.records.request.UserUpdateRequest;
import org.alessipg.shared.records.response.UserSelfGetResponse;
import org.alessipg.shared.records.util.UserRecord;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

public class ProfileViewController {

    @FXML
    private Label lblUser;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Button btnPassword;

    @FXML
    public void initialize() {
        try {
            UserSelfGetResponse user = SessionManager.getInstance().getUserClientService().selfGet();
            if (user.usuario() != null && !user.usuario().isEmpty()) {
                lblUser.setText("Olá, " + user.usuario() + "!");
            } else {
                lblUser.setText("Olá!");
            }
        } catch (IOException e) {
            // TODO: improve error feedback
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setContentText("Não foi possível buscar informações do usuário");
            alert.showAndWait();
            e.printStackTrace();
        }

    }

    @FXML
    private void onChangePassword() {

        try {
            StatusTable res = SessionManager.getInstance().getUserClientService().update(pfPassword.getText());
            switch (res) {
                case OK: {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Sucesso");
                    alert.setContentText("Senha alterada com sucesso!");
                    alert.showAndWait();
                }
                case BAD: {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Falha");
                    alert.setContentText("Algo deu errado, tente novamente!");
                    alert.showAndWait();
                }
                case FORBIDDEN: {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Não enccontrado");
                    alert.setContentText("Usuário não encontrado. Faça um novo login e tente novamente!");
                    alert.showAndWait();
                }
                default:
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
