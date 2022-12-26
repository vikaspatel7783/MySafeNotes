package com.vikas.mobile.mynotes.network

import com.vikas.mobile.mynotes.network.entity.CloudNoteRequest
import com.vikas.mobile.mynotes.network.entity.CloudNoteResponse

interface NetworkRepository {
    suspend fun addNote(cloudNoteRequest: CloudNoteRequest): CloudNoteResponse

}