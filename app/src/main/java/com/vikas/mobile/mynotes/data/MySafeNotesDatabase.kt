package com.vikas.mobile.mynotes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.vikas.mobile.mynotes.data.dao.CategoryDao
import com.vikas.mobile.mynotes.data.dao.NoteDao
import com.vikas.mobile.mynotes.data.entity.Category
import com.vikas.mobile.mynotes.data.entity.Note
import com.vikas.mobile.mynotes.worker.DefaultNotesSeedWorker

@Database(entities = [Category::class, Note::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
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
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
//                            CoroutineScope(Dispatchers.IO).launch {}
//                            val request = OneTimeWorkRequestBuilder<DefaultNotesSeedWorker>().build()
//                            WorkManager.getInstance(context).enqueue(request)
                        }
                    }
                )
                //.allowMainThreadQueries()
                .build()
        }

    }
}