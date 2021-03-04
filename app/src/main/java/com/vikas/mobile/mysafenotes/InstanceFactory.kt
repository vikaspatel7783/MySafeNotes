package com.vikas.mobile.mysafenotes

import com.vikas.mobile.mysafenotes.data.MySafeNotesDatabase
import com.vikas.mobile.mysafenotes.data.Repository
import com.vikas.mobile.mysafenotes.data.RepositoryImpl

class InstanceFactory {

    companion object {
        private lateinit var mySafeNotesDatabase: MySafeNotesDatabase
        lateinit var repository: Repository

        fun instantiate(instance: MySafeNotesDatabase) {
            mySafeNotesDatabase = instance
            repository = RepositoryImpl(mySafeNotesDatabase)
        }
    }
}