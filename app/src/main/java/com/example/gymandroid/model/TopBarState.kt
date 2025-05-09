package com.example.gymandroid.model

import androidx.compose.runtime.Composable

sealed class TopBarState(
    val title: String,
    val showBackButton: Boolean = false,
    val actions: @Composable () -> Unit = {}
) {
    object Home : TopBarState(title = "Inicio")
    object Favorites : TopBarState(title = "Favoritos")
    object Trainers : TopBarState(title = "Entrenadores")
    object Info : TopBarState(title = "Información")
    object ExerciseDetail : TopBarState(
        title = "Detalle ejercicio",
        showBackButton = true
    )
    object RoutineDetail : TopBarState(
        title = "Detalle rutina",
        showBackButton = true
    )

    object TrainerDetail : TopBarState(
        title = "Detalle entrenador",
        showBackButton = true
    )
}