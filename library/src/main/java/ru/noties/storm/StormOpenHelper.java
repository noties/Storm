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

package ru.noties.storm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.List;

import ru.noties.storm.statements.PragmaStatements;
import ru.noties.storm.statements.TableCreator;
import ru.noties.storm.statements.TableUpdater;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) 01.02.15.
 */
public class StormOpenHelper extends SQLiteOpenHelper {

    private final List<CachedTable> mTables;
    private final Pragma mPragma;
    private final SQLiteOpenCallbacks mOpenCallbacks;

    public StormOpenHelper(
            Context context,
            String name,
            int version,
            List<CachedTable> tables,
            @Nullable Pragma pragma,
            @Nullable SQLiteOpenCallbacks openCallbacks
    ) {
        super(context, name, null, version);
        this.mTables = tables;
        this.mPragma = pragma;
        this.mOpenCallbacks = openCallbacks;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final TableCreator creator = new TableCreator(mTables);

        for (String sql: creator) {
            db.execSQL(sql);
        }

        if (mOpenCallbacks != null) {
            mOpenCallbacks.onCreate(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        final TableUpdater updater = new TableUpdater(mTables, oldVersion, newVersion);

        for (String sql: updater) {
            db.execSQL(sql);
        }

        if (mOpenCallbacks != null) {
            mOpenCallbacks.onUpgrade(db, oldVersion, newVersion);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);

        // check for pragmas
        final PragmaStatements strings = new PragmaStatements(mPragma);

        Cursor cursor;

        for (String sql: strings) {
            cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                cursor.close();
            }
        }

        if (mOpenCallbacks != null) {
            mOpenCallbacks.onOpen(db);
        }
    }
}
