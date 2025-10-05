package org.alessipg.client.ui;

import java.io.IOException;

import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.shared.domain.model.User;
import org.alessipg.shared.records.UserSelfGetResponse;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ProfileViewController {

    @FXML
    private Label lblUser;

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
}
