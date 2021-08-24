package com.example.appwithroomdb.room

import androidx.room.*

@Dao
interface NoteDao {
    @Insert
    suspend fun addNotes(note: Note)

    @Update
    suspend fun updateNotes(note: Note)

    @Delete
    suspend fun deleteNotes(note: Note)

    @Query("SELECT * FROM note")
    suspend fun getNotes() : List<Note>

    @Query("SELECT * FROM note WHERE id =:note_id")
    suspend fun getNote(note_id : Int) : List<Note>
}