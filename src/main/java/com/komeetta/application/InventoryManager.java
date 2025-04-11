package com.komeetta.application;

import com.komeetta.view.LoginGUI;
import com.komeetta.view.SalesPurchaseGUI;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Application;


public class InventoryManager{

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();
        System.setProperty("JDBC_URL", dotenv.get("JDBC_URL"));
        System.setProperty("JDBC_USER", dotenv.get("JDBC_USER"));
        System.setProperty("JDBC_PASSWORD", dotenv.get("JDBC_PASSWORD"));

        Application.launch(LoginGUI.class);

    }
}