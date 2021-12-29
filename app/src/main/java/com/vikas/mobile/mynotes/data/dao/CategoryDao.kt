package com.vikas.mobile.mynotes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vikas.mobile.mynotes.data.entity.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category_table /* ORDER BY name ASC */")
    fun getAll(): LiveData<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<Category>)

    @Query("SELECT * FROM category_table WHERE id = :id")
    suspend fun get(id: Long): Category

    @Query("DELETE FROM category_table WHERE id = :categoryId")
    suspend fun delete(categoryId: Long)
}