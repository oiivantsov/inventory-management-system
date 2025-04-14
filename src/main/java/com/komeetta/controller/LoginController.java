/**
 * LoginController manages the login screen behavior, including language selection,
 * user authentication, and navigation to the dashboard or signup view.
 */
package com.komeetta.controller;

import com.komeetta.dao.UserDAO;
import com.komeetta.model.LanguageOption;
import com.komeetta.model.User;
import com.komeetta.util.LanguageUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private ResourceBundle resources;
    @FXML private ComboBox<LanguageOption> languageSelector;

    private User user;

    /**
     * Initializes the login screen, populates the language selector and sets event handlers.
     */
    @FXML
    public void initialize() {
        languageSelector.getItems().addAll(
                new LanguageOption("English", "EN"),
                new LanguageOption("Finnish", "FI"),
                new LanguageOption("Russian", "RU"),
                new LanguageOption("Japanese", "JA")
        );

        Locale currentLocale = LanguageUtil.getCurrentLocale();
        String currentLangCode = currentLocale.getLanguage().toUpperCase();

        for (LanguageOption option : languageSelector.getItems()) {
            if (option.getCode().equalsIgnoreCase(currentLangCode)) {
                languageSelector.getSelectionModel().select(option);
                break;
            }
        }

        languageSelector.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(LanguageOption item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getCode());
            }
        });

        languageSelector.setOnAction(e -> {
            LanguageOption selected = languageSelector.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Locale newLocale = switch (selected.getCode()) {
                    case "FI" -> new Locale("fi");
                    case "RU" -> new Locale("ru");
                    case "JA" -> new Locale("ja");
                    default -> new Locale("en");
                };
                if (!LanguageUtil.getCurrentLocale().equals(newLocale)) {
                    LanguageUtil.setLocale(newLocale);
                    reloadScene();
                }
            }
        });
    }

    /**
     * Reloads the login scene with the current selected language.
     */
    private void reloadScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Login.fxml"));
            loader.setResources(ResourceBundle.getBundle("UIMessages", LanguageUtil.getCurrentLocale()));
            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(LanguageUtil.getString("str_login_form"));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Authenticates user credentials and loads the dashboard if successful.
     */
    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        UserDAO uDAO = new UserDAO();
        if (uDAO.authenticate(username, password)) {
            System.out.println("Login successful");
            user = uDAO.getUser(username);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Dashboard.fxml"), ResourceBundle.getBundle("UIMessages", LanguageUtil.getCurrentLocale()));
                Parent root = loader.load();

                DashboardController controller = loader.getController();
                controller.setInitialView(user);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root, 1100, 570));
                stage.setTitle("Dashboard");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Wrong credentials");
            showAlert(Alert.AlertType.INFORMATION,
                    resources.getString("str_signin_failed_title"),
                    resources.getString("str_wrong_credentials"));
        }
    }

    /**
     * Navigates the user to the signup view.
     */
    @FXML
    private void handleSignupButtonAction(ActionEvent event) {
        System.out.println("Redirecting to the registration page...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Signup.fxml"));
            loader.setResources(ResourceBundle.getBundle("UIMessages", LanguageUtil.getCurrentLocale()));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(resources.getString("str_register_window_title"));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays an alert dialog with the provided message.
     * @param alertType The type of alert (e.g., INFORMATION, ERROR)
     * @param title The title of the alert dialog
     * @param message The message to display in the alert
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}