# Storm
Android SQLite manager

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Storm-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1584)

### Introduction
Although there are letters `[ORM]` in title of this library it's not really an [ORM](https://en.wikipedia.org/wiki/Object-relational_mapping). Mostly because there are no object relations out of box (but still it could be done, more on this later). Think of it as an echanted helper for working with SQLite database on Android. At almost no price you get:

* Easy table creation via @Annotations
* Easy migration via @Annotations
* Nearly native SQLite speed at inserting, updating and selecting operations
* No need to parse Cursors by hand / No need to init ContentValues by hand
* Straight-forward API
* Support for multiple database files
* Any Object could be a model, no need to extend or implement anything
* Easy serialization/deserialization of not supported by SQLite types
* Android-orientired library


### Getting started
```java
compile 'ru.noties:storm:1.0.3' // check for latest version in this repo's releases tab
```


#### Table creation

Supported annotations during table creation:

* @Column
* @PrimaryKey
* @Autoincrement
* @DBNonNull
* @Unique
* @Index
* @Default
* @ForeignKey

Supported fields' types:

* boolean (note, that it's represented as int (`1` or `0`, don't forget that if you are doing some selection by this field) (also think of suppling @Default("0") or @Default("1") for this type)
* short
* int
* long
* float
* double
* byte[]
* String
* and any other type that has registered TypeSerializer

```java
@Table("my_super_table")
public class MySuperTable {
	
    @Column
    @PrimaryKey
    @Autoincrement
    private long id;
    
    @Column
    @DBNonNull
    private String otherColumn;
    
    @Column
    @Unique
    private int kindOfId;
    
    @Column
    @Index("my_super_table_date_index")
    private long date;
    
    @Column("other_title_for_this_column")
    @Default("0")
    private int color;
    
    @Column("id_of_other_table")
    @ForeignKey(
        parentTable = OtherTable.class,
        parentColumnName = OtherTable.COL_ID,
        onDelete = ForeignKeyAction.CASCADE,
        onUpdate = ForeignKeyAction.NO_ACTION
	)
    private long idOfOtherTable;
    
    @Column("some_column_that_has_registered_type_serializer")
    private SomeObject serializedObject;
}
```

#### Table migration

Supported annotations during table migration:

* @NewTable
* @NewColumn

```java
@Table("new_table_for_version_2")
@NewTable(2)
public class MySuperNewTable {
	// the same as table creation
}
```

```java
@Table("my_old_table_that_need_refreshement")
public class OldTableThatNeedRefreshment {

	// some columns
    
    @Column
    @NewColumn(2)
    private String newData;
    
    @Column
    @Index("new_column_index_name")
    @Unique
    @NewColumn(2)
    private long date;
}
```

#### Initialization

The main entry point for initialization is

```java
Storm.getInstance().init(Context applicationContext, boolean isDebug);
```

This method should be called only once (the second time it will throw an Exception). So, the best place to call it is in your Application's ```onCreate()``` method. This will NOT open any connections, just establish required linking for library's fuctionality.

Additionally, during initialization you could supply an InstanceCreator or TypeSerializer.

**InstanceCreator**

By default all objects are constructed via ReflectionInstanceCreator. It almost has no performance penalty, but still if you wish to smooth things a little or if your model object has no spare empty constructor please use the following:

```java
final Storm storm = Storm.getInstance();
storm.registerInstanceCreator(SampleObject.class, new InstanceCreator<SampleObject>() {
	@Override
	public SampleObject create(Class<SampleObject> clazz) {
		return new SampleObject();
    }
});
```

**TypeSerializer**

By default if field's type is not supported by SQLite it won't be in a table. But if you supply a TypeSerializer things change.

```java
final Storm storm = Storm.getInstance();
storm.registerTypeSerializer(Date.class, new LongSerializer<Date>() {
	@Override
	public Date deserialize(long value) {
		return new Date(value);
	}
    
	@Override
	public long serialize(Date value) {
		return value.getTime();
	}
});
```

There are 8 abstract serializers for each SQLite supported type:

* BooleanSerializer (INT) (well, there is no such type BOOL is SQLite, but still)
* ShortSerializer (INT)
* IntSerializer	(INT)
* LongSerializer (INT)
* FloatSerializer (REAL)
* DoubleSerializer (REAL)
* StringSerializer (TEXT)
* ByteArraySerializer (BLOB)

