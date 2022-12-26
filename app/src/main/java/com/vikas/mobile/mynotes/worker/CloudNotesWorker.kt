
package com.vikas.mobile.mynotes.worker

import android.content.Context
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vikas.mobile.mynotes.data.MySafeNotesDatabase
import com.vikas.mobile.mynotes.data.Repository
import com.vikas.mobile.mynotes.data.entity.Note
import com.vikas.mobile.mynotes.data.entity.Setting
import com.vikas.mobile.mynotes.network.NetworkCheck
import com.vikas.mobile.mynotes.network.NetworkRepository
import com.vikas.mobile.mynotes.network.entity.CloudNoteRequest
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class CloudNotesWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    @Inject lateinit var repository: Repository
    @Inject lateinit var networkRepository: NetworkRepository

    override suspend fun doWork(): Result = coroutineScope {

        if (NetworkCheck.isDeviceOnline(applicationContext)) {
            Toast.makeText(applicationContext, "No Internet connection found", Toast.LENGTH_LONG).show()
            Result.failure()
        }

        val noteJson = inputData.getString(DATA)
        val mobileNoteObject = noteJson?.let { Note.deserialize(it) }

        when (inputData.getString(CLOUD_OPERATION)) {
            CLOUD_OPERATION_ADD_NOTE -> {
                val cloudNoteRequest = CloudNoteRequest(
                    cloudNoteId = null,
                    noteCategory = "",
                    noteContent = mobileNoteObject?.noteContent?.content
                )
                val cloudNoteResponse = networkRepository.addNote(cloudNoteRequest)
                mobileNoteObject?.cloudTokenId = cloudNoteResponse.response.toString()

                if (mobileNoteObject != null) {
                    repository.addUpdateNote(mobileNoteObject)
                }
            }

            CLOUD_OPERATION_UPDATE_NOTE -> {}
            CLOUD_OPERATION_DELETE_NOTE -> {}
        }

        Result.success()
    }

    companion object {
        const val DATA = "data"
        const val CLOUD_OPERATION = "cloud_operation"
        const val CLOUD_OPERATION_ADD_NOTE = "add_note"
        const val CLOUD_OPERATION_UPDATE_NOTE = "update_note"
        const val CLOUD_OPERATION_DELETE_NOTE = "delete_note"
    }
}
