package com.vikas.mobile.mynotes.data

import androidx.lifecycle.LiveData
import com.vikas.mobile.mynotes.data.entity.Category
import com.vikas.mobile.mynotes.data.entity.Note

interface Repository {

    suspend fun addCategory(category: String): Long

    suspend fun getCategory(categoryId: Long): Category

    fun getAllCategories(): LiveData<List<Category>>

    suspend fun addUpdateNote(note: Note): Long

    fun getNotes(categoryId: Long): LiveData<List<Note>>

    fun getNote(noteId: Long): LiveData<Note>

    suspend fun deleteNote(note: Note)

    suspend fun deleteCategoryAndNotes(categoryId: Long)

    suspend fun searchNotes(searchText: String, onResult: (List<Note>) -> Unit)
}