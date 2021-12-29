package com.vikas.mobile.mynotes.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vikas.mobile.mynotes.data.Repository
import com.vikas.mobile.mynotes.data.entity.Category
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

    fun deleteNotesForCategory(categoryId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCategoryAndNotes(categoryId)
        }
    }

}

//class DashboardViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return modelClass.getConstructor(Repository::class.java)
//                .newInstance(repository)
//    }

//}