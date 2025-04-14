package com.komeetta.application;

import com.komeetta.view.LoginGUI;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Application;


public class InventoryManager{

    public static void main(String[] args) {

        // Load environment variables from .env file (e.g., database credentials)
        Dotenv dotenv = Dotenv.load();

        // Set system properties for JDBC database connection
        System.setProperty("JDBC_URL", dotenv.get("JDBC_URL"));
        System.setProperty("JDBC_USER", dotenv.get("JDBC_USER"));
        System.setProperty("JDBC_PASSWORD", dotenv.get("JDBC_PASSWORD"));

        // Launch the JavaFX application starting with the LoginGUI screen
        Application.launch(LoginGUI.class);

    }
}