package ru.noties.performance_test;

import java.util.List;

import ru.noties.performance_test.ui.AdapterItem;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 14.02.2015.
 */
public class TestEvent {

    private final List<AdapterItem> items;

    public TestEvent(List<AdapterItem> items) {
        this.items = items;
    }

    public List<AdapterItem> getItems() {
        return items;
    }
}
