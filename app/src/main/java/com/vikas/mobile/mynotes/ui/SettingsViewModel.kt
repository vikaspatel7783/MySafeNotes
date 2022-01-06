package com.vikas.mobile.mynotes.ui

import androidx.lifecycle.*
import com.vikas.mobile.mynotes.data.Repository
import com.vikas.mobile.mynotes.data.entity.Setting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var repository: Repository

    private val _authSettings: MutableLiveData<Setting> by lazy {
        MutableLiveData<Setting>().also { retainedAuthSettings ->
            viewModelScope.launch(Dispatchers.IO) {
                repository.getSetting(Setting.AUTH_SETTINGS_NAME).let {
                    retainedAuthSettings.postValue(it)
                }
            }
        }
    }

    fun getAuthSetting(): LiveData<Setting> {
        return _authSettings
    }

    fun updateAuthSetting(authSetting: Setting) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSettings(authSetting)
        }
    }
}