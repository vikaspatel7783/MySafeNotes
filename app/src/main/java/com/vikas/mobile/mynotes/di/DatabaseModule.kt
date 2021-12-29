package com.vikas.mobile.mynotes.di

import android.content.Context
import com.vikas.mobile.mynotes.data.MySafeNotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): MySafeNotesDatabase {
        return MySafeNotesDatabase.getInstance(appContext)
    }
}