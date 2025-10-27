package com.example.practike3andr.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.practike3andr.data.model.Actor
import com.example.practike3andr.domain.GetActorsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ActorsViewModel(
    private val useCase: GetActorsUseCase
) : ViewModel() {
    
    private val _actors = MutableStateFlow<List<Actor>>(emptyList())
    val actors: StateFlow<List<Actor>> = _actors.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        loadActors()
    }
    
    fun loadActors(limit: Int = 10, page: Int = 1) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                useCase.execute(limit, page).collect { result ->
                    result.onSuccess { list ->
                        _actors.value = list
                    }.onFailure { t ->
                        _error.value = t.message ?: "Неизвестная ошибка"
                        _actors.value = emptyList()
                    }
                }
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

class ActorsViewModelFactory(
    private val useCase: GetActorsUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActorsViewModel::class.java)) {
            return ActorsViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
