package com.vikas.mobile.mysafenotes.data

import androidx.lifecycle.LiveData
import com.vikas.mobile.mysafenotes.data.entity.Category
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val mySafeNotesDatabase: MySafeNotesDatabase): Repository {

    override suspend fun addCategory(category: Category) = mySafeNotesDatabase.categoryDao().insert(category)

    override fun getAllCategories() = mySafeNotesDatabase.categoryDao().getAll()

}