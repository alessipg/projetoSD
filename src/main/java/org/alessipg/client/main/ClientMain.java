package org.alessipg.client.main;

import org.alessipg.client.infra.config.ClientContainer;
import org.alessipg.client.infra.session.SessionManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/org/alessipg/client/ui/TcpConnection.fxml"));
        primaryStage.setTitle("Conexão TCP");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
