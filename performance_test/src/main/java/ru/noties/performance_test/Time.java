package ru.noties.performance_test;

/**
* Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 14.02.2015.
*/
public enum Time {

    MILLIS  (R.string.millis_pattern),
    NANOS   (R.string.nanos_pattern);

    private final int patternResId;

    Time(int resId) {
        this.patternResId = resId;
    }

    public int getPatternResId() {
        return patternResId;
    }
}
