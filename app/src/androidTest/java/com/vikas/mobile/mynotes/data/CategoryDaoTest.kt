package com.vikas.mobile.mynotes.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.vikas.mobile.mynotes.data.dao.CategoryDao
import com.vikas.mobile.mynotes.data.entity.Category
import com.vikas.mobile.mynotes.data.entity.MaskedData
import com.vikas.mobile.mynotes.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoryDaoTest {

    private lateinit var database: MySafeNotesDatabase
    private lateinit var categoryDao: CategoryDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, MySafeNotesDatabase::class.java).build()
        categoryDao = database.categoryDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    private fun createCategory(name: String) = Category(name = MaskedData(name))

    @Test
    fun testCategoryInsert() {
        runBlocking {
            createCategory("BANKING")
            .let {
                val insertedId = categoryDao.insert(it)
                val category = categoryDao.get(insertedId)
                Assert.assertEquals("BANKING", category.name.content)
            }
        }
    }

    @Test
    fun testMultipleCategoriesInsert() {
        val bankingCategory = createCategory("BANKING")
        val personalCategory = createCategory("PERSONAL")
        val friendsCategory = createCategory("FRIENDS")

        runBlocking {
            categoryDao.insertAll(listOf(bankingCategory, personalCategory, friendsCategory))
            Assert.assertEquals(3, categoryDao.getAll().getOrAwaitValue().size)
        }
    }

    @Test
    fun testCategoryDelete() {
        runBlocking {
            createCategory("BANKING")
                .let {
                    val insertedId = categoryDao.insert(it)
                    categoryDao.delete(insertedId)
                    val bankingCategory = categoryDao.get(insertedId)
                    Assert.assertNull(bankingCategory)
                }
        }
    }
}