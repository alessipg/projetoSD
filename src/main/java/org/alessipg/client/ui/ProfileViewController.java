package org.alessipg.client.ui;

import java.io.IOException;
import java.util.Optional;

import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.records.response.UserSelfGetResponse;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

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
            Alert alert = new Alert(null);
            switch (res) {
                case OK: 
                    alert.setAlertType(AlertType.INFORMATION);
                    alert.setTitle("Sucesso");
                    alert.setContentText("Senha alterada com sucesso!");
                    alert.showAndWait();
                
                case BAD: 
                    alert.setAlertType(AlertType.ERROR);
                    alert.setTitle("Falha");
                    alert.setContentText("Algo deu errado, tente novamente!");
                    alert.showAndWait();
                
                case FORBIDDEN: 
                    alert.setAlertType(AlertType.ERROR);
                    alert.setTitle("Não enccontrado");
                    alert.setContentText("Usuário não encontrado. Faça um novo login e tente novamente!");
                    alert.showAndWait();
                
                default:
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    private void onDelete() {
        try {
            Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
            confirmAlert.setHeaderText("Confirmação");
            confirmAlert.setHeaderText("Deseja excluir sua conta? Esta ação não pode ser desfeita.");
            Optional<ButtonType> buttonType = confirmAlert.showAndWait();
            if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
                StatusTable res = SessionManager.getInstance().getUserClientService().delete();
                Alert alert = new Alert(null);
                switch (res) {
                    case OK: 
                        alert.setAlertType(AlertType.INFORMATION);
                        alert.setTitle("Sucesso");
                        alert.setContentText("Conta excluída com sucesso!");
                        alert.showAndWait();
                        btnDelete.getScene().getWindow().hide();
                        break;
                    
                    case BAD: 
                        alert.setAlertType(AlertType.ERROR);
                        alert.setTitle("Falha");
                        alert.setContentText("Algo deu errado, tente novamente!");
                        alert.showAndWait();
                        break;
                    
                    case FORBIDDEN: 
                        alert.setAlertType(AlertType.ERROR);
                        alert.setTitle("Não enccontrado");
                        alert.setContentText("Usuário não encontrado. Faça um novo login e tente novamente!");
                        alert.showAndWait();
                        break;
                    
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            // TODO: same
        }
    }
}
