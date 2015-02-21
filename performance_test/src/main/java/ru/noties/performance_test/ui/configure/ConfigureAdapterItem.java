package ru.noties.performance_test.ui.configure;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 15.02.2015.
 */
public class ConfigureAdapterItem {

    private final String name;
    private boolean checked;

    public ConfigureAdapterItem(String name) {
        this(name, false);
    }

    public ConfigureAdapterItem(String name, boolean checked) {
        this.name = name;
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
