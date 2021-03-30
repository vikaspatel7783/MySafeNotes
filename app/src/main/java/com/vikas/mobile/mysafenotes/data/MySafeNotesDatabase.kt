package com.vikas.mobile.mysafenotes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vikas.mobile.mysafenotes.data.dao.CategoryDao
import com.vikas.mobile.mysafenotes.data.dao.NoteDao
import com.vikas.mobile.mysafenotes.data.entity.Category
import com.vikas.mobile.mysafenotes.data.entity.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
//                            val db1 = MySafeNotesDatabase.getInstance(context)
//                            CoroutineScope(Dispatchers.IO).launch {
//                                db1.categoryDao().insert(Category("BANKING"))
//                                db1.categoryDao().insert(Category("PERSONAL"))
//                                db1.categoryDao().insert(Category("FRIENDS"))
//                            }
                        }
                    }
                )
                //.allowMainThreadQueries()
                .build()
        }

    }
}