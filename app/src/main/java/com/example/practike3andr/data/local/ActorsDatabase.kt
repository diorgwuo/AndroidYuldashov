package com.example.practike3andr.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteActorEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ActorsDatabase : RoomDatabase() {
    abstract fun favoriteActorDao(): FavoriteActorDao
}

