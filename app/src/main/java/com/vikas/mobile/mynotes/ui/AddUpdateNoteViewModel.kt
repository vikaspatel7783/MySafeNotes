package com.vikas.mobile.mynotes.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.vikas.mobile.mynotes.data.Repository
import com.vikas.mobile.mynotes.data.entity.Note
import com.vikas.mobile.mynotes.network.NetworkRepository
import com.vikas.mobile.mynotes.network.entity.CloudNoteRequest
import com.vikas.mobile.mynotes.worker.CloudNotesWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUpdateNoteViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var repository: Repository
    @Inject lateinit var networkRepository: NetworkRepository
    @Inject lateinit var workManagerModule: WorkManager

    fun getNote(noteId: Long) = repository.getNote(noteId)

    fun addUpdateNote(note: Note) =
        viewModelScope.launch(Dispatchers.IO) {
            addUpdateNoteInNetwork(note)
//            repository.addUpdateNote(note)
//            triggerUploadNoteWorker(note)
        }

    private suspend fun addUpdateNoteInNetwork(note: Note) {
        val cloudNoteRequest = CloudNoteRequest(
            cloudNoteId = null,
            noteCategory = repository.getCategory(note.categoryId).name.content,
            noteContent = note.noteContent.content
        )
        val cloudNoteResponse = networkRepository.addNote(cloudNoteRequest)
        note.cloudTokenId = cloudNoteResponse.response.toString()
        note.syncedStatus = true

        repository.addUpdateNote(note)
    }

    private fun triggerUploadNoteWorker(note: Note) {
        val dataBuilder = Data.Builder()
        dataBuilder.put(CloudNotesWorker.CLOUD_OPERATION,
            if (note.cloudTokenId == null) CloudNotesWorker.CLOUD_OPERATION_ADD_NOTE else CloudNotesWorker.CLOUD_OPERATION_UPDATE_NOTE
        )
        dataBuilder.put(CloudNotesWorker.DATA, Note.serialize(note))

        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val workerRequest = OneTimeWorkRequestBuilder<CloudNotesWorker>()
            .setInputData(dataBuilder.build())
            .setConstraints(constraints)
            .build()
        workManagerModule.enqueue(workerRequest)
    }


//    class CategoryTabViewModelFactory() : ViewModelProvider.Factory {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return modelClass.getConstructor().newInstance()
//    }

}