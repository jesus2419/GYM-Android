package com.example.gymandroid.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.gymandroid.model.Exercise
import com.example.gymandroid.model.ExerciseDBHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val dbHandler = ExerciseDBHandler(application)

    private val _favorites = MutableStateFlow<List<Exercise>>(emptyList())
    val favorites: StateFlow<List<Exercise>> = _favorites.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _favorites.value = dbHandler.getAllFavorites()
            } catch (e: Exception) {
                // Manejar error
                _favorites.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun removeFavorite(exercise: Exercise) {
        viewModelScope.launch {
            if (dbHandler.removeFavorite(exercise.id)) {
                _favorites.value = _favorites.value.filter { it.id != exercise.id }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                // Obtenemos el Application desde los extras
                val application = checkNotNull(extras[APPLICATION_KEY]) {
                    "Application no disponible en CreationExtras"
                }
                return FavoritesViewModel(application) as T
            }
        }
    }
}