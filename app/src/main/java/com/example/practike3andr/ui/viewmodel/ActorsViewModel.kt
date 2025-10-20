package com.example.practike3andr.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practike3andr.data.model.Actor
import com.example.practike3andr.data.mock.MockData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ActorsViewModel : ViewModel() {
    
    private val _actors = MutableStateFlow<List<Actor>>(emptyList())
    val actors: StateFlow<List<Actor>> = _actors.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        loadActors()
    }
    
    fun loadActors() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                // Имитируем загрузку данных
                kotlinx.coroutines.delay(1000)
                val response = MockData.getMockActors()
                _actors.value = response.docs
            } catch (e: Exception) {
                _error.value = e.message ?: "Неизвестная ошибка"
                // В случае ошибки устанавливаем пустой список
                _actors.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun refreshActors() {
        loadActors()
    }
}
