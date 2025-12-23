package com.example.practike3andr.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FilterPreferences(private val context: Context) {
    
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("filter_preferences")
        
        private val NAME_FILTER_KEY = stringPreferencesKey("name_filter")
        private val MIN_AGE_KEY = stringPreferencesKey("min_age")
        private val MAX_AGE_KEY = stringPreferencesKey("max_age")
        private val SEX_FILTER_KEY = stringPreferencesKey("sex_filter")
    }
    
    val filterState: Flow<FilterState> = context.dataStore.data.map { preferences ->
        FilterState(
            nameFilter = preferences[NAME_FILTER_KEY] ?: "",
            minAge = preferences[MIN_AGE_KEY] ?: "",
            maxAge = preferences[MAX_AGE_KEY] ?: "",
            sexFilter = preferences[SEX_FILTER_KEY] ?: ""
        )
    }
    
    suspend fun saveNameFilter(name: String) {
        context.dataStore.edit { preferences ->
            preferences[NAME_FILTER_KEY] = name
        }
    }
    
    suspend fun saveAgeRange(minAge: String, maxAge: String) {
        context.dataStore.edit { preferences ->
            preferences[MIN_AGE_KEY] = minAge
            preferences[MAX_AGE_KEY] = maxAge
        }
    }
    
    suspend fun saveSexFilter(sex: String) {
        context.dataStore.edit { preferences ->
            preferences[SEX_FILTER_KEY] = sex
        }
    }
    
    suspend fun saveFilterState(state: FilterState) {
        context.dataStore.edit { preferences ->
            preferences[NAME_FILTER_KEY] = state.nameFilter
            preferences[MIN_AGE_KEY] = state.minAge
            preferences[MAX_AGE_KEY] = state.maxAge
            preferences[SEX_FILTER_KEY] = state.sexFilter
        }
    }
    
    suspend fun clearFilters() {
        context.dataStore.edit { preferences ->
            preferences.remove(NAME_FILTER_KEY)
            preferences.remove(MIN_AGE_KEY)
            preferences.remove(MAX_AGE_KEY)
            preferences.remove(SEX_FILTER_KEY)
        }
    }
}

data class FilterState(
    val nameFilter: String = "",
    val minAge: String = "",
    val maxAge: String = "",
    val sexFilter: String = ""
) {
    fun hasActiveFilters(): Boolean {
        return nameFilter.isNotBlank() || 
               minAge.isNotBlank() || 
               maxAge.isNotBlank() || 
               sexFilter.isNotBlank()
    }
}

