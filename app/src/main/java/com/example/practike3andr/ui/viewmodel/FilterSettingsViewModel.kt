package com.example.practike3andr.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practike3andr.data.local.FilterPreferences
import com.example.practike3andr.data.local.FilterState
import com.example.practike3andr.ui.cache.BadgeStateCache
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterSettingsViewModel @Inject constructor(
    private val filterPreferences: FilterPreferences,
    private val badgeStateCache: BadgeStateCache
) : ViewModel() {
    
    val filterState = filterPreferences.filterState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FilterState()
    )
    
    init {
        // Update badge state when filter state changes
        filterPreferences.filterState.onEach { state ->
            badgeStateCache.setActiveFiltersState(state.hasActiveFilters())
        }.launchIn(viewModelScope)
    }
    
    fun saveFilters(
        nameFilter: String,
        minAge: String,
        maxAge: String,
        sexFilter: String
    ) {
        viewModelScope.launch {
            val state = FilterState(
                nameFilter = nameFilter.trim(),
                minAge = minAge.trim(),
                maxAge = maxAge.trim(),
                sexFilter = sexFilter.trim()
            )
            filterPreferences.saveFilterState(state)
            badgeStateCache.setActiveFiltersState(state.hasActiveFilters())
        }
    }
    
    fun clearFilters() {
        viewModelScope.launch {
            filterPreferences.clearFilters()
            badgeStateCache.setActiveFiltersState(false)
        }
    }
}

