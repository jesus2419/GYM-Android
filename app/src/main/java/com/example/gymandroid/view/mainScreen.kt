package com.example.gymandroid.view

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.unit.dp


import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gymandroid.ui.theme.AppTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(0) }

    val screens = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorites,
        BottomNavItem.Trainers,
        BottomNavItem.Info
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "si") },

                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = AppTheme.PrimaryColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = AppTheme.BackgroundColor,

        bottomBar = {
            NavigationBar {
                screens.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            // Navegación a las pantallas principales
                            navController.popBackStack()
                            navController.navigate(screen.route)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Pantalla de Inicio
            composable(BottomNavItem.Home.route) {
                HomeScreen(navController)
            }

            // Pantalla de Favoritos
            composable(BottomNavItem.Favorites.route) {
                FavoritesScreen(navController)
            }

            // Pantalla de Entrenadores con su propia navegación interna
            composable(BottomNavItem.Trainers.route) {
                TrainersNavHost() // Nuevo NavHost anidado para entrenadores
            }

            // Pantalla de Información
            composable(BottomNavItem.Info.route) {
                var selectedDate by remember { mutableStateOf(LocalDate.now()) }

                CalendarView(
                    selectedDate = selectedDate,
                    onDateSelected = { newDate -> selectedDate = newDate })
            }


            composable(
                route = "exercise_detail/{exerciseId}",
                arguments = listOf(navArgument("exerciseId") {
                    type = NavType.IntType
                })
            ) { backStackEntry ->
                val exerciseId = backStackEntry.arguments?.getInt("exerciseId") ?: -1
                ExerciseDetailScreen(
                    exerciseId = exerciseId,
                    navController = navController
                )
            }

        }
    }
}

@Composable
fun PlaceholderScreen(x0: String) {

}

// Nuevo NavHost específico para la sección de entrenadores
@Composable
fun TrainersNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "trainersList"
    ) {
        composable("trainersList") {
            TrainersScreen(
                onTrainerClick = { trainerId ->
                    navController.navigate("trainerDetails/$trainerId")
                }
            )
        }

        composable("trainerDetails/{trainerId}") { backStackEntry ->
            val trainerId = backStackEntry.arguments?.getString("trainerId")?.toIntOrNull() ?: 0
            TrainerDetailScreen(trainerId = trainerId)
        }
    }
}

// Actualiza tu data class BottomNavItem para incluir rutas
sealed class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    object Home : BottomNavItem("Inicio", Icons.Default.Home, "home")
    object Favorites : BottomNavItem("Favoritos", Icons.Default.Favorite, "favorites")
    object Trainers : BottomNavItem("Entrenadores", Icons.Default.Person, "trainers")
    object Info : BottomNavItem("Info", Icons.Default.Info, "info")
}