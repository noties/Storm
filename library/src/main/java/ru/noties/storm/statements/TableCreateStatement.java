/*
 * Copyright 2015 Dimitry Ivanov (mail@dimitryivanov.ru)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.noties.storm.statements;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ru.noties.storm.Storm;
import ru.noties.storm.FieldPrimaryKey;
import ru.noties.storm.ForeignKeyAction;
import ru.noties.storm.anno.Autoincrement;
import ru.noties.storm.anno.DBNonNull;
import ru.noties.storm.anno.Default;
import ru.noties.storm.anno.ForeignKey;
import ru.noties.storm.anno.Index;
import ru.noties.storm.FieldHolder;
import ru.noties.storm.anno.Unique;
import ru.noties.storm.TableNameParser;
import ru.noties.storm.util.Pair;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 25.01.2015.
 */
public class TableCreateStatement {

    public static final String CREATE_TABLE     = "CREATE TABLE ";
    public static final String INDEX_STATEMENT  = "CREATE INDEX %1$s ON %2$s(%3$s)";

    public static final String AUTOINCREMENT    = "AUTOINCREMENT";
    public static final String PRIMARY_KEY      = "PRIMARY KEY";
    public static final String NON_NULL         = "NOT NULL";
    public static final String DEFAULT          = "DEFAULT";
    public static final String UNIQUE           = "UNIQUE";
    public static final String REFERENCES       = "REFERENCES %1$s(%2$s)";
    public static final String FK_ON_UPDATE     = "ON UPDATE";
    public static final String FK_ON_DELETE     = "ON DELETE";

    private static final Class<Autoincrement>   AUTOINCREMENT_CLASS   = Autoincrement.class;
    private static final Class<DBNonNull>       NOT_NULL_CLASS        = DBNonNull.class;
    private static final Class<Default>         DEFAULT_CLASS         = Default.class;
    private static final Class<Index>           INDEX_CLASS           = Index.class;
    private static final Class<Unique>          UNIQUE_CLASS          = Unique.class;
    private static final Class<ForeignKey>      FOREIGN_KEY_CLASS     = ForeignKey.class;

    protected static final char SINGLE_QUOTE  = '\'';
    protected static final char DELIMITER     = '`';
    protected static final char SPACE         = ' ';
    protected static final char OPEN          = '(';
    protected static final char CLOSE         = ')';
    protected static final char COMMA         = ',';
    protected static final char NEW_LINE      = '\n';
    protected static final char SEMICOLON     = ';';

    private final List<IndexHolder> mIndices;

    private boolean mIsPrimaryKeySet;

    public TableCreateStatement() {
        mIndices = new ArrayList<>();
    }

    public String createField(@NonNull FieldHolder fieldHolder) {

        final Field field = fieldHolder.getField();

        // is autoincrement;
        // is primary key;
        // if both - append;
        // is not null
        // has default

        final boolean isAutoincrement   = isAutoincrement(field);
        final boolean isPrimary         = isPrimaryKey(field);
        final boolean isNotNull         = isNotNull(field);
        final String  isDefault         = isDefault(field);
        final boolean isUnique          = isUnique(field);

        final String foreignKey = getForeignKeyStatement(field);

        final StringBuilder builder = new StringBuilder();

        final int name = wrapTwoDelimiters(builder);
        builder.insert(name, fieldHolder.getName());
        builder.append(SPACE)
            .append(fieldHolder.getType().getSqlRepresentation());

        if (isNotNull) {
            appendWithLeadingSpace(builder, NON_NULL);
        }

        if (isDefault != null) {
            appendWithLeadingSpace(builder, DEFAULT);
            builder.append(SPACE);
            final int defaultWhere = wrapTwoSingleQuotes(builder);
            builder.insert(defaultWhere, isDefault);
        }

        if (isPrimary) {

            if (mIsPrimaryKeySet) {
                if (Storm.isDebug()) {
                    throw new AssertionError("Multiple primary keys are not supported");
                }
            } else {

                appendWithLeadingSpace(builder, PRIMARY_KEY);

                if (isAutoincrement) {
                    appendWithLeadingSpace(builder, AUTOINCREMENT);
                }

                mIsPrimaryKeySet = true;
            }

        }

        if (isUnique) {
            appendWithLeadingSpace(builder, UNIQUE);
        }

        if (foreignKey != null) {
            appendWithLeadingSpace(builder, foreignKey);
        }

        checkIfFieldIsInIndex(field, fieldHolder.getName());

        return builder.toString();
    }

    private int wrapTwoSingleQuotes(StringBuilder builder) {
        return wrapTwoSymbols(builder, SINGLE_QUOTE);
    }

    private int wrapTwoDelimiters(StringBuilder builder) {
        return wrapTwoSymbols(builder, DELIMITER);
    }

    private int wrapTwoSymbols(StringBuilder builder, char symbol) {
        final int length = builder.length();
        builder.append(symbol)
                .append(symbol);
        return length + 1;
    }

    private void appendWithLeadingSpace(StringBuilder builder, String what) {
        builder.append(SPACE)
                .append(what);
    }

    private boolean isAutoincrement(Field field) {
        return field.isAnnotationPresent(AUTOINCREMENT_CLASS);
    }

    private boolean isPrimaryKey(Field field) {
        return FieldPrimaryKey.isPrimaryKey(field);
    }

    private boolean isNotNull(Field field) {
        return field.isAnnotationPresent(NOT_NULL_CLASS);
    }

