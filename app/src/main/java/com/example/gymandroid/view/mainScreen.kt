package com.example.gymandroid.view

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.unit.dp


import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

sealed class BottomNavItem(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : BottomNavItem("Inicio", Icons.Filled.Home)
    object Favorites : BottomNavItem("Favoritos", Icons.Filled.Star)
    object Trainers : BottomNavItem("Entrenadores", Icons.Filled.Person)
    object Info : BottomNavItem("Información", Icons.Filled.Info)
}

@Composable
fun MainScreen() {
    var selectedItem by remember { mutableStateOf(0) }
    val screens = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorites,
        BottomNavItem.Trainers,
        BottomNavItem.Info
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                screens.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        when (selectedItem) {
            0 -> HomeScreen()
            1 -> PlaceholderScreen("Favoritos")
            2 -> PlaceholderScreen("Entrenadores")
            3 -> PlaceholderScreen("Información")
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(text = title)
    }
}
