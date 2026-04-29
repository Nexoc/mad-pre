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

import  androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Room access object for all note persistence operations.
 */
@Dao
interface NoteDao {
    /**
     * Returns all notes as a Flow so Compose screens can react to database updates.
     */
    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<Note>>

    /**
     * Returns one note as a Flow for editor/detail screens that need live updates.
     */
    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNote(id: Long): Flow<Note?>

    /**
     * Inserts a note and returns the generated primary key.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(note: Note): Long

    /**
     * Persists changed fields of an existing note.
     */
    @Update
    suspend fun updateNote(note: Note)

    /**
     * Deletes the note row from the database.
     */
    @Delete
    suspend fun deleteNote(note: Note)

    /**
     * Loads a note once for repository operations that must read before writing.
     */
    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Long): Note?
}
