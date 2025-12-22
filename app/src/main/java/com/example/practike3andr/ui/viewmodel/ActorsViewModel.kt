package com.example.practike3andr.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practike3andr.ui.cache.BadgeStateCache
import com.example.practike3andr.data.local.FilterPreferences
import com.example.practike3andr.data.local.FilterState
import com.example.practike3andr.data.model.Actor
import com.example.practike3andr.domain.GetActorsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActorsViewModel @Inject constructor(
    private val useCase: GetActorsUseCase,
    private val filterPreferences: FilterPreferences,
    private val badgeStateCache: BadgeStateCache
) : ViewModel() {
    
    private val _allActors = MutableStateFlow<List<Actor>>(emptyList())
    private val _actors = MutableStateFlow<List<Actor>>(emptyList())
    val actors: StateFlow<List<Actor>> = _actors.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _currentFilterState = MutableStateFlow(FilterState())
    val currentFilterState: StateFlow<FilterState> = _currentFilterState.asStateFlow()

    val hasActiveFilters: StateFlow<Boolean> = badgeStateCache.hasActiveFilters
    
    init {
        // Observe filter state changes to update badge and reapply filters
        filterPreferences.filterState.onEach { state ->
            _currentFilterState.value = state
            badgeStateCache.setActiveFiltersState(state.hasActiveFilters())
            // Применяем фильтры к исходным данным
            _actors.value = applyFilters(_allActors.value)
        }.launchIn(viewModelScope)
        
        loadActors()
    }
    
    fun loadActors(limit: Int = 10, page: Int = 1) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                useCase.execute(limit, page).collect { result ->
                    result.onSuccess { list ->
                        _allActors.value = list
                        _actors.value = applyFilters(list)
                    }.onFailure { t ->
                        _error.value = t.message ?: "Неизвестная ошибка"
                        _allActors.value = emptyList()
                        _actors.value = emptyList()
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Неизвестная ошибка"
                _allActors.value = emptyList()
                _actors.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private suspend fun applyFilters(actors: List<Actor>): List<Actor> {
        // Get the current filter state
        val state = _currentFilterState.value
        var filtered = actors
        
        // Filter by name
        if (state.nameFilter.isNotBlank()) {
            filtered = filtered.filter { actor ->
                actor.name.contains(state.nameFilter, ignoreCase = true) ||
                actor.enName?.contains(state.nameFilter, ignoreCase = true) == true
            }
        }
        
        // Filter by age
        if (state.minAge.isNotBlank()) {
            val minAge = state.minAge.toIntOrNull() ?: 0
            filtered = filtered.filter { it.age != null && it.age >= minAge }
        }
        if (state.maxAge.isNotBlank()) {
            val maxAge = state.maxAge.toIntOrNull() ?: Int.MAX_VALUE
            filtered = filtered.filter { it.age != null && it.age <= maxAge }
        }
        
        // Filter by sex
        if (state.sexFilter.isNotBlank()) {
            val sexFilter = state.sexFilter.lowercase()
            filtered = filtered.filter {
                when {
                    sexFilter.contains("муж") && it.sex?.lowercase() == "male" -> true
                    sexFilter.contains("жен") && it.sex?.lowercase() == "female" -> true
                    else -> false
                }
            }
        }
        
        return filtered
    }
    
    fun refreshActors() {
        loadActors()
    }
    
    fun loadWithCurrentFilters() {
        loadActors()
    }
}
