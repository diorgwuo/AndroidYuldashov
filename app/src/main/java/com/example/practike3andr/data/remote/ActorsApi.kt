package com.example.practike3andr.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

// DTOs optimized for the remote schema (example uses a placeholder free API)
data class RemoteActorsResponse(
    val docs: List<RemoteActor>,
    val total: Int,
    val limit: Int,
    val page: Int,
    val pages: Int
)

data class RemoteActor(
    val id: Int,
    val name: String,
    val enName: String?,
    val photo: String?,
    val sex: String? = null,
    val growth: Int? = null,
    val birthday: String? = null,
    val death: String? = null,
    val age: Int? = null,
    val birthPlace: List<RemotePlace>? = null,
    val deathPlace: List<RemotePlace>? = null,
    val spouses: List<RemoteSpouse>? = null,
    val countAwards: Int? = null,
    val profession: List<RemoteProfession>? = null,
    val facts: List<RemoteFact>? = null,
    val movies: List<RemoteMovie>? = null,
    val updatedAt: String? = null,
    val createdAt: String? = null
)

data class RemotePlace(val value: String)
data class RemoteSpouse(
    val id: Int?,
    val name: String?,
    val divorced: Boolean?,
    val divorcedReason: String?,
    val sex: String?,
    val children: Int?,
    val relation: String?
)
data class RemoteProfession(val value: String)
data class RemoteFact(val value: String)
data class RemoteMovie(
    val id: Int?,
    val name: String?,
    val alternativeName: String?,
    val rating: Double?,
    val general: Boolean?,
    val description: String?,
    val enProfession: String?
)

interface ActorsApi {
    @GET("person")
    suspend fun getActors(
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 1,
        @Query("selectFields") selectFields: List<String> = listOf(
            "id", "name", "enName", "photo", "sex", "growth", "birthday",
            "death", "age", "birthPlace", "deathPlace", "spouses",
            "countAwards", "profession", "facts", "movies", "updatedAt", "createdAt"
        )
    ): RemoteActorsResponse
}


