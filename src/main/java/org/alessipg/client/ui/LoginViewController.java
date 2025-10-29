package org.alessipg.client.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;

import java.io.IOException;

import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.shared.dto.response.StatusResponse;

import javafx.event.ActionEvent;
import org.alessipg.shared.dto.response.UserLoginResponse;

public class LoginViewController {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtSenha;

    @FXML
    private void onLogin(ActionEvent event) {
        String usuario = txtUsuario.getText();
        String senha = txtSenha.getText();
        try {
            UserLoginResponse res = SessionManager.getInstance()
                    .getAuthClientService().login(usuario, senha);
            if(res.status().equals("200")) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setContentText("Login realizado com sucesso!");
                alert.showAndWait();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/alessipg/client/ui/MoviesView.fxml"));
                Parent novaTelaRoot = loader.load();
                Stage stage = (Stage) txtUsuario.getScene().getWindow();
                Scene novaTelaScene = new Scene(novaTelaRoot);
                stage.setScene(novaTelaScene);
                stage.setTitle("Aplicação Cliente - Main");
                stage.show();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Algo deu errado");
                alert.setContentText(res.mensagem());
                alert.showAndWait();
            }
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setResizable(true);
            alert.setTitle("Erro de conexão");
            alert.setContentText("Erro ao conectar com o servidor: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onCreate(ActionEvent event) {
        String usuario = txtUsuario.getText();
        String senha = txtSenha.getText();
        try {
            StatusResponse res = SessionManager.getInstance()
                    .getUserClientService().create(usuario, senha);
            Alert alert;
            if (res.status().equals("200")) {
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Sucesso");
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Algo deu errado");
            }
            alert.setContentText(res.mensagem());
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setResizable(true);
            alert.setTitle("Erro de conexão");
            alert.setContentText("Erro ao conectar com o servidor: " + e.getMessage());
            alert.showAndWait();
        }
    }
}