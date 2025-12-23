package com.example.practike3andr.data.model

import java.util.Date

data class Actor(
    val id: Int,
    val name: String = "",
    val enName: String?,
    val photo: String?,
    val sex: String?,
    val growth: Int?,
    val birthday: String?,
    val death: String?,
    val age: Int?,
    val birthPlace: List<Place>?,
    val deathPlace: List<Place>?,
    val spouses: List<Spouse>?,
    val countAwards: Int?,
    val profession: List<Profession>?,
    val facts: List<Fact>?,
    val movies: List<Movie>?,
    val updatedAt: String?,
    val createdAt: String?
)

data class Place(
    val value: String?
)

data class Spouse(
    val id: Int?,
    val name: String?,
    val divorced: Boolean?,
    val divorcedReason: String?,
    val sex: String?,
    val children: Int?,
    val relation: String?
)

data class Profession(
    val value: String?
)

data class Fact(
    val value: String?
)

data class Movie(
    val id: Int?,
    val name: String?,
    val alternativeName: String?,
    val rating: Double?,
    val general: Boolean?,
    val description: String?,
    val enProfession: String?
)

data class ActorsResponse(
    val docs: List<Actor>,
    val total: Int,
    val limit: Int,
    val page: Int,
    val pages: Int
)
