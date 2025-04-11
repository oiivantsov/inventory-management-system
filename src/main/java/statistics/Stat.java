package statistics;

import javafx.beans.property.SimpleStringProperty;

/**
 * Class for the statistics
 * Contains the name and value of a statistic
 * Used in the statistics table in the GUI
 */
// this is for the GUI's tableview, so it cannot be removed
public class Stat {
    private final SimpleStringProperty name;
    private final SimpleStringProperty value;

    public Stat(String name, String value) {
        this.name = new SimpleStringProperty(name);
        this.value = new SimpleStringProperty(value);
    }

    public String getName() {
        return name.get();
    }

    public String getValue() {
        return value.get();
    }
}