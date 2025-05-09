package com.example.gymandroid.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymandroid.model.FullRoutine
import com.example.gymandroid.model.dummyRoutines
import kotlinx.coroutines.launch
class TrainerViewModel : ViewModel() {
    private val _trainerRoutines = mutableStateOf<List<FullRoutine>>(emptyList())
    private val _isLoading = mutableStateOf(false)

    val trainerRoutines: State<List<FullRoutine>> = _trainerRoutines
    val isLoading: State<Boolean> = _isLoading

    fun loadTrainerRoutines(trainerId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _trainerRoutines.value = dummyRoutines.filter { it.routine.id_trainer == trainerId }
            } finally {
                _isLoading.value = false
            }
        }
    }
}