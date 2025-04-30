package com.example.gymandroid.viewmodel


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymandroid.model.Category
import com.example.gymandroid.model.Exercise
import com.example.gymandroid.model.ExerciseDBHandler
import com.example.gymandroid.model.ExerciseRepository
import com.example.gymandroid.model.dummyCategories
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHandler = ExerciseDBHandler(application)

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _favoriteExercises = MutableStateFlow<Set<Int>>(emptySet())
    val favoriteExercises: StateFlow<Set<Int>> = _favoriteExercises.asStateFlow()

    // Estado para la animación de like
    private val _likedExercise = MutableStateFlow<Exercise?>(null)
    val likedExercise: StateFlow<Exercise?> = _likedExercise.asStateFlow()
    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _categories.value = ExerciseRepository.getCategories()
        }
    }


    fun toggleFavorite(exercise: Exercise) {
        val wasFavorite = _favoriteExercises.value.contains(exercise.id)

        // Actualizamos el estado de favoritos
        _favoriteExercises.value = if (wasFavorite) {
            _favoriteExercises.value - exercise.id
        } else {
            // Solo registramos cuando se AGREGA a favoritos
            logExerciseLike(exercise)
            _favoriteExercises.value + exercise.id
        }

        // Establecemos el ejercicio al que se dio like para la animación
        _likedExercise.value = exercise

        // Resetear después de un tiempo para la animación
        viewModelScope.launch {
            kotlinx.coroutines.delay(1000) // Duración de la animación
            _likedExercise.value = null
        }
    }
    private fun logExerciseLike(exercise: Exercise) {
        when (val result = dbHandler.addFavorite(exercise)) {
            is ExerciseDBHandler.SaveResult.Success -> {
                // Guardado exitoso (nuevo registro)
                Log.d("FAVORITE", "Nuevo favorito guardado: ${exercise.title}")

            }
            is ExerciseDBHandler.SaveResult.Updated -> {
                // Actualización exitosa (ya existía)
                Log.d("FAVORITE", "Favorito actualizado: ${exercise.title}")

            }
            is ExerciseDBHandler.SaveResult.Error -> {
                // Error al guardar
                Log.e("FAVORITE", "Error al guardar favorito", result.exception)

            }
        }
    }


    // Función para eliminar favorito con verificación
    fun removeFavorite(exercise: Exercise) {
        if (dbHandler.removeFavorite(exercise.id)) {
            Log.d("FAVORITE", "Ejercicio ${exercise.title} eliminado de favoritos")

        } else {
            Log.w("FAVORITE", "No se pudo eliminar el ejercicio ${exercise.title}")
        }
    }
}