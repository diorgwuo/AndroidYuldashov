package com.example.practike3andr.data.repository

import com.example.practike3andr.data.model.*
import com.example.practike3andr.data.remote.ActorsApi
import com.example.practike3andr.data.remote.RemoteActor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ActorsRepository {
    suspend fun getActors(limit: Int, page: Int): ActorsResponse
}

class ActorsRepositoryImpl(
    private val api: ActorsApi
) : ActorsRepository {

    override suspend fun getActors(limit: Int, page: Int): ActorsResponse = withContext(Dispatchers.IO) {
        val remote = api.getActors(limit = limit, page = page)
        ActorsResponse(
            docs = remote.docs.map { it.toDomain() },
            total = remote.total,
            limit = remote.limit,
            page = remote.page,
            pages = remote.pages
        )
    }
}

private fun RemoteActor.toDomain(): Actor = Actor(
    id = id,
    name = name ?: enName ?: "Неизвестный",
    enName = enName,
    photo = photo,
    sex = sex,
    growth = growth,
    birthday = birthday,
    death = death,
    age = age,
    birthPlace = birthPlace?.map { Place(it.value) }?.filter { it.value != null },
    deathPlace = deathPlace?.map { Place(it.value) }?.filter { it.value != null },
    spouses = spouses?.map {
        Spouse(
            id = it.id,
            name = it.name,
            divorced = it.divorced,
            divorcedReason = it.divorcedReason,
            sex = it.sex,
            children = it.children,
            relation = it.relation
        )
    },
    countAwards = countAwards,
    profession = profession?.map { Profession(it.value) }?.filter { it.value != null },
    facts = facts?.map { Fact(it.value) }?.filter { it.value != null },
    movies = movies?.map {
        Movie(
            id = it.id,
            name = it.name,
            alternativeName = it.alternativeName,
            rating = it.rating,
            general = it.general,
            description = it.description,
            enProfession = it.enProfession
        )
    },
    updatedAt = updatedAt,
    createdAt = createdAt
)


