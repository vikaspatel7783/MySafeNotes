package com.vikas.mobile.mysafenotes.data

import androidx.lifecycle.LiveData
import com.vikas.mobile.mysafenotes.data.entity.Category

interface Repository {

    suspend fun addCategory(category: Category): Long
    fun getAllCategories(): LiveData<List<Category>>
}