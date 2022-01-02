package com.vikas.mobile.mynotes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vikas.mobile.mynotes.data.entity.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_table")
    suspend fun getAll(): List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note): Long

    @Query("SELECT * FROM note_table WHERE category_id = :categoryId ORDER BY content ASC")
    fun getByCategory(categoryId: Long): LiveData<List<Note>>

    @Query("SELECT * FROM note_table WHERE id = :noteId")
    fun getNote(noteId: Long): LiveData<Note>

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM note_table WHERE category_id = :categoryId")
    suspend fun deleteNotes(categoryId: Long)

//    @Query("SELECT * FROM note_table WHERE content LIKE :searchText")
//    fun searchNotes(searchText: String): LiveData<List<Note>>

}