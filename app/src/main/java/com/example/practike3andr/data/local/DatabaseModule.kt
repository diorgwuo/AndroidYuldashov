package com.example.practike3andr.data.local

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ActorsDatabase {
        return Room.databaseBuilder(
            context,
            ActorsDatabase::class.java,
            "actors_database"
        ).build()
    }
    
    @Provides
    fun provideFavoriteActorDao(database: ActorsDatabase): FavoriteActorDao {
        return database.favoriteActorDao()
    }
}

