package com.vikas.mobile.mysafenotes.ui.dashboard

import androidx.lifecycle.*
import com.vikas.mobile.mysafenotes.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryTabViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var repository: Repository

    fun getNotes(categoryId: Long) = repository.getNotes(categoryId)

//    class CategoryTabViewModelFactory() : ViewModelProvider.Factory {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return modelClass.getConstructor().newInstance()
//    }

}