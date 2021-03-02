package com.vikas.mobile.mysafenotes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vikas.mobile.mysafenotes.data.dao.CategoryDao
import com.vikas.mobile.mysafenotes.data.dao.NoteDao
import com.vikas.mobile.mysafenotes.data.entity.Category
import com.vikas.mobile.mysafenotes.data.entity.Note

@Database(entities = [Category::class, Note::class], version = 1, exportSchema = false)
abstract class MySafeNotesDatabase: RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun noteDao(): NoteDao

    companion object {

        private const val DATABASE_NAME = "MySafeNotesDb"

        // For Singleton instantiation
        @Volatile private var instance: MySafeNotesDatabase? = null

        fun getInstance(context: Context): MySafeNotesDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): MySafeNotesDatabase {
            return Room.databaseBuilder(context, MySafeNotesDatabase::class.java, DATABASE_NAME)
                //.addCallback()
                //.allowMainThreadQueries()
                .build()
        }

    }
}