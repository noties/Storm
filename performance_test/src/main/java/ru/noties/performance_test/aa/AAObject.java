package ru.noties.performance_test.aa;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import ru.noties.performance_test.IObject;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 13.02.2015.
 */
@Table(name = "aa_object")
public class AAObject extends Model implements IObject {

    // do not add this
//    @Column(name = "id")
//    private long id;



    @Column
    private String someString;

    @Column
    private long someLong;

    @Column
    private int someShort; // does not have short support

    @Column
    private boolean someBool;

    @Column
    private float someFloat;

    @Column
    private double someDouble;

    @Column
    private int someInt;
//
//    @Override
//    public long getId() {
//        return id;
//    }

    public String getSomeString() {
        return someString;
    }

    @Override
    public void setId(long id) {

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

//    public short getSomeShort() {
//        return someShort;
//    }

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
