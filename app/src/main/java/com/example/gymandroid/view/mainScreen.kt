package com.example.gymandroid.view

import FavoritesScreen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp


import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gymandroid.model.TopBarState
import com.example.gymandroid.model.Trainer
import com.example.gymandroid.model.dummyTrainers
import com.example.gymandroid.ui.theme.AppTheme
import com.example.gymandroid.viewmodel.HomeViewModel
import com.example.gymandroid.viewmodel.mainViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(0) }
    val viewModel: mainViewModel = viewModel(factory = mainViewModel.Factory)
    val scope = rememberCoroutineScope()


    // Obtener la ruta actual
    val currentRoute by navController.currentBackStackEntryAsState()
    val currentRouteName = currentRoute?.destination?.route

    // Estado para el entrenador seleccionado
    var currentTrainer by remember { mutableStateOf<Trainer?>(null) }

    // Determinar el estado del TopBar basado en la ruta
    val topBarState = remember(currentRouteName) {
        when {
            currentRouteName == BottomNavItem.Home.route -> TopBarState.Home
            currentRouteName == BottomNavItem.Favorites.route -> TopBarState.Favorites
            currentRouteName == BottomNavItem.Trainers.route -> TopBarState.Trainers
            currentRouteName == BottomNavItem.Info.route -> TopBarState.Info
            currentRouteName?.startsWith("exercise_detail") == true -> TopBarState.ExerciseDetail
            currentRouteName?.startsWith("routine_detail") == true -> TopBarState.RoutineDetail
            currentRouteName?.startsWith("trainerDetails") == true -> TopBarState.TrainerDetail

            else -> TopBarState.Home
        }
    }

    val screens = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorites,
        BottomNavItem.Trainers,
        BottomNavItem.Info
    )

    ModalNavigationDrawer(
        drawerState = viewModel.drawerState,
        drawerContent = {
            DrawerContent(
                onClose = {
                    scope.launch { viewModel.drawerState.close() }
                }
            )
        }
    ) {

        Scaffold(
            topBar = {
                DynamicTopBar(
                    state = topBarState,
                    onBackClick = { navController.popBackStack() },
                    onMenuClick = { scope.launch { viewModel.drawerState.open() } }
                )
            },
            containerColor = AppTheme.BackgroundColor,

            bottomBar = {
                val selectedColor = MaterialTheme.colorScheme.onPrimary
                val unselectedColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)

                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = selectedColor
                ) {
                    screens.forEachIndexed { index, screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, screen.title, tint = if (selectedItem == index) selectedColor else unselectedColor) },
                            label = { Text(screen.title, color = if (selectedItem == index) selectedColor else unselectedColor) },
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
                                navController.popBackStack()
                                navController.navigate(screen.route)
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = selectedColor,
                                selectedTextColor = selectedColor,
                                unselectedIconColor = unselectedColor,
                                unselectedTextColor = unselectedColor,
                                indicatorColor = MaterialTheme.colorScheme.secondary
                            )
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


                composable(BottomNavItem.Trainers.route) {
                    TrainersScreen(
                        onTrainerClick = { trainerId ->
                            navController.navigate("trainerDetails/$trainerId")
                        }
                    )
                }



                composable("trainerDetails/{trainerId}") { backStackEntry ->
                    val trainerId = backStackEntry.arguments?.getString("trainerId")?.toIntOrNull() ?: 0
                    TrainerDetailScreen(trainerId = trainerId, navController)
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

                composable(
                    route = "routine_detail/{routineId}",
                    arguments = listOf(navArgument("routineId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val routineId = backStackEntry.arguments?.getInt("routineId") ?: 0
                    val viewModel: HomeViewModel = viewModel()

                    // Cargar la rutina cuando se entra a la pantalla
                    LaunchedEffect(routineId) {
                        viewModel.getRoutineById(routineId)
                    }

                    // Observar cambios en la rutina seleccionada
                    val routine by viewModel.selectedRoutine.collectAsState()

                    if (routine != null) {
                        RoutineScreen(
                            navController = navController,
                            routine = routine!!
                        )
                    } else {
                        // Muestra un mensaje de carga o error
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Cargando rutina...")
                        }
                    }
                }

            }
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