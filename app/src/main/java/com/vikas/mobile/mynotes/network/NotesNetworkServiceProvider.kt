package com.vikas.mobile.mynotes.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NotesNetworkServiceProvider {

    @Provides
    @Singleton
    fun provideNetworkService(): NotesNetworkService {
        return NotesNetworkService.create()
    }
}