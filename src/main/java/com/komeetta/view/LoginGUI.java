/**
 * LoginGUI is the entry point of the application. It sets up and launches the login window.
 * It initializes the JavaFX application using a localized FXML layout and resource bundle.
 */
package com.komeetta.view;

import com.komeetta.util.LanguageUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * LoginGUI class is responsible for displaying the login window of the application.
 * It uses JavaFX to create the GUI and loads the FXML layout with localization support.
 */
public class LoginGUI extends Application {

    /**
     * Starts the JavaFX application and displays the login window.
     * Loads the Login.fxml file using the current locale's resource bundle.
     *
     * @param primaryStage the main application stage
     * @throws Exception if loading the FXML file fails
     */
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

    /**
     * Main method to launch the JavaFX application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}