package com.example.practike3andr.ui.cache

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BadgeStateCache @Inject constructor() {
    private val _hasActiveFilters = MutableStateFlow(false)
    val hasActiveFilters: StateFlow<Boolean> = _hasActiveFilters.asStateFlow()
    
    fun setActiveFiltersState(hasFilters: Boolean) {
        _hasActiveFilters.value = hasFilters
    }
}

