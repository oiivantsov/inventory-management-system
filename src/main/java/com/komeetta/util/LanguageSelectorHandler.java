/**
 * LanguageSelectorHandler provides utility methods for initializing and handling language selection
 * in JavaFX ComboBoxes, enabling dynamic localization for the user interface.
 */
package com.komeetta.util;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import com.komeetta.model.LanguageOption;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * LanguageSelectorHandler class provides methods to set up and handle language selection
 * in a JavaFX application. It populates a ComboBox with available languages, sets the current
 * system language, and handles language change events to update the UI accordingly.
 */
public class LanguageSelectorHandler {

    /**
     * Sets up the language selector ComboBox with available language options and custom rendering.
     * Automatically selects the current system language if it exists in the list.
     *
     * @param languageSelector the ComboBox to populate
     * @param errorLabel optional label for future use (e.g. errors or tooltips)
     */
    public static void setupLanguageSelector(ComboBox<LanguageOption> languageSelector, Label errorLabel) {
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

        languageSelector.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(LanguageOption item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName() + " - " + item.getCode());
            }
        });

        languageSelector.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(LanguageOption item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getCode());
            }
        });
    }

    /**
     * Handles a language change event from the language selector ComboBox.
     * If a new language is selected, it updates the application locale and reloads the UI.
     *
     * @param languageSelector the ComboBox that triggered the language change
     */
    public static void handleLanguageChange(ComboBox<LanguageOption> languageSelector) {
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
                reloadUIWithBundle(languageSelector, ResourceBundle.getBundle("UIMessages", newLocale));
            }
        }
    }

    /**
     * Reloads the Dashboard UI using the provided resource bundle.
     * This enables dynamic text translation based on the selected locale.
     *
     * @param languageSelector the ComboBox used to get the current Stage
     * @param bundle the ResourceBundle to load the new UI content
     */
    private static void reloadUIWithBundle(ComboBox<LanguageOption> languageSelector, ResourceBundle bundle) {
        try {
            FXMLLoader loader = new FXMLLoader(LanguageSelectorHandler.class.getResource("/Scenes/Dashboard.fxml"), bundle);
            Parent root = loader.load();

            Stage stage = (Stage) languageSelector.getScene().getWindow();
            stage.setTitle(bundle.getString("str_dashboard"));
            stage.setScene(new Scene(root, 1100, 570));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
