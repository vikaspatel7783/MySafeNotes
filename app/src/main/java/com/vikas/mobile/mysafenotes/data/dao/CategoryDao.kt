package com.vikas.mobile.mysafenotes.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vikas.mobile.mysafenotes.data.entity.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category_table ORDER BY name ASC")
    suspend fun getAll(): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<Category>)

    @Query("SELECT * FROM category_table WHERE id = :id")
    suspend fun get(id: Long): Category

    @Query("SELECT EXISTS(SELECT 1 FROM category_table WHERE name = :categoryName LIMIT 1)")
    suspend fun isExist(categoryName: String): Boolean
}