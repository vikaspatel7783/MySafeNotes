package com.vikas.mobile.mynotes.ui

import androidx.lifecycle.*
import com.vikas.mobile.mynotes.data.Repository
import com.vikas.mobile.mynotes.data.entity.MaskedData
import com.vikas.mobile.mynotes.data.entity.Note
import com.vikas.mobile.mynotes.data.entity.NoteExport
import com.vikas.mobile.mynotes.data.entity.Setting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var repository: Repository

    private val noteExportList = MutableLiveData<NoteExport>()
    private val noteImportSignal = MutableLiveData<Boolean>()

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

    fun getAllNotes(): LiveData<NoteExport> {
        viewModelScope.launch(Dispatchers.IO) {
            noteExportList.postValue(repository.getAllNotes())
        }
        return noteExportList
    }

    fun importNotes(jsonObject: JSONObject): MutableLiveData<Boolean> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()

            for (i in 0 until jsonObject.names().length()) {
                val categoryName = jsonObject.names().get(i) as String
                val categoryId = repository.addCategory(categoryName)

                val noteContentArray = jsonObject.getJSONArray(categoryName)
                val finalNotes = mutableListOf<Note>()
                for (j in 0 until noteContentArray.length()) {
                    val noteContent = noteContentArray[j]
                    finalNotes.add(Note(categoryId = categoryId, noteContent = MaskedData(noteContent.toString())))
                }
                repository.addNotes(finalNotes)
            }
            noteImportSignal.postValue(true)
        }
        return noteImportSignal
    }
}