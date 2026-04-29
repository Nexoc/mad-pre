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

import android.content.Context
import androidx.ink.strokes.Stroke
import com.example.notez.core.ui.Converters
import com.example.notez.features.drawing.CustomBrushes
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

/**
 * Repository implementation that keeps the UI and ViewModels independent from Room details.
 */
class OfflineNotesRepository(
    private val notesDao: NoteDao,
    private val context: Context
) : NotesRepository {

    private val converters = Converters()

    /**
     * Exposes the complete note list as a Flow so the Home screen updates after database changes.
     */
    override fun getAllNotesStream(): Flow<List<Note>> = notesDao.getAllNotes()

    /**
     * Exposes a single note as a Flow so editor and detail screens react to updates.
     */
    override fun getNoteStream(id: Long): Flow<Note?> = notesDao.getNote(id)

    /**
     * Inserts a note and assigns creation/update timestamps when the caller did not provide them.
     */
    override suspend fun addNote(note: Note): Long {
        val now = System.currentTimeMillis()
        val createdAtMillis = note.createdAtMillis.takeIf { it > 0L } ?: now
        return notesDao.addNote(
            note.copy(
                createdAtMillis = createdAtMillis,
                updatedAtMillis = note.updatedAtMillis.takeIf { it > 0L } ?: createdAtMillis
            )
        )
    }

    /**
     * Updates a note while preserving the original creation time and refreshing the modified time.
     */
    override suspend fun updateNote(note: Note) {
        val existingNote = notesDao.getNoteById(note.id)
        val createdAtMillis = existingNote?.createdAtMillis?.takeIf { it > 0L }
            ?: note.createdAtMillis.takeIf { it > 0L }
            ?: System.currentTimeMillis()

        notesDao.updateNote(
            note.copy(
                createdAtMillis = createdAtMillis,
                updatedAtMillis = System.currentTimeMillis()
            )
        )
    }

    /**
     * Removes the note from Room, which also causes the Home list Flow to emit a new list.
     */
    override suspend fun deleteNote(note: Note) = notesDao.deleteNote(note)

    /**
     * Serializes drawing strokes into JSON and stores them on the corresponding drawing note.
     */
    override suspend fun updateNoteStrokes(
        noteId: Long,
        strokes: List<Stroke>,
        clientBrushFamilyId: String?
    ) {
        val customBrushes = CustomBrushes.getBrushes(context)
        val strokesData = strokes.map { converters.serializeStroke(it, customBrushes) }
        val strokesJson = Json.encodeToString(strokesData)

        val note = notesDao.getNoteById(noteId)
        if (note != null) {
            val updatedNote =
                note.copy(
                    strokesData = strokesJson,
                    clientBrushFamilyId = clientBrushFamilyId,
                    updatedAtMillis = System.currentTimeMillis()
                )
            notesDao.updateNote(updatedNote)
        }
    }

    /**
     * Restores persisted drawing strokes from JSON for detail thumbnails and drawing editors.
     */
    override suspend fun getNoteStrokes(noteId: Long): List<Stroke> {
        val note = notesDao.getNoteById(noteId)
        val strokesJson = note?.strokesData ?: return emptyList()
        val customBrushes = CustomBrushes.getBrushes(context)

        val strokesData = Json.decodeFromString<List<String>>(strokesJson)
        return strokesData.mapNotNull { converters.deserializeStrokeFromString(it, customBrushes) }
    }

    /**
     * Flips the favorite flag so the Home screen can move the note between sections.
     */
    override suspend fun toggleFavorite(noteId: Long) {
        val note = notesDao.getNoteById(noteId)
        if (note != null) {
            val updatedNote = note.copy(
                isFavorite = !note.isFavorite,
                updatedAtMillis = if (note.updatedAtMillis > 0L) {
                    System.currentTimeMillis()
                } else {
                    note.updatedAtMillis
                }
            )
            notesDao.updateNote(updatedNote)
        }
    }

    /**
     * Persists image URIs attached to a note and refreshes the modified timestamp.
     */
    override suspend fun updateNoteImageUriList(noteId: Long, imageUriList: List<String>?) {
        val note = notesDao.getNoteById(noteId)
        if (note != null) {
            val updatedNote = note.copy(
                imageUriList = imageUriList,
                updatedAtMillis = System.currentTimeMillis()
            )
            notesDao.updateNote(updatedNote)
        }
    }

}
