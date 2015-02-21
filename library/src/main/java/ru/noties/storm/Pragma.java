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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 29.01.2015.
 */
public class Pragma {

    public static enum Synchronous {

        OFF(0), NORMAL(1), FULL(2), SKIP(-1);

        private static final String NAME = "synchronous";

        private final int value;

        private Synchronous(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return NAME;
        }
    }

    public static enum ForeignKeys {

        ON(1), OFF(0), SKIP(-1);

        private static final String NAME = "foreign_keys";

        private final int value;

        private ForeignKeys(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return NAME;
        }
    }

    public static enum JournalMode {

        DELETE      ("DELETE"),
        TRUNCATE    ("TRUNCATE"),
        PERSIST     ("PERSIST"),
        MEMORY      ("MEMORY"),
        WAL         ("WAL"),
        OFF         ("OFF"),
        SKIP        (null);

        private static final String NAME = "journal_mode";

        private final String value;

        private JournalMode(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String getName() {
            return NAME;
        }
    }

    public static enum TempStore {

        DEFAULT (0),
        FILE    (1),
        MEMORY  (2),
        SKIP    (-1);

        private static final String NAME = "temp_store";

        private int mValue;

        private TempStore(int value) {
            this.mValue = value;
        }

        public int getValue() {
            return mValue;
        }

        public String getName() {
            return NAME;
        }
    }

    private final Synchronous  synchronous;
    private final ForeignKeys  foreignKeys;
    private final JournalMode  journalMode;
    private final TempStore    tempStore;
    private final List<String> customPragmas;

    private Pragma(Builder builder) {
        this.synchronous   = builder.synchronous;
        this.foreignKeys   = builder.foreignKeys;
        this.journalMode   = builder.journalMode;
        this.tempStore     = builder.tempStore;
        this.customPragmas = builder.customPragmas;
    }

    public Synchronous getSynchronous() {
        return synchronous;
    }

    public ForeignKeys getForeignKeys() {
        return foreignKeys;
    }

    public JournalMode getJournalMode() {
        return journalMode;
    }

    public TempStore getTempStore() {
        return tempStore;
    }

    public List<String> getCustomPragmas() {
        return customPragmas;
    }

    public static class Builder {

        private Synchronous  synchronous;
        private ForeignKeys  foreignKeys;
        private JournalMode  journalMode;
        private TempStore    tempStore;
        private List<String> customPragmas;

        public Builder setSynchronous(Synchronous synchronous) {
            this.synchronous = synchronous;
            return this;
        }

        public Builder setForeignKeys(ForeignKeys foreignKeys) {
            this.foreignKeys = foreignKeys;
            return this;
        }

        public Builder setJournalMode(JournalMode journalMode) {
            this.journalMode = journalMode;
            return this;
        }

        public Builder setTempStore(TempStore tempStore) {
            this.tempStore = tempStore;
            return this;
        }

        public Builder setCustomPragmas(List<String> customPragmas) {

            if (customPragmas != null) {
                this.customPragmas = new ArrayList<>(customPragmas);
            }

            return this;
        }

        public Pragma build() {
            synchronous = synchronous == null ? Synchronous.SKIP : synchronous;
            foreignKeys = foreignKeys == null ? ForeignKeys.SKIP : foreignKeys;
            journalMode = journalMode == null ? JournalMode.SKIP : journalMode;
            tempStore   = tempStore   == null ? TempStore.SKIP   : tempStore;

            customPragmas = (customPragmas == null || customPragmas.size() == 0)
                    ? null
                    : customPragmas;

            return new Pragma(this);
        }
    }
}
