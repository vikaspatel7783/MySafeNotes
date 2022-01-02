package com.vikas.mobile.mynotes.ui

import androidx.lifecycle.*
import com.vikas.mobile.mynotes.data.Repository
import com.vikas.mobile.mynotes.data.entity.Category
import com.vikas.mobile.mynotes.data.entity.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchNotesViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var repository: Repository

    private var _filteredNotes = MutableLiveData<List<Note>>()

    private val _category = MutableLiveData<Category>()
    val category: LiveData<Category> = _category


    fun getCategory(categoryId: Long): LiveData<Category> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCategory(categoryId).let {
                _category.postValue(it)
            }
        }
        return category
    }

    fun searchNotes(searchText: String): LiveData<List<Note>> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.searchNotes(searchText, onResult = {
                _filteredNotes.postValue(it)
            })
        }
        return _filteredNotes
    }

    fun deleteNote(note: Note) =
            viewModelScope.launch(Dispatchers.IO) {
                repository.deleteNote(note)
            }

//    class CategoryTabViewModelFactory() : ViewModelProvider.Factory {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return modelClass.getConstructor().newInstance()
//    }

}