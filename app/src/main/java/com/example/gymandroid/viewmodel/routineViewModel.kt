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


class routineViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHandler = ExerciseDBHandler(application)





    fun deleteroutine(routine: FullRoutine) : Boolean   {
        try {
            dbHandler.deleteRoutineById(routine.routine.id)
            Log.d("RoutineViewModel", "Routine deleted successfully: ${routine.routine.id}")
            return true
        } catch (e: Exception) {
            Log.e("RoutineViewModel", "Error deleting routine: ${routine.routine.id}", e)
        }
        return false

    }









}