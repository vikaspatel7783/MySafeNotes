
package com.vikas.mobile.mysafenotes.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vikas.mobile.mysafenotes.data.MySafeNotesDatabase
import com.vikas.mobile.mysafenotes.data.entity.Category
import com.vikas.mobile.mysafenotes.data.entity.MaskedData
import com.vikas.mobile.mysafenotes.data.entity.Note
import kotlinx.coroutines.coroutineScope

class DefaultNotesSeedWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {

        val safenoteDb = MySafeNotesDatabase.getInstance(applicationContext)

        getPrePopulatedNotes().forEach {

            val bankingCategoryId = safenoteDb.categoryDao().insert(createCategory(it.key))
            it.value.forEach { note ->
                val bankingNote = createNote(
                        categoryId = bankingCategoryId,
                        noteContent = note
                )
                safenoteDb.noteDao().insert(bankingNote)
            }
        }

        Result.success()
    }

    private fun createCategory(name: String) = Category(name = MaskedData(name))
    private fun createNote(categoryId: Long, noteContent: String) = Note(categoryId = categoryId, noteContent = MaskedData(noteContent))

    private fun getPrePopulatedNotes(): Map<String, Set<String>> {

        val prePopulatedNotes = mutableMapOf<String, Set<String>>()

        return prePopulatedNotes
    }
}
