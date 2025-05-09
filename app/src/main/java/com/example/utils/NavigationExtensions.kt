package com.example.utils

import androidx.compose.runtime.*
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.compose.runtime.State

@Composable
fun NavController.currentBackStackEntryAsState(): State<NavBackStackEntry?> {
    val currentEntry = remember { mutableStateOf(currentBackStackEntry) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, _, _ ->
            currentEntry.value = currentBackStackEntry
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return currentEntry
}