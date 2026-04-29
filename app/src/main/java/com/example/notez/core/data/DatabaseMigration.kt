/*
 *
 *  *
 *  *  * Copyright 2025 Google LLC. All rights reserved.
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
 *
 */

package com.example.notez.core.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Normalizes older enum values so Room can keep reading existing drawing notes.
 */
val MIGRATION_7_8 = object : Migration(7, 8) {
    /**
     * Performs the schema/data change required between database versions 7 and 8.
     */
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("UPDATE notes SET type = 'Drawing' WHERE type = 'DRAWING'")
    }
}

/**
 * Adds the custom brushes table used by the drawing tools.
 */
val MIGRATION_8_9 = object : Migration(8, 9) {
    /**
     * Creates the custom brush storage table without removing existing notes.
     */
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `custom_brushes` (`name` TEXT NOT NULL, `brushBytes` BLOB NOT NULL, PRIMARY KEY(`name`))"
        )
    }
}

/**
 * Adds creation and modification timestamps to existing note rows.
 */
val MIGRATION_9_10 = object : Migration(9, 10) {
    /**
     * Adds timestamp columns and gives old notes a valid timestamp instead of leaving them empty.
     */
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE `notes` ADD COLUMN `created_at_millis` INTEGER NOT NULL DEFAULT 0"
        )
        db.execSQL(
            "ALTER TABLE `notes` ADD COLUMN `updated_at_millis` INTEGER NOT NULL DEFAULT 0"
        )
        db.execSQL(
            "UPDATE `notes` SET `created_at_millis` = strftime('%s','now') * 1000, " +
                "`updated_at_millis` = strftime('%s','now') * 1000 " +
                "WHERE `created_at_millis` = 0 OR `updated_at_millis` = 0"
        )
    }
}
