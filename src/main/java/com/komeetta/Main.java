package com.komeetta;

import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) {

        // load env variables (you need to create a .env file in the root of the project)
        Dotenv dotenv = Dotenv.load();
        System.setProperty("JDBC_URL", dotenv.get("JDBC_URL"));
        System.setProperty("JDBC_USER", dotenv.get("JDBC_USER"));
        System.setProperty("JDBC_PASSWORD", dotenv.get("JDBC_PASSWORD"));

    }
}
