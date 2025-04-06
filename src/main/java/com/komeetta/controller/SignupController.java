package com.komeetta.controller;

import java.io.IOException;

import com.komeetta.dao.UserDAO;
import com.komeetta.model.LanguageOption;
import com.komeetta.model.LanguageUtil;
import com.komeetta.model.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;
import java.text.MessageFormat;

public class SignupController {

    // Variables for view objects
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private ResourceBundle resources;

    @FXML
    private ComboBox<LanguageOption> languageSelector;

    // Variable for storing and sending user object to the next view
    private User user;

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
        }
        );
    }


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

    // Called when the Signup button is clicked
    @FXML
    private void handleSignupAction(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate inputs if valid create user in database
        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, resources.getString("str_signup_failed_title"), resources.getString("str_fill_all_fields"));
        } else if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, resources.getString("str_signup_failed_title"), resources.getString("str_password_mismatch"));
        } else {
        	UserDAO uDAO = new UserDAO();
        	user = new User(username, password);
        	if(!uDAO.isUsernameAvailable(username)){
                showAlert(Alert.AlertType.INFORMATION, resources.getString("str_signup_failed_title"), resources.getString("str_username_taken"));
        	}else {
        		uDAO.addUser(user);
                showAlert(Alert.AlertType.INFORMATION, resources.getString("str_signup_success"),
                        MessageFormat.format(resources.getString("str_welcome_message"), firstName, lastName));

        		// Send to Dashboard here
        		try {
                    // Load the new FXML file
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Dashboard.fxml"), ResourceBundle.getBundle("UIMessages", LanguageUtil.getCurrentLocale()));
                    Parent root = loader.load();

                    // Send User variable to the next view
                    DashboardController controller = loader.getController();
                    controller.setInitialView(user);

                    // Get the current stage (window)
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    // Set the new scene
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setTitle("Dashboard");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        	}
            
        	
        }
    }

    // Used to show user alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
