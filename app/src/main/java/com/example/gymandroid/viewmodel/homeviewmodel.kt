package com.example.gymandroid.viewmodel


import android.app.Application
import android.util.Log
import androidx.compose.material3.Text
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymandroid.model.Category
import com.example.gymandroid.model.Exercise
import com.example.gymandroid.model.ExerciseDBHandler
import com.example.gymandroid.model.ExerciseRepository
import com.example.gymandroid.model.FullRoutine
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHandler = ExerciseDBHandler(application)

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    // Estado para ejercicio seleccionado
    private val _selectedExercise = MutableStateFlow<Exercise?>(null)
    val selectedExercise: StateFlow<Exercise?> = _selectedExercise.asStateFlow()

    // Estado para la rutina seleccionada
    private val _selectedRoutine = MutableStateFlow<FullRoutine?>(null)
    val selectedRoutine: StateFlow<FullRoutine?> = _selectedRoutine.asStateFlow()


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
            //_categories.value = ExerciseRepository.getCategories2()
        }
    }

    fun loadExerciseById(id: Int) {
        viewModelScope.launch {
            try {
                _selectedExercise.value = ExerciseRepository.getExerciseById(id)
            } catch (e: Exception) {
                // Manejo de errores
                _selectedExercise.value = null
            }
        }
    }

    // Función para obtener rutina por ID
    fun getRoutineById(routineId: Int) {
        viewModelScope.launch {
            try {
                // Obtener todas las rutinas y filtrar por ID
                val allRoutines = dbHandler.getAllSavedRoutines()
                _selectedRoutine.value = allRoutines.find { it.routine.id == routineId }

                if (_selectedRoutine.value == null) {
                    Log.w("ROUTINE", "No se encontró rutina con ID: $routineId")
                }
            } catch (e: Exception) {
                Log.e("ROUTINE", "Error al obtener rutina", e)
                _selectedRoutine.value = null
            }
        }
    }




    // Función para cargar favoritos al iniciar
    init {
        loadInitialFavorites()
    }

    private fun loadInitialFavorites() {
        viewModelScope.launch {
            val favoriteIds = dbHandler.getAllFavorites().map { it.id }
            _favoriteExercises.value = favoriteIds.toSet()
        }
    }

    // Modifica tu toggleFavorite para usar la base de datos
    fun toggleFavorite(exercise: Exercise) {
        val isCurrentlyFavorite = _favoriteExercises.value.contains(exercise.id)

        if (isCurrentlyFavorite) {
            // Eliminar de favoritos
            dbHandler.removeFavorite(exercise.id)
            _favoriteExercises.value = _favoriteExercises.value - exercise.id
        } else {
            // Agregar a favoritos
            dbHandler.addFavorite(exercise)
            _favoriteExercises.value = _favoriteExercises.value + exercise.id
        }

        // Animación
        _likedExercise.value = exercise
        viewModelScope.launch {
            delay(1000)
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

    // Función para eliminar favorito con verificación
    fun eliminar() {
        if (dbHandler.deleteAllData()){

        }
    }


}