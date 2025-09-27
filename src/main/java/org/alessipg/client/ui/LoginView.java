package org.alessipg.client.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class LoginView extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/org/alessipg/client/ui/LoginView.fxml"));
        primaryStage.setTitle("VoteFlix");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
	
}