package com.vikas.mobile.mysafenotes.ui.dashboard

import androidx.lifecycle.*
import com.vikas.mobile.mysafenotes.data.Repository
import com.vikas.mobile.mysafenotes.data.RepositoryImpl
import com.vikas.mobile.mysafenotes.data.entity.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var repository: Repository

    fun getAllCategories(): LiveData<List<Category>> {
        return repository.getAllCategories()
    }

}

//class DashboardViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return modelClass.getConstructor(Repository::class.java)
//                .newInstance(repository)
//    }

//}