package ru.noties.performance_test.raw;

import ru.noties.performance_test.IObject;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 14.02.2015.
 */
public class RawObject implements IObject {

    public static final String TABLE_NAME = "raw_object";

    public static final String COL_ID = "id";
    public static final String COL_SOME_STRING = "someString";
    public static final String COL_SOME_SHORT = "someShort";
    public static final String COL_SOME_BOOL = "someBool";
    public static final String COL_SOME_FLOAT = "someFloat";
    public static final String COL_SOME_DOUBLE = "someDouble";
    public static final String COL_SOME_INT = "someInt";
    public static final String COL_SOME_LONG = "someLong";

    private long id;

    private String someString;

    private long someLong;

    private short someShort;

    private boolean someBool;

    private float someFloat;

    private double someDouble;

    private int someInt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSomeString() {
        return someString;
    }

    public void setSomeString(String someString) {
        this.someString = someString;
    }

    public long getSomeLong() {
        return someLong;
    }

    public void setSomeLong(long someLong) {
        this.someLong = someLong;
    }

    public short getSomeShort() {
        return someShort;
    }

    public void setSomeShort(short someShort) {
        this.someShort = someShort;
    }

    public boolean isSomeBool() {
        return someBool;
    }

    public void setSomeBool(boolean someBool) {
        this.someBool = someBool;
    }

    public float getSomeFloat() {
        return someFloat;
    }

    public void setSomeFloat(float someFloat) {
        this.someFloat = someFloat;
    }

    public double getSomeDouble() {
        return someDouble;
    }

    public void setSomeDouble(double someDouble) {
        this.someDouble = someDouble;
    }

    public int getSomeInt() {
        return someInt;
    }

    public void setSomeInt(int someInt) {
        this.someInt = someInt;
    }
}
