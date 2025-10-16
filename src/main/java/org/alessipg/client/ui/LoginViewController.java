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
// removed unused StatusTable import
import org.alessipg.shared.util.Result;

import javafx.event.ActionEvent;

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
            Result<Void> res = SessionManager.getInstance()
                    .getAuthClientService().login(usuario, senha);

            if (res instanceof Result.Success<Void>) {
                try {
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/org/alessipg/client/ui/MoviesView.fxml"));
                    Parent novaTelaRoot = loader.load();
                    Stage stage = (Stage) txtUsuario.getScene().getWindow();
                    stage.setTitle("Filmes");
                    Scene scene = new Scene(novaTelaRoot);
                    stage.setScene(scene);
                    stage.sizeToScene();
                    javafx.application.Platform.runLater(stage::centerOnScreen);
                } catch (IOException e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setResizable(true);
                    alert.setTitle("Falha ao carregar tela");
                    alert.setContentText("Erro ao carregar a tela de filmes: " + e.getMessage());
                    alert.showAndWait();
                }
            } else if (res instanceof Result.Failure<Void> f) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setResizable(true);
                alert.setTitle("Falha no login");
                alert.setContentText(f.message());
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
            Result<Void> res = SessionManager.getInstance()
                    .getUserClientService().create(usuario, senha);
            if (res instanceof Result.Success<Void>) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setContentText("Usuário criado com sucesso!");
                alert.showAndWait();
            } else if (res instanceof Result.Failure<Void> f) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Algo deu errado");
                alert.setContentText(f.message());
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
}