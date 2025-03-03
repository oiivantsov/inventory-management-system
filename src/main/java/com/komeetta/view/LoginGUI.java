package com.komeetta.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class LoginGUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {    // change this to the correct path when needed
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 400);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Login Form");
        primaryStage.show();
    }


}
