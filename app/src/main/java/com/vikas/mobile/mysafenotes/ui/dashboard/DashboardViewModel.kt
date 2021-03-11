package com.vikas.mobile.mysafenotes.ui.dashboard

import androidx.lifecycle.*
import com.vikas.mobile.mysafenotes.data.Repository
import com.vikas.mobile.mysafenotes.data.RepositoryImpl
import com.vikas.mobile.mysafenotes.data.entity.Category
import com.vikas.mobile.mysafenotes.data.entity.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(): ViewModel() {

    @Inject lateinit var repository: Repository

    fun getAllCategories(): LiveData<List<Category>> {
        return repository.getAllCategories()
    }

    fun addNewNote(note: Note) {
        viewModelScope.launch {
            repository.addNote(note)
        }
    }

}

//class DashboardViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return modelClass.getConstructor(Repository::class.java)
//                .newInstance(repository)
//    }

//}