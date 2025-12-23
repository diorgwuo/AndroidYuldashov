package com.example.practike3andr.domain

import com.example.practike3andr.data.model.Actor
import com.example.practike3andr.data.repository.ActorsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetActorsUseCase(
    private val repository: ActorsRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    // Simple in-memory cache using StateFlow
    private val cache = MutableStateFlow<List<Actor>>(emptyList())

    fun execute(limit: Int, page: Int): Flow<Result<List<Actor>>> = flow {
        if (cache.value.isNotEmpty()) {
            emit(Result.success(cache.value))
        }
        try {
            val response = repository.getActors(limit, page)
            val actors = response.docs
            cache.value = actors
            emit(Result.success(actors))
        } catch (t: Throwable) {
            emit(Result.failure(t))
        }
    }.flowOn(ioDispatcher)
}


