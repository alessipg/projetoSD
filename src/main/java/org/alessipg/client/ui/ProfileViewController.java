package org.alessipg.client.ui;

import java.io.IOException;
import java.util.Optional;

import org.alessipg.client.infra.session.SessionManager;
// removed unused StatusTable import
import org.alessipg.shared.util.Result;
import org.alessipg.shared.records.response.UserSelfGetResponse;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private Button btnBack;

    @FXML
    public void initialize() {
        try {
            Result<UserSelfGetResponse> res = SessionManager.getInstance().getUserClientService().selfGet();
            if (res instanceof Result.Success<UserSelfGetResponse> s) {
                UserSelfGetResponse user = s.data();
                String name = user.usuario();
                lblUser.setText(name != null && !name.isEmpty() ? "Olá, " + name + "!" : "Olá!");
            } else if (res instanceof Result.Failure<UserSelfGetResponse> f) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Algo deu errado");
                alert.setContentText(f.message());
                alert.showAndWait();
            }
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro de conexão");
            alert.setContentText("Não foi possível buscar informações do usuário: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onChangePassword() {

        try {
            Result<Void> res = SessionManager.getInstance().getUserClientService().update(pfPassword.getText());
            if (res instanceof Result.Success<Void>) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setContentText("Senha alterada com sucesso!");
                alert.showAndWait();
            } else if (res instanceof Result.Failure<Void> f) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Falha");
                alert.setContentText(f.message());
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
                Result<Void> res = SessionManager.getInstance().getUserClientService().delete();
                if (res instanceof Result.Success<Void>) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Sucesso");
                    alert.setContentText("Conta excluída com sucesso!");
                    alert.showAndWait();
                    btnDelete.getScene().getWindow().hide();
                } else if (res instanceof Result.Failure<Void> f) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Falha");
                    alert.setContentText(f.message());
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
