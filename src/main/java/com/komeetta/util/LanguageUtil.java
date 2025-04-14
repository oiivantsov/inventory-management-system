/**
 * LanguageUtil manages localization settings such as the current locale and resource bundle.
 * It provides utilities for getting localized strings and setting a callback for language changes.
 */
package com.komeetta.util;

import java.util.Locale;
import java.util.ResourceBundle;

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

    /**
     * Retrieves the localized string for the given key from the resource bundle.
     *
     * @param key the key to look up
     * @return the localized string
     */
    public static String getString(String key) {
        return bundle.getString(key);
    }

    /**
     * Returns the currently set locale.
     *
     * @return the current Locale
     */
    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    /**
     * Sets a callback to be run when the locale changes.
     *
     * @param callback a Runnable to execute on language change
     */
    public static void setOnLanguageChange(Runnable callback) {
        onLanguageChange = callback;
    }
}