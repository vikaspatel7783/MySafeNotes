package com.vikas.mobile.mysafenotes.data

import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val mySafeNotesDatabase: MySafeNotesDatabase): Repository {

    override fun getAllCategories() = mySafeNotesDatabase.categoryDao().getAll()


}