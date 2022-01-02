package com.vikas.mobile.mynotes.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vikas.mobile.mynotes.data.entity.Setting

@Dao
interface SettingsDao {

    @Query("SELECT * FROM settings_table /* ORDER BY name ASC */")
    fun getAll(): List<Setting>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(setting: Setting): Long

    @Query("SELECT * FROM settings_table WHERE name = :name")
    suspend fun get(name: String): Setting

    @Query("DELETE FROM settings_table WHERE name = :name")
    suspend fun delete(name: String)
}