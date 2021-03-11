package com.vikas.mobile.mysafenotes.ui

import androidx.lifecycle.*
import com.vikas.mobile.mysafenotes.data.Repository
import com.vikas.mobile.mysafenotes.data.entity.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var repository: Repository

    fun addCategory(category: Category) {
        viewModelScope.launch {
            repository.addCategory(category)
        }
    }


}

//class DashboardViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return modelClass.getConstructor(Repository::class.java)
//                .newInstance(repository)
//    }

//}