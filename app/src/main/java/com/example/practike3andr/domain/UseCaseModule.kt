package com.example.practike3andr.domain

import com.example.practike3andr.data.repository.ActorsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    
    @Provides
    @Singleton
    fun provideGetActorsUseCase(repository: ActorsRepository): GetActorsUseCase {
        return GetActorsUseCase(repository)
    }
}

