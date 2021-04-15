package com.vikas.mobile.mysafenotes.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// ----------- category_table --------------
// id (autogenerated) | name
//-----------------------------------
//        1           | BANKING
//        2           | PERSONAL
//        3           | FRIENDS

@Entity(tableName = "category_table", indices = [Index(value = ["name"], unique = true)])
data class Category (
    @ColumnInfo(name = "name")
    var name: MaskedData
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}