package com.example.practike3andr.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practike3andr.data.local.ProfilePreferences
import com.example.practike3andr.data.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profilePreferences: ProfilePreferences
) : ViewModel() {
    
    val profile: StateFlow<UserProfile> = profilePreferences.profile
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile()
        )
    
    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch {
            profilePreferences.saveProfile(profile)
        }
    }
}

