package com.komeetta.model;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageUtil {
    private static Locale currentLocale = new Locale("en"); // Default locale
    private static ResourceBundle bundle = ResourceBundle.getBundle("UIMessages", currentLocale);

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("UIMessages", currentLocale);
    }

    public static String getString(String key) {
        return bundle.getString(key);
    }
}
