package com.vikas.mobile.mynotes.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// ----------- settings_table ----------------------
// id (autogenerated) | name               | value
//----------------------------------------------
//        1           | user authentication | 1 or 0
//        2           | some text           | some value

@Entity(tableName = "settings_table", indices = [Index(value = ["name"], unique = true)])
data class Setting (
        @PrimaryKey
    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "value")
    var value: String
) {

    companion object {
        const val AUTH_SETTINGS_NAME = "auth settings"
        const val AUTH_ON = "1"
        const val AUTH_OFF = "0"

        fun interpretAuthSettingValue(value: String): Boolean {
            return when(value.trim()) {
                "1"-> true
                "0"-> false
                else -> throw IllegalArgumentException("Unsupported value received: $value")
            }
        }
    }
}