package com.vikas.mobile.mysafenotes.di

import com.vikas.mobile.mysafenotes.data.Repository
import com.vikas.mobile.mysafenotes.data.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepository(repositoryImpl: RepositoryImpl): Repository
}