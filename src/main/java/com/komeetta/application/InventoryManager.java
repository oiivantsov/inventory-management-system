package com.komeetta.application;

import com.komeetta.view.LoginGUI;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Application;

public class InventoryManager{

    public static void main(String[] args) {

        /*
        ------------------------------------ REMOVED FOR SIMPLICITY ------------------------------------


        load env variables (you need to create a .env file in the root of the project)

        Dotenv dotenv = Dotenv.load();
        System.setProperty("JDBC_URL", dotenv.get("JDBC_URL"));
        System.setProperty("JDBC_USER", dotenv.get("JDBC_USER"));
        System.setProperty("JDBC_PASSWORD", dotenv.get("JDBC_PASSWORD"));
        launch(args);

        ------------------------------------ REMOVED FOR SIMPLICITY ------------------------------------
        */

        // Launch the GUI
        Application.launch(LoginGUI.class);




    }
}