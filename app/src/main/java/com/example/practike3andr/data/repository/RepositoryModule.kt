package com.example.practike3andr.data.repository

import com.example.practike3andr.data.remote.ActorsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    
    @Provides
    @Singleton
    fun provideActorsRepository(api: ActorsApi): ActorsRepository {
        return ActorsRepositoryImpl(api)
    }
}

