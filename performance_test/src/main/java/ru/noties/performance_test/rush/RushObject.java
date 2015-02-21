package ru.noties.performance_test.rush;

import co.uk.rushorm.core.annotations.RushTableAnnotation;
import ru.noties.performance_test.IObject;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 13.02.2015.
 */
@RushTableAnnotation
public class RushObject extends co.uk.rushorm.core.RushObject implements IObject {

//    private long id;

    private String someString;

    private long someLong;

    private short someShort;

    private boolean someBool;

    private float someFloat;

    private double someDouble;

    private int someInt;

//    @Override
//    public long getId() {
//        return id;
//    }

    @Override
    public void setId(long id) {
//        this.id = id;
        // cannot set id?
    }

    public String getSomeString() {
        return someString;
    }

    @Override
    public void setSomeString(String someString) {
        this.someString = someString;
    }

    public long getSomeLong() {
        return someLong;
    }

    @Override
    public void setSomeLong(long someLong) {
        this.someLong = someLong;
    }

    public short getSomeShort() {
        return someShort;
    }

    @Override
    public void setSomeShort(short someShort) {
        this.someShort = someShort;
    }

    public boolean isSomeBool() {
        return someBool;
    }

    @Override
    public void setSomeBool(boolean someBool) {
        this.someBool = someBool;
    }

    public float getSomeFloat() {
        return someFloat;
    }

    @Override
    public void setSomeFloat(float someFloat) {
        this.someFloat = someFloat;
    }

    public double getSomeDouble() {
        return someDouble;
    }

    @Override
    public void setSomeDouble(double someDouble) {
        this.someDouble = someDouble;
    }

    public int getSomeInt() {
        return someInt;
    }

    @Override
    public void setSomeInt(int someInt) {
        this.someInt = someInt;
    }
}
