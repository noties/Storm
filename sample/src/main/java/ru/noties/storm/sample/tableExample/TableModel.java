package ru.noties.storm.sample.tableExample;

import java.util.Date;
import java.util.List;

import ru.noties.storm.anno.Autoincrement;
import ru.noties.storm.anno.Column;
import ru.noties.storm.anno.DBNonNull;
import ru.noties.storm.anno.Default;
import ru.noties.storm.anno.Index;
import ru.noties.storm.anno.PrimaryKey;
import ru.noties.storm.anno.Table;
import ru.noties.storm.anno.Unique;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 15.02.2015.
 */

/**
 * Notify that this object represents some table.
 * In oder to be a table, class of this object
 * should be passed to Database manager's constructor
 */
@Table("table_model")
public class TableModel {

    /**
     * Supported types: all primitives(except single byte) & String
     * byte[], short, int, long, boolean (represented as int (1 or 0)), float, double, String
     */

    public static final String COL_ID = "id";

    /**
     * @@Column - To mark a field as a column annotate it with @Column.
     * By default column in a table will be named as a field's name,
     * but if you wish to name it differently pass
     * a name to the @Column("my_other_name")
     * It's crucial to have this annotation on every column you intent to create in your table
     *
     * @@PrimaryKey - To create a primary key column annotate with @PrimaryKey
     * Note, SQLite supports `multiple` primary keys in one table (PRIMARY KEY(col1, col2),
     * but most of the time it is not what was intended. Storm doesn't support that feature -
     * one table - one primary key
     *
     * @@AutoIncrement - For an autoincrement column annotate with @Autoincrement
     */
    @Column(COL_ID)
    @PrimaryKey
    @Autoincrement
    private long id;

    @Column("different_name")
    private long id2;

    /**
     * @@Index - To create an index annotate with @Index and pass the index's name
     */
    @Column
    @Index("table_model_some_value_index")
    private int someValue;

    /**
     * @@DBNonNull - to indicate that column should NOT be NULL annotate with @DBNonNull
     */
    @Column
    @DBNonNull
    private String serverId;

    /**
     * @@Default - to set SQLite default value annotate with @Default
     * It's a good thing for a new columns
     */
    @Column
    @Default("33")
    private int someInt;

    /**
     * @@Unique - To create a unique column annotate with @Unique
     * Unique does not imply that column should not be null
     */
    @Column
    @Unique
    private long time;

    /**
     * This is a type with registered TypeSerializer.
     * In order to register it - call Storm.getInstance().registerTypeSerializer(Class, {Type}Serializer)
     * Note, serializer must be one of the following:
     * ShortSerializer, IntSerializer, LongSerializer, FloatSerializer,
     * DoubleSerializer, StringSerializer, BooleanSerializer, ByteArraySerializer
     */
    @Column
    private Date date;

    /**
     * It's a bad idea. Type serializers are stored in a map with Class as a key.
     * In this case Class will be List.class, so you wont be able
     * to serialize/deserialize desired field.
     * Don't do that
     */
    @Column
    private List<String> list;

    /**
     * It's better, and definitely will work as desired (if you provided a working serialization)
     * But mostly it's a bad SQL practice
     */
    @Column
    private ListString listString;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId2() {
        return id2;
    }

    public void setId2(long id2) {
        this.id2 = id2;
    }

    public int getSomeValue() {
        return someValue;
    }

    public void setSomeValue(int someValue) {
        this.someValue = someValue;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public int getSomeInt() {
        return someInt;
    }

    public void setSomeInt(int someInt) {
        this.someInt = someInt;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static class ListString {

        private List<String> list;

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }
    }

    public static enum SomeEnum {
        DO, DO_NOT, NOT
    }
}
