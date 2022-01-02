package com.vikas.mobile.mynotes.ui

import androidx.lifecycle.*
import com.vikas.mobile.mynotes.data.Repository
import com.vikas.mobile.mynotes.data.entity.Note
import com.vikas.mobile.mynotes.data.entity.Setting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var repository: Repository

    private val _authSetting = MutableLiveData<Setting>()
    private val authSetting: LiveData<Setting> = _authSetting

    fun getAuthSetting(): LiveData<Setting> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSetting(Setting.AUTH_SETTINGS_NAME).let {
                _authSetting.postValue(it)
            }
        }
        return authSetting
    }

    fun updateAuthSetting(authSetting: Setting) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSettings(authSetting)
        }
    }
}