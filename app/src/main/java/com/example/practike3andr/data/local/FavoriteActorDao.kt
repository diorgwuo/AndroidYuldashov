package com.example.practike3andr.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteActorDao {
    
    @Query("SELECT * FROM favorite_actors ORDER BY name ASC")
    fun getAllFavorites(): Flow<List<FavoriteActorEntity>>
    
    @Query("SELECT * FROM favorite_actors WHERE id = :id")
    suspend fun getFavoriteById(id: Int): FavoriteActorEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteActorEntity)
    
    @Query("DELETE FROM favorite_actors WHERE id = :id")
    suspend fun deleteFavorite(id: Int)
    
    @Query("SELECT COUNT(*) FROM favorite_actors WHERE id = :id")
    suspend fun isFavorite(id: Int): Boolean
}

