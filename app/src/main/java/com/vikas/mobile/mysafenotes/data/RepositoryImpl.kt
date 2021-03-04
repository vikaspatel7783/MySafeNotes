package com.vikas.mobile.mysafenotes.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RepositoryImpl(private val mySafeNotesDatabase: MySafeNotesDatabase) : Repository {

    fun myTest() {
        CoroutineScope(Dispatchers.IO).launch {

        }
    }

    override fun getAllCategories() = mySafeNotesDatabase.categoryDao().getAll()
}