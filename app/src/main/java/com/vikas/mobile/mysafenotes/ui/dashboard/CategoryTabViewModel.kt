package com.vikas.mobile.mysafenotes.ui.dashboard

import androidx.lifecycle.*
import com.vikas.mobile.mysafenotes.data.Repository
import com.vikas.mobile.mysafenotes.data.entity.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryTabViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var repository: Repository

    fun getNotes(categoryId: Long) = repository.getNotes(categoryId)

    fun deleteNote(note: Note) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(note)
        }

//    class CategoryTabViewModelFactory() : ViewModelProvider.Factory {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return modelClass.getConstructor().newInstance()
//    }

}