/**
 * SignupController handles user registration and localization.
 * It allows users to enter details, validates input, creates a new user,
 * and navigates to the dashboard on success. It also supports multilingual UI.
 */
package com.komeetta.controller;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

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

public class SignupController {

    // UI elements for registration form
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ResourceBundle resources;
    @FXML private ComboBox<LanguageOption> languageSelector;

    private User user;

    /**
     * Initializes the signup screen and sets up the language selector.
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
     * Reloads the signup scene using the currently selected language.
     */
    private void reloadScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Signup.fxml"));
            loader.setResources(ResourceBundle.getBundle("UIMessages", LanguageUtil.getCurrentLocale()));
            Parent root = loader.load();

            Stage stage = (Stage) firstNameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(LanguageUtil.getString("str_register_window_title"));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles signup form submission: validates inputs, checks for username availability,
     * creates a new user, and redirects to the dashboard.
     */
    @FXML
    private void handleSignupAction(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, resources.getString("str_signup_failed_title"),
                    resources.getString("str_fill_all_fields"));
        } else if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, resources.getString("str_signup_failed_title"),
                    resources.getString("str_password_mismatch"));
        } else {
            UserDAO uDAO = new UserDAO();
            user = new User(username, password);

            if (!uDAO.isUsernameAvailable(username)) {
                showAlert(Alert.AlertType.INFORMATION, resources.getString("str_signup_failed_title"),
                        resources.getString("str_username_taken"));
            } else {
                uDAO.addUser(user);
                showAlert(Alert.AlertType.INFORMATION, resources.getString("str_signup_success"),
                        MessageFormat.format(resources.getString("str_welcome_message"), firstName, lastName));

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Dashboard.fxml"),
                            ResourceBundle.getBundle("UIMessages", LanguageUtil.getCurrentLocale()));
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
            }
        }
    }

    /**
     * Displays an alert dialog with the given message.
     * @param alertType the type of alert (INFORMATION, ERROR, etc.)
     * @param title the title of the alert window
     * @param message the message shown inside the alert
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
