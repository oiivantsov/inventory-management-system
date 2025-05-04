package com.komeetta.model;

/**
 * Represents a language option for the application.
 * It has the following attributes:
 * - name: the name of the language
 * - code: the code of the language
 */
public class LanguageOption {
    private final String name;
    private final String code;

    public LanguageOption(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return name + " - " + code;
    }
}
