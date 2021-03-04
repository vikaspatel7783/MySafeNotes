package com.vikas.mobile.mysafenotes.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vikas.mobile.mysafenotes.data.Repository
import com.vikas.mobile.mysafenotes.data.entity.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    private lateinit var repository : Repository

    fun setRepository(repository: Repository) {
        this.repository = repository
    }

    fun getAllCategories(): LiveData<List<Category>> {
        return repository.getAllCategories()
    }

//    val allCategories = MediatorLiveData<List<Category>>().apply {
//        viewModelScope.launch(Dispatchers.IO) {
//            postValue(repository.getAllCategories())
//        }
//    }

}