package com.vikas.mobile.mysafenotes.data

import androidx.lifecycle.LiveData
import com.vikas.mobile.mysafenotes.data.entity.Category
import com.vikas.mobile.mysafenotes.data.entity.Note

interface Repository {

    suspend fun addCategory(category: Category): Long

    fun getAllCategories(): LiveData<List<Category>>

    suspend fun addUpdateNote(note: Note): Long

    fun getNotes(categoryId: Long): LiveData<List<Note>>

    fun getNote(noteId: Long): LiveData<Note>

    suspend fun deleteNote(note: Note)

    suspend fun deleteCategoryAndNotes(categoryId: Long)
}