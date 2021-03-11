package com.vikas.mobile.mysafenotes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vikas.mobile.mysafenotes.data.entity.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_table")
    suspend fun getAll(): List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note): Long

    @Query("SELECT * FROM note_table WHERE category_id = :categoryId")
    fun getByCategory(categoryId: Long): LiveData<List<Note>>

    @Query("SELECT * FROM note_table WHERE id = :noteId")
    suspend fun getNote(noteId: Long): Note

    //@Query("DELETE FROM note_table WHERE id = :bankNoteId")
    //suspend fun delete(bankNoteId: Long)

    //@Delete
    //suspend fun delete(note: Note)
}