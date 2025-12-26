package com.example.practike3andr.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_actors")
data class FavoriteActorEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val enName: String?,
    val photo: String?,
    val sex: String?,
    val growth: Int?,
    val birthday: String?,
    val death: String?,
    val age: Int?,
    val birthPlace: String?,
    val deathPlace: String?,
    val countAwards: Int?,
    val profession: String?,
    val facts: String?,
    val updatedAt: String?,
    val createdAt: String?
)

