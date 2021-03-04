package com.vikas.mobile.mysafenotes.data

import androidx.lifecycle.LiveData
import com.vikas.mobile.mysafenotes.data.entity.Category

interface Repository {

    fun getAllCategories(): LiveData<List<Category>>
}