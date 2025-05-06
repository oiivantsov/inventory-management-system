/**
 * LanguageUtil manages localization settings such as the current locale and resource bundle.
 * It provides utilities for getting localized strings and setting a callback for language changes.
 */
package com.komeetta.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * LanguageUtil class provides methods to manage localization settings in the application.
 * It allows setting the current locale, retrieving localized strings, and setting a callback
 * for when the language changes.
 */
public class LanguageUtil {

    // Holds the current locale, defaults to English
    private static Locale currentLocale = new Locale("en");

    // Resource bundle for current locale
    private static ResourceBundle bundle = ResourceBundle.getBundle("UIMessages", currentLocale);

    // Optional callback to be triggered when language is changed
    private static Runnable onLanguageChange;

    /**
     * Sets the application locale and updates the resource bundle.
     * Triggers the language change callback if defined.
     *
     * @param locale the new Locale to set
     */
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