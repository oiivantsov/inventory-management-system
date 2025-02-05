package com.komeetta;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("\\view\\Login.fxml"));
        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("Login Form");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {


        // load env variables (you need to create a .env file in the root of the project)
        Dotenv dotenv = Dotenv.load();
        System.setProperty("JDBC_URL", dotenv.get("JDBC_URL"));
        System.setProperty("JDBC_USER", dotenv.get("JDBC_USER"));
        System.setProperty("JDBC_PASSWORD", dotenv.get("JDBC_PASSWORD"));

        launch(args);

    }
}