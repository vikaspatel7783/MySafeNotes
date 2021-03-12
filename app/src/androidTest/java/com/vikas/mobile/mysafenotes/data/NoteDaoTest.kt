package com.vikas.mobile.mysafenotes.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.vikas.mobile.mysafenotes.data.dao.CategoryDao
import com.vikas.mobile.mysafenotes.data.dao.NoteDao
import com.vikas.mobile.mysafenotes.data.entity.Category
import com.vikas.mobile.mysafenotes.data.entity.Note
import com.vikas.mobile.mysafenotes.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteDaoTest {

    private lateinit var database: MySafeNotesDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var noteDao: NoteDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, MySafeNotesDatabase::class.java).build()
        categoryDao = database.categoryDao()
        noteDao = database.noteDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    private fun createCategory(name: String) = Category(name = name)
    private fun createNote(categoryId: Long, noteContent: String) = Note(categoryId = categoryId, noteContent = noteContent)

    @Test
    fun testNoteInsert() {
        val bankContent = "ICICI BANK, account number: 1234, pin: 1234"
        runBlocking {
            val bankCategory = categoryDao.get(categoryDao.insert(createCategory("BANKING")))
            val bankNote = createNote(bankCategory.id, bankContent)
            val bankNoteId = noteDao.insert(bankNote)

            val savedBankNote = noteDao.getNote(bankNoteId).getOrAwaitValue()
            Assert.assertEquals(savedBankNote.categoryId, bankCategory.id)
            Assert.assertEquals(savedBankNote.noteContent, bankContent)
        }
    }

    @Test
    fun testMultipleNoteInsert() {
        val bankContent = "ICICI BANK, account number: 1234, pin: 1234"
        val personalContent = "I should wake up 6:00 am early in morning"
        runBlocking {
            val bankCategory = categoryDao.get(categoryDao.insert(createCategory("BANKING")))
            val bankNote = createNote(bankCategory.id, bankContent)
            val bankNoteId = noteDao.insert(bankNote)

            val personalCategory = categoryDao.get(categoryDao.insert(createCategory("PERSONAL")))
            val personalNote = createNote(personalCategory.id, personalContent)
            val personalNoteId = noteDao.insert(personalNote)

            val savedBankNote = noteDao.getNote(bankNoteId).getOrAwaitValue()
            val savedPersonalNote = noteDao.getNote(personalNoteId).getOrAwaitValue()
            Assert.assertEquals(2, noteDao.getAll().size)
            Assert.assertEquals(savedBankNote.noteContent, bankContent)
            Assert.assertEquals(savedPersonalNote.noteContent, personalContent)
        }
    }

}