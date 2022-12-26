package com.vikas.mobile.mynotes.network.entity

import com.google.gson.annotations.SerializedName

data class CloudNoteResponse(

        @SerializedName("response")
        var response: Any
)
