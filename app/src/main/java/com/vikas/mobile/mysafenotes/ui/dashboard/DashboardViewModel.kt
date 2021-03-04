package com.vikas.mobile.mysafenotes.ui.dashboard

import androidx.lifecycle.*
import com.vikas.mobile.mysafenotes.data.Repository
import com.vikas.mobile.mysafenotes.data.entity.Category

class DashboardViewModel(private val repository: Repository) : ViewModel() {

    fun getAllCategories(): LiveData<List<Category>> {
        return repository.getAllCategories()
    }

//    val allCategories = MediatorLiveData<List<Category>>().apply {
//        viewModelScope.launch(Dispatchers.IO) {
//            postValue(repository.getAllCategories())
//        }
//    }
}

class DashboardViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Repository::class.java)
                .newInstance(repository)
    }

}