package com.vikas.mobile.mysafenotes.data

import com.vikas.mobile.mysafenotes.data.entity.Category
import com.vikas.mobile.mysafenotes.data.entity.Note
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val mySafeNotesDatabase: MySafeNotesDatabase): Repository {

    override suspend fun addCategory(category: Category) = mySafeNotesDatabase.categoryDao().insert(category)

    override fun getAllCategories() = mySafeNotesDatabase.categoryDao().getAll()

    override suspend fun addNote(note: Note): Long = mySafeNotesDatabase.noteDao().insert(note)

    override fun getNotes(categoryId: Long) = mySafeNotesDatabase.noteDao().getByCategory(categoryId)

}