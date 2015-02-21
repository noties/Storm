package ru.noties.performance_test.sprkls;

import ru.noties.performance_test.IObject;
import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.annotations.AutoIncrement;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Key;
import se.emilsjolander.sprinkles.annotations.Table;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 31.01.2015.
 */
@Table(SprinklesObject.TABLE_NAME)
public class SprinklesObject extends Model implements IObject {

    public static final String TABLE_NAME = "sprinkles_object";

    public static final String COL_ID = "id";

    @Key
    @AutoIncrement
    @Column(COL_ID)
    private long id;

    @Column("someString")
    private String someString;

    @Column("someLong")
    private long someLong;

    @Column("someShort")
    private short someShort;

    @Column("someBool")
    private boolean someBool;

    @Column("someFloat")
    private float someFloat;

    @Column("someDouble")
    private double someDouble;

    @Column("someInt")
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
