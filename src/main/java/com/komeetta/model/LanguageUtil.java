package com.komeetta.model;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageUtil {
    private static Locale currentLocale = new Locale("en");
    private static ResourceBundle bundle = ResourceBundle.getBundle("UIMessages", currentLocale);

    private static Runnable onLanguageChange;

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("UIMessages", currentLocale);
        if (onLanguageChange != null) {
            onLanguageChange.run();
        }
    }

    public static String getString(String key) {
        return bundle.getString(key);
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    public static void setOnLanguageChange(Runnable callback) {
        onLanguageChange = callback;
    }
}

