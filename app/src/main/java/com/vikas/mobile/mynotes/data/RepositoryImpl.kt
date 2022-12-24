package com.vikas.mobile.mynotes.data

import androidx.lifecycle.LiveData
import com.vikas.mobile.mynotes.data.entity.*
import java.util.*
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val mySafeNotesDatabase: MySafeNotesDatabase): Repository {

    override suspend fun addCategory(category: String) =
            mySafeNotesDatabase.categoryDao().insert(Category(MaskedData(category)))

    override suspend fun getCategory(categoryId: Long): Category = mySafeNotesDatabase.categoryDao().get(categoryId)

    override fun getAllCategories() = mySafeNotesDatabase.categoryDao().getAll()

    override suspend fun addUpdateNote(note: Note): Long = mySafeNotesDatabase.noteDao().insert(note)

    override suspend fun addNotes(notes: List<Note>) = mySafeNotesDatabase.noteDao().insertAll(notes)

    override fun getNotes(categoryId: Long) = mySafeNotesDatabase.noteDao().getByCategory(categoryId)

    override fun getNote(noteId: Long): LiveData<Note> = mySafeNotesDatabase.noteDao().getNote(noteId)

    override suspend fun deleteNote(note: Note) = mySafeNotesDatabase.noteDao().deleteNote(note)

    override suspend fun deleteCategoryAndNotes(categoryId: Long) {
        mySafeNotesDatabase.noteDao().deleteNotes(categoryId)
        mySafeNotesDatabase.categoryDao().delete(categoryId)
    }

    override suspend fun deleteAll() {
        mySafeNotesDatabase.categoryDao().getAllCategoriesSynchronous().forEach { category ->
            mySafeNotesDatabase.noteDao().deleteNotes(category.id)
            mySafeNotesDatabase.categoryDao().delete(category.id)
        }
    }

    override suspend fun searchNotes(searchText: String, onResult: (List<Note>) -> Unit) {
        val filteredNotes = mutableListOf<Note>()
        val allNotes = mySafeNotesDatabase.noteDao().getAll()
        val trimmedSearchText = searchText.trim().toLowerCase(Locale.ROOT)
        for (note in allNotes) {
            if (note.noteContent.content.toLowerCase(Locale.ROOT).contains(trimmedSearchText)) {
                filteredNotes.add(note)
            }
        }
        onResult(filteredNotes)
    }

    override suspend fun getAllNotes(): NoteExport {
        val noteExport = NoteExport()
        mySafeNotesDatabase.categoryDao().getAllCategoriesSynchronous().let { categories ->
            categories.forEach { category ->
                mySafeNotesDatabase.noteDao().getByCategorySynchronous(category.id).let { notes ->
                    val notesList = mutableListOf<String>()
                    notes.forEach { note ->
                        notesList.add(note.noteContent.content)
                    }
                    noteExport.categoryNotesMap[category.name.content] = notesList
                }
            }
        }
        return noteExport
    }

    override suspend fun getSetting(name: String): Setting {
        return mySafeNotesDatabase.settingsDao().get(name)
    }

    override suspend fun updateSettings(setting: Setting) {
        mySafeNotesDatabase.settingsDao().insert(setting)
    }

}