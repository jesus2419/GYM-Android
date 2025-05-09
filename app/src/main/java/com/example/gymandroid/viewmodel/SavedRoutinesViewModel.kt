package com.example.gymandroid.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gymandroid.model.ExerciseDBHandler
import com.example.gymandroid.model.FullRoutine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SavedRoutinesViewModel(
    private val dbHandler: ExerciseDBHandler
) : ViewModel() {
    private val _savedRoutines = MutableStateFlow<List<FullRoutine>>(emptyList())
    val savedRoutines: StateFlow<List<FullRoutine>> = _savedRoutines

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadSavedRoutines()
    }

    fun loadSavedRoutines() {
        _isLoading.value = true
        viewModelScope.launch {
            _savedRoutines.value = dbHandler.getAllSavedRoutines()
            _isLoading.value = false
        }
    }

    companion object {
        fun Factory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SavedRoutinesViewModel(
                        ExerciseDBHandler.getInstance(context)
                    ) as T
                }
            }
        }
    }
}