These open a possibility to store in any column nearly any type of object. Although it is supposed to be a very bad practice, you could use, for example, JSON to store complex short-lived objects, or [Kryo](https://github.com/EsotericSoftware/kryo). Of cause it could be used for good, for example, to query and parse object from other table:

```java
final Storm storm = Storm.getInstance();
storm.registerTypeSerializer(OtherTable.class, new LongSerializer<OtherTable>() {

  private final DatabaseManager mManager;
  {
      mManager = getManager();
  }

  @Override
  public OtherTable deserialize(long value) {
  	return Storm.newSelect(mManager).query(OtherTable.class, Selection.eq(OtherTable.COL_ID, value));
  }
  @Override
  public long serialize(OtherTable value) {
  	return value.getId();
  }
});
```

(TODO maybe this fuctionality should be implemented in library itself)

#### Preparing database for using

The main class to work with database is DatabaseManager. You pass it's instance to every database operation (insert, update, delete, select). This gives a possibility to use as much database files as one wishes.

```java
final DatabaseManager manager = new DatabaseManager(
	Context applicationContext,
    String databaseName,
    int databaseVersion,
    Class<?>[] arrayOfClassesThatThisConcreteDatabaseHas // see table creation
);
```

Additionally you could supply DatabaseManage's constructor a Pragma object, that will evaluate pragma statements on SQLite database connection opened:

```java
final Pragma pragma = new Pragma.Builder()
	.setSynchronous(Synchronous.FULL)
    .setForeignKeys(ForeignKeys.ON) // this is crucial if you are using foreign keys
    .setJournalMode(JournalMode.TRUNCATE)
    .setTempStore(TempStore.DEFAULT)
    .setCustomPragmas(List<String> customPragmas)
    .build();
```

On DatabaseManager construction it will build it's own cache for classes that are in this database. It might be wise to call it off the main thread if you have a lot of tables with a lot of columns. Still you could share DatabaseManager instance between different threads although it has no inner synchronization whatsoever. Read more about how SQLite synchronizes on Android and prefer as less open SQLite database connections as possible.

Under the hood DatabaseManager holds a SQLiteDatabase reference. So you should call `open()` to start really using it.

```java
manager.open();
```

Additionally you could catch SQLiteException duting method call:

```java
try {
	manager.open();
} catch (SQLiteException e) {
	// there was an error during opening
    manager = null;
}
```

Even more you could supply you own SQLiteOpenCallbacks to the `open()` method to be executed during `onCreate()`, `onUpgrade()`, `onOpen()` of SQLiteDatabase:

```java
manager.open(new SQLiteOpenCallbacks() {
  public void onCreate (SQLiteDatabase db) {}
  public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {}
  public void onOpen (SQLiteDatabase db) {}
});
```

Now you are all set and ready to go.

#### SQLite operations

**INSERT**

```java
try {
	Storm.newInsert(mManager).insert(list, boolean shouldNotify *true, boolean setPrimaryKey *false); // * - optional
} catch (StormException e) {
	e.printStackTrace();
}
```

For more information refer to `ru.noties.storm.op.Insert` javadoc

**UPDATE**

```java
try {
    Storm.newUpdate(mManager).update(list, boolean shouldNotify *true, boolean setPrimaryKey *false); // * - optional
} catch (StormException e) {
    e.printStackTrace();
}
```

For more information refer to `ru.noties.storm.op.Update` javadoc

**DELETE**

```java
try {
    Storm.newDelete(mManager).deleteAll(SomeTable.class, boolean shouldNotify *true); // * - optional
} catch (StormException e) {
    e.printStackTrace();
}
```

For more information refer to `ru.noties.storm.op.Delete` javadoc

**SELECT**

```java
@Nullable final SomeTable table = Storm.newSelect(mManager).query(SomeTable.class, Selection.eq("id", 5));
```

For more information refer to `ru.noties.storm.op.Select` javadoc

***Selection***

Supported selections:

```java
Selection.eq(); // ' = '
Selection.neq(); // '!='
Selection.in();	// ' IN ([])'
Selection.btw(); // ' BETWEEN (_int_ AND _int_)'
Selection.b(); // ' > '
Selection.be(); // ' >= '
Selection.l(); // ' < '
Selection.le(); // ' <= '
```

Selections could be chained:

```java
final Selection root = Selection.empty();
root.grp(Selection.eq("id", 5).or(Selection.neq("column", 13)));
root.and(Selection.b("column2", 1001));
```

## License

```
  Copyright 2015 Dimitry Ivanov (mail@dimitryivanov.ru)

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
```