    private @Nullable String isDefault(Field field) {
        if (!field.isAnnotationPresent(DEFAULT_CLASS)) {
            return null;
        }

        final Default anno = field.getAnnotation(DEFAULT_CLASS);

        return anno.value();
    }

    private boolean isUnique(Field field) {
        return field.isAnnotationPresent(UNIQUE_CLASS);
    }

    private @Nullable String getForeignKeyStatement(Field field) {
        if (!field.isAnnotationPresent(FOREIGN_KEY_CLASS)) {
            return null;
        }

        final ForeignKey foreignKey = field.getAnnotation(FOREIGN_KEY_CLASS);

        // NB there is no checking whether parentTable
        // in it's bounds (the same db file) and/or already created
        final Class<?>  parentTable   = foreignKey.parentTable();
        final String    parentColumn  = foreignKey.parentColumnName();

        final String parentTableName = TableNameParser.parse(parentTable);
        if (parentTableName == null) {
            return null;
        }

        final String mainFKStatement = String.format(REFERENCES, parentTableName, parentColumn);

        final ForeignKeyAction onUpdate = foreignKey.onUpdate();
        final ForeignKeyAction onDelete = foreignKey.onDelete();

        final boolean hasOnUpdate = onUpdate != ForeignKeyAction.NO_ACTION;
        final boolean hasOnDelete = onDelete != ForeignKeyAction.NO_ACTION;

        if (!hasOnDelete
                && !hasOnUpdate) {
            return mainFKStatement;
        }

        final StringBuilder builder = new StringBuilder(mainFKStatement);
        if (hasOnUpdate) {
            appendWithLeadingSpace(builder, FK_ON_UPDATE);
            appendWithLeadingSpace(builder, onUpdate.getSqlRepresentation());
        }

        if (hasOnDelete) {
            appendWithLeadingSpace(builder, FK_ON_DELETE);
            appendWithLeadingSpace(builder, onDelete.getSqlRepresentation());
        }

        return builder.toString();
    }

    public List<String> getStatements(@NonNull String tableName, @NonNull List<FieldHolder> holders) {

        final String mainStatement = createMainStatement(tableName, holders);
        final List<String> indices = createIndicesStatements(tableName);

        final List<String> statements = new ArrayList<>();
        statements.add(mainStatement);

        if (indices != null) {
            statements.addAll(indices);
        }

        return statements;
    }

    public String createMainStatement(@NonNull String tableName, @NonNull List<FieldHolder> holders) {

        final List<String> fields = new ArrayList<>();
        for (FieldHolder holder: holders) {
            fields.add(createField(holder));
        }

        final StringBuilder builder = new StringBuilder(CREATE_TABLE);
        final int tableNameWhere =  wrapTwoDelimiters(builder);
        builder.insert(tableNameWhere, tableName);
        builder.append(OPEN)
                .append(NEW_LINE);

        for (int i = 0, size = fields.size(), max = size - 1; i < size; i++) {
            builder.append(fields.get(i));
            if (max != i) {
                builder.append(COMMA)
                        .append(NEW_LINE);
            }
        }

        builder.append(NEW_LINE)
                .append(CLOSE)
                .append(SEMICOLON);

        return builder.toString();
    }

    public @Nullable List<String> createIndicesStatements(@NonNull String tableName) {

        if (mIndices.size() == 0) {
            return null;
        }

        final List<String> statements = new ArrayList<>();

        StringBuilder builder;
        Pair.SimplePair<String> pair;

        for (IndexHolder indexHolder: mIndices) {

            builder = new StringBuilder();

            for (int i = 0, size = indexHolder.columnAndSort.size(), max = size - 1; i < size; i++) {
                pair = indexHolder.columnAndSort.get(i);
                builder.append(pair.getFirst())
                        .append(SPACE)
                        .append(pair.getSecond());
                if (max != i) {
                    builder.append(COMMA)
                            .append(SPACE);
                }
            }

            statements.add(
                    String.format(
                            INDEX_STATEMENT,
                            indexHolder.indexName,
                            tableName,
                            builder.toString()
                    )
            );
        }

        if (statements.size() == 0) {
            return null;
        }

        return statements;
    }

    private void checkIfFieldIsInIndex(Field field, String columnName) {

        if (!field.isAnnotationPresent(INDEX_CLASS)) {
            return;
        }

        final Index index = field.getAnnotation(INDEX_CLASS);

        final String indexName = index.value();
        final String sorting   = index.sorting().getValue();

        final IndexHolder newHolder = new IndexHolder(indexName);
        final int indexIndex = mIndices.indexOf(newHolder);

        final IndexHolder holder;

        if (indexIndex >= 0) {
            holder = mIndices.get(indexIndex);
        } else {
            holder = newHolder;
            mIndices.add(holder);
        }

        holder.add(columnName, sorting);
    }

    private static class IndexHolder {

        final String indexName;
        List<Pair.SimplePair<String>> columnAndSort;

        IndexHolder(@NonNull String indexName) {
            this.indexName = indexName;
        }

        public void add(String columnName, String sorting) {
            if (columnAndSort == null) {
                columnAndSort = new ArrayList<>();
            }
            columnAndSort.add(new Pair.SimplePair<>(columnName, sorting));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            IndexHolder that = (IndexHolder) o;

            if (!indexName.equals(that.indexName))
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            return indexName.hashCode();
        }
    }

}
