/*
 *
 *  * Copyright 2025 Google LLC. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.example.notez.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.notez.core.ui.Converters
import com.example.notez.developer.brushdesigner.data.CustomBrushDao
import com.example.notez.developer.brushdesigner.data.CustomBrushEntity

/**
 * Main Room database containing notes and custom drawing brush definitions.
 */
@Database(
    entities = [Note::class, CustomBrushEntity::class],
    version = 10,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NoteDatabase : RoomDatabase() {
    /**
     * Provides DAO operations for notes.
     */
    abstract fun noteDao(): NoteDao

    /**
     * Provides DAO operations for custom drawing brushes.
     */
    abstract fun customBrushDao(): CustomBrushDao

    companion object {
        const val DATABASE_NAME = "note_database"
    }
}
