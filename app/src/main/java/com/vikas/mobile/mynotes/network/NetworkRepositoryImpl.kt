package com.vikas.mobile.mynotes.network

import com.vikas.mobile.mynotes.network.entity.CloudNoteRequest
import com.vikas.mobile.mynotes.network.entity.CloudNoteResponse
import retrofit2.Response
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(private val notesNetworkService: NotesNetworkService) : NetworkRepository {

    override suspend fun addNote(cloudNoteRequest: CloudNoteRequest): CloudNoteResponse {
        return try {
            notesNetworkService.addNote(cloudNoteRequest)
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw ex
        }
    }

    override suspend fun updateNote(cloudNoteRequest: CloudNoteRequest): CloudNoteResponse {
        return try {
            notesNetworkService.updateNote(cloudNoteRequest)
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw ex
        }
    }


}