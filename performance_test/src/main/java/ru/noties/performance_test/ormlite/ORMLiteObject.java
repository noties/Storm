package ru.noties.performance_test.ormlite;
//
//import com.j256.ormlite.field.DatabaseField;
//import com.j256.ormlite.table.DatabaseTable;
//
//import ru.noties.performance_test.IObject;
//
///**
// * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 14.02.2015.
// */
//@DatabaseTable(tableName = "ormlite_object")
//public class ORMLiteObject implements IObject {
//
//    @DatabaseField(generatedId = true)
//    private long id;
//
//    @DatabaseField
//    private String someString;
//
//    @DatabaseField
//    private long someLong;
//
//    @DatabaseField
//    private short someShort; // does not have short support
//
//    @DatabaseField
//    private boolean someBool;
//
//    @DatabaseField
//    private float someFloat;
//
//    @DatabaseField
//    private double someDouble;
//
//    @DatabaseField
//    private int someInt;
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public String getSomeString() {
//        return someString;
//    }
//
//    public void setSomeString(String someString) {
//        this.someString = someString;
//    }
//
//    public long getSomeLong() {
//        return someLong;
//    }
//
//    public void setSomeLong(long someLong) {
//        this.someLong = someLong;
//    }
//
//    public short getSomeShort() {
//        return someShort;
//    }
//
//    public void setSomeShort(short someShort) {
//        this.someShort = someShort;
//    }
//
//    public boolean isSomeBool() {
//        return someBool;
//    }
//
//    public void setSomeBool(boolean someBool) {
//        this.someBool = someBool;
//    }
//
//    public float getSomeFloat() {
//        return someFloat;
//    }
//
//    public void setSomeFloat(float someFloat) {
//        this.someFloat = someFloat;
//    }
//
//    public double getSomeDouble() {
//        return someDouble;
//    }
//
//    public void setSomeDouble(double someDouble) {
//        this.someDouble = someDouble;
//    }
//
//    public int getSomeInt() {
//        return someInt;
//    }
//
//    public void setSomeInt(int someInt) {
//        this.someInt = someInt;
//    }
//}
