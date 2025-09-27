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

import org.alessipg.client.app.clientservice.AuthClientService;
import org.alessipg.shared.enums.StatusTable;

import javafx.event.ActionEvent;

public class LoginViewController {
    private final AuthClientService authService = new AuthClientService();

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtSenha;

    @FXML
    private void onLogin(ActionEvent event) {
        String usuario = txtUsuario.getText();
        String senha = txtSenha.getText();
        try {
            StatusTable res = authService.login(usuario, senha);
            switch (res) {
                case StatusTable.OK:
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/org/alessipg/client/ui/FilmesView.fxml"));
                    Parent novaTelaRoot = loader.load();
                    Stage stage = (Stage) txtUsuario.getScene().getWindow();
                    stage.setTitle("Filmes");
                    stage.setScene(new Scene(novaTelaRoot));
                    break;
                case StatusTable.UNAUTHORIZED: {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Não autorizado");
                    alert.setContentText("Usuário ou senha incorretos!");
                    alert.showAndWait();
                    break;
                }
                case StatusTable.INTERNAL_SERVER_ERROR: {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Erro interno");
                    alert.setContentText("Erro no servidor!");
                    alert.showAndWait();
                    break;
                }
                default:
                    break;

            }
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro de conexão");
            alert.setContentText("Erro ao conectar com o servidor: " + e.getMessage());
            alert.showAndWait();
        }
    }
}