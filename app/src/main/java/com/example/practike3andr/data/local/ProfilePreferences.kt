package com.example.practike3andr.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.practike3andr.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfilePreferences(private val context: Context) {
    
    companion object {
        private val Context.profileDataStore: DataStore<Preferences> by preferencesDataStore("user_profile")
        
        private val FULL_NAME_KEY = stringPreferencesKey("full_name")
        private val AVATAR_URI_KEY = stringPreferencesKey("avatar_uri")
        private val RESUME_URL_KEY = stringPreferencesKey("resume_url")
        private val POSITION_KEY = stringPreferencesKey("position")
    }
    
    val profile: Flow<UserProfile> = context.profileDataStore.data.map { preferences ->
        UserProfile(
            fullName = preferences[FULL_NAME_KEY] ?: "",
            avatarUri = preferences[AVATAR_URI_KEY] ?: "",
            resumeUrl = preferences[RESUME_URL_KEY] ?: "",
            position = preferences[POSITION_KEY] ?: ""
        )
    }
    
    suspend fun saveProfile(profile: UserProfile) {
        context.profileDataStore.edit { preferences ->
            preferences[FULL_NAME_KEY] = profile.fullName
            preferences[AVATAR_URI_KEY] = profile.avatarUri
            preferences[RESUME_URL_KEY] = profile.resumeUrl
            preferences[POSITION_KEY] = profile.position
        }
    }
}

