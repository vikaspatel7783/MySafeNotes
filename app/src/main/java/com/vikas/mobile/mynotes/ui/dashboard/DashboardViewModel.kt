package com.vikas.mobile.mynotes.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vikas.mobile.mynotes.data.Repository
import com.vikas.mobile.mynotes.data.entity.Category
import com.vikas.mobile.mynotes.data.entity.Setting
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

    private val _authSetting = MutableLiveData<Setting>()
    private val authSetting: LiveData<Setting> = _authSetting

    fun getAuthSetting(): LiveData<Setting> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSetting(Setting.AUTH_SETTINGS_NAME).let {
                _authSetting.postValue(it ?: Setting(Setting.AUTH_SETTINGS_NAME, value = Setting.AUTH_OFF))
            }
        }
        return authSetting
    }
}

//class DashboardViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return modelClass.getConstructor(Repository::class.java)
//                .newInstance(repository)
//    }

//}