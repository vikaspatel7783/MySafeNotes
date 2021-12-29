package com.vikas.mobile.mynotes.ui

import androidx.lifecycle.*
import com.vikas.mobile.mynotes.data.Repository
import com.vikas.mobile.mynotes.data.entity.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUpdateNoteViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var repository: Repository

    fun getNote(noteId: Long) = repository.getNote(noteId)

    fun addUpdateNote(note: Note) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUpdateNote(note)
        }

//    class CategoryTabViewModelFactory() : ViewModelProvider.Factory {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return modelClass.getConstructor().newInstance()
//    }

}