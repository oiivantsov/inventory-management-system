package com.komeetta.view;

import com.komeetta.model.LanguageUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class LoginGUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("UIMessages", LanguageUtil.getCurrentLocale());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Login.fxml"));
        loader.setResources(bundle);
        Parent root = loader.load();

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);

        primaryStage.setTitle(bundle.getString("str_login_form"));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
