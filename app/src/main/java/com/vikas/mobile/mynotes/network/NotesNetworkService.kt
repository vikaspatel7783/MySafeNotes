package com.vikas.mobile.mynotes.network

import com.vikas.mobile.mynotes.network.entity.CloudNoteRequest
import com.vikas.mobile.mynotes.network.entity.CloudNoteResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface NotesNetworkService {

    @GET("/notes")
    suspend fun getAllNotes(): List<CloudNoteRequest>

    @POST("/notes")
    suspend fun addNote(@Body cloudNoteRequest: CloudNoteRequest): CloudNoteResponse

    @PUT("/notes")
    suspend fun updateNote(@Body cloudNoteRequest: CloudNoteRequest): CloudNoteResponse

//    @POST("v1/item/image")
//    suspend fun matchItem(@Body itemMatchRequest: ItemMatchRequest): ItemMatchResponse

    companion object {
        private const val BASE_URL = "http://10.0.2.2:8080"

        fun create(): NotesNetworkService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NotesNetworkService::class.java)
        }
    }
}