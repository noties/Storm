package ru.noties.storm.sample.tableExample;

import ru.noties.storm.ForeignKeyAction;
import ru.noties.storm.anno.Column;
import ru.noties.storm.anno.Default;
import ru.noties.storm.anno.ForeignKey;
import ru.noties.storm.anno.NewColumn;
import ru.noties.storm.anno.NewTable;
import ru.noties.storm.anno.PrimaryKey;
import ru.noties.storm.anno.Table;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 15.02.2015.
 */

/**
 * Migration is very easy - annotate with @NewTable(db_version)
 * to create new table
 */
@NewTable(2)
@Table("other_table_model")
public class OtherTableModel {

    /**
     * To init a foreign key (aka REFERENCES) annotate with @ForeignKey.
     * It takes parent's class, referenced column name and (optional)
     * action onDelete or/and onUpdate
     *
     * Don't forget to enable ForeignKey support by passing Pragma to DatabaseManager
     */
    @PrimaryKey
    @ForeignKey(
            parentTable = TableModel.class,
            parentColumnName = TableModel.COL_ID,
            onDelete = ForeignKeyAction.CASCADE,
            onUpdate = ForeignKeyAction.NO_ACTION
    )
    private long id;

    @Column
    private byte[] someBytes;

    /**
     * To alter existing table and add new column annotate with @NewColumn(db_version)
     * Note: consider adding @Default to all new columns
     */
    @NewColumn(3)
    @Default("-1")
    @Column
    private int againInt;
}
