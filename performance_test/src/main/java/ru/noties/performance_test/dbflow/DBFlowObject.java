package ru.noties.performance_test.dbflow;

//import com.raizlabs.android.dbflow.annotation.Column;
//import com.raizlabs.android.dbflow.annotation.Table;
//import com.raizlabs.android.dbflow.structure.BaseModel;

//import ru.noties.performance_test.IObject;
//
///**
// * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 12.02.2015.
// */
//@Table(value = DBFlowObject.TABLE_NAME, databaseName = "db_flow")
//public class DBFlowObject extends BaseModel implements IObject {
//
//    // fields must be with (at least) default access modifier
//
//    public static final String TABLE_NAME = "db_flow_object";
//
//    public static final String COL_ID = "id";
//
//    @Column(columnType = Column.PRIMARY_KEY_AUTO_INCREMENT, name = COL_ID)
//    long id;
//
//    @Column
//    String someString;
//
//    @Column
//    long someLong;
//
//    @Column
//    short someShort;
//
//    @Column
//    boolean someBool;
//
//    @Column
//    float someFloat;
//
//    @Column
//    double someDouble;
//
//    @Column
//    int someInt;
//
//    public long getId() {
//        return id;
//    }
//
//    @Override
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public String getSomeString() {
//        return someString;
//    }
//
//    @Override
//    public void setSomeString(String someString) {
//        this.someString = someString;
//    }
//
//    public long getSomeLong() {
//        return someLong;
//    }
//
//    @Override
//    public void setSomeLong(long someLong) {
//        this.someLong = someLong;
//    }
//
//    public short getSomeShort() {
//        return someShort;
//    }
//
//    @Override
//    public void setSomeShort(short someShort) {
//        this.someShort = someShort;
//    }
//
//    public boolean isSomeBool() {
//        return someBool;
//    }
//
//    @Override
//    public void setSomeBool(boolean someBool) {
//        this.someBool = someBool;
//    }
//
//    public float getSomeFloat() {
//        return someFloat;
//    }
//
//    @Override
//    public void setSomeFloat(float someFloat) {
//        this.someFloat = someFloat;
//    }
//
//    public double getSomeDouble() {
//        return someDouble;
//    }
//
//    @Override
//    public void setSomeDouble(double someDouble) {
//        this.someDouble = someDouble;
//    }
//
//    public int getSomeInt() {
//        return someInt;
//    }
//
//    @Override
//    public void setSomeInt(int someInt) {
//        this.someInt = someInt;
//    }
//}
