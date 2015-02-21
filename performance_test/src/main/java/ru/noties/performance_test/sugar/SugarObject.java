package ru.noties.performance_test.sugar;

import com.orm.SugarRecord;

import ru.noties.performance_test.IObject;

/**
 * Created by Dimitry Ivanov (dima.ivanov@cleverpumpkin.ru) / macbook on 13/02/15.
 */
// Generic is never used...
public class SugarObject extends SugarRecord<SugarObject> implements IObject {

    private String someString;

    private long someLong;

    private short someShort;

    private boolean someBool;

    private float someFloat;

    private double someDouble;

    private int someInt;

    public String getSomeString() {
        return someString;
    }

    @Override
    public void setId(long i) {
        id = i;
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
