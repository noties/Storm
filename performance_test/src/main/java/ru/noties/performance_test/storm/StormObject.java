package ru.noties.performance_test.storm;

import ru.noties.storm.anno.Autoincrement;
import ru.noties.storm.anno.Column;
import ru.noties.storm.anno.PrimaryKey;
import ru.noties.storm.anno.Table;
import ru.noties.performance_test.IObject;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 31.01.2015.
 */
@Table(StormObject.TABLE_NAME)
public class StormObject implements IObject {

    public static final String TABLE_NAME = "storm_object";

    public static final String COL_ID = "id";

    @Column(COL_ID)
    @PrimaryKey
    @Autoincrement
    private long id;

    @Column
    private String someString;

    @Column
    private long someLong;

    @Column
    private short someShort;

    @Column
    private boolean someBool;

    @Column
    private float someFloat;

    @Column
    private double someDouble;

    @Column
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

    @Override
    public String toString() {
        return "TestObject{" +
                "id=" + id +
                ", someString='" + someString + '\'' +
                ", someLong=" + someLong +
                ", someShort=" + someShort +
                ", someBool=" + someBool +
                ", someFloat=" + someFloat +
                ", someDouble=" + someDouble +
                ", someInt=" + someInt +
                '}';
    }
}
