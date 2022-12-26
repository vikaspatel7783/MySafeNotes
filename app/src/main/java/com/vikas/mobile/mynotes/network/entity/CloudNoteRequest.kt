package com.vikas.mobile.mynotes.network.entity

import com.google.gson.annotations.SerializedName

data class CloudNoteRequest(

        @SerializedName("id")
        var cloudNoteId: String?,

        @SerializedName("noteCategory")
        var noteCategory: String?,

        @SerializedName("noteContent")
        var noteContent: String?
)
