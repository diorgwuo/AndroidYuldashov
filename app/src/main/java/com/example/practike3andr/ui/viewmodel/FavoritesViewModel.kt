package com.example.practike3andr.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practike3andr.data.local.FavoriteActorDao
import com.example.practike3andr.data.local.FavoriteActorEntity
import com.example.practike3andr.data.model.Actor
import com.example.practike3andr.data.model.Fact
import com.example.practike3andr.data.model.Movie
import com.example.practike3andr.data.model.Place
import com.example.practike3andr.data.model.Profession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoriteActorDao: FavoriteActorDao
) : ViewModel() {
    
    val favorites = favoriteActorDao.getAllFavorites()
        .map { entities ->
            entities.map { it.toActor() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    suspend fun addToFavorites(actor: Actor) {
        favoriteActorDao.insertFavorite(actor.toEntity())
    }
    
    suspend fun removeFromFavorites(actor: Actor) {
        favoriteActorDao.deleteFavorite(actor.id)
    }
    
    suspend fun isFavorite(actorId: Int): Boolean {
        return favoriteActorDao.isFavorite(actorId)
    }
}

fun FavoriteActorEntity.toActor(): Actor {
    return Actor(
        id = id,
        name = name,
        enName = enName,
        photo = photo,
        sex = sex,
        growth = growth,
        birthday = birthday,
        death = death,
        age = age,
        birthPlace = birthPlace?.let { listOf(Place(it)) },
        deathPlace = deathPlace?.let { listOf(Place(it)) },
        spouses = null,
        countAwards = countAwards,
        profession = profession?.let { listOf(Profession(it)) },
        facts = facts?.let { listOf(Fact(it)) },
        movies = null,
        updatedAt = updatedAt,
        createdAt = createdAt
    )
}

fun Actor.toEntity(): FavoriteActorEntity {
    return FavoriteActorEntity(
        id = id,
        name = name.ifEmpty { enName ?: "Неизвестный" },
        enName = enName,
        photo = photo,
        sex = sex,
        growth = growth,
        birthday = birthday,
        death = death,
        age = age,
        birthPlace = birthPlace?.firstOrNull()?.value,
        deathPlace = deathPlace?.firstOrNull()?.value,
        countAwards = countAwards,
        profession = profession?.firstOrNull()?.value,
        facts = facts?.firstOrNull()?.value,
        updatedAt = updatedAt,
        createdAt = createdAt
    )
}

