import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gymandroid.R
import com.example.gymandroid.model.Exercise
import com.example.gymandroid.model.ExerciseDBHandler
import com.example.gymandroid.model.FullRoutine
import com.example.gymandroid.ui.theme.AppTheme
import com.example.gymandroid.view.ExerciseCard
import com.example.gymandroid.viewmodel.FavoritesViewModel
import com.example.gymandroid.viewmodel.SavedRoutinesViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    context: Context = LocalContext.current,
    favoritesViewModel: FavoritesViewModel = viewModel(factory = FavoritesViewModel.Factory),
    routinesViewModel: SavedRoutinesViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SavedRoutinesViewModel(
                    ExerciseDBHandler.getInstance(context)
                ) as T
            }
        }
    )
) {
    val favorites by favoritesViewModel.favorites.collectAsState()
    val isLoadingFavorites by favoritesViewModel.isLoading.collectAsState()

    val routines by routinesViewModel.savedRoutines.collectAsState()
    val isLoadingRoutines by routinesViewModel.isLoading.collectAsState()

    val tabs = listOf("Ejercicios Favoritos", "Rutinas Guardadas")
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(

    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // Pestañas - Versión corregida
            val tabs = listOf("Ejercicios Favoritos", "Rutinas Guardadas")
            var selectedTabIndex by remember { mutableStateOf(0) }
            // Pestañas
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            // Contenido de las pestañas
            when (selectedTabIndex) {
                0 -> FavoritesTabContent(
                    isLoading = favoritesViewModel.isLoading.collectAsState().value,
                    favorites = favoritesViewModel.favorites.collectAsState().value,
                    navController = navController,
                    onRemoveFavorite = { favoritesViewModel.removeFavorite(it) }
                )
                1 -> RoutinesTabContent(
                    isLoading = routinesViewModel.isLoading.collectAsState().value,
                    routines = routinesViewModel.savedRoutines.collectAsState().value,
                    navController = navController
                )
            }
        }
    }
}

@Composable
private fun FavoritesTabContent(
    isLoading: Boolean,
    favorites: List<Exercise>,
    navController: NavController,
    onRemoveFavorite: (Exercise) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp),
                color = AppTheme.PrimaryColor
            )
        } else {
            if (favorites.isEmpty()) {
                EmptyContentView(
                    title = "No tienes ejercicios favoritos",
                    message = "Presiona el corazón en los ejercicios para guardarlos aquí"
                )
            } else {
                FavoritesList(
                    navController = navController,
                    exercises = favorites,
                    onRemoveFavorite = onRemoveFavorite
                )
            }
        }
    }
}

@Composable
private fun RoutinesTabContent(
    isLoading: Boolean,
    routines: List<FullRoutine>,
    navController: NavController
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp),
                color = AppTheme.PrimaryColor
            )
        } else {
            if (routines.isEmpty()) {
                EmptyContentView(
                    title = "No tienes rutinas guardadas",
                    message = "Guarda rutinas completas desde la sección de rutinas"
                )
            } else {
                SavedRoutinesList(
                    navController = navController,
                    routines = routines
                )
            }
        }
    }
}

@Composable
private fun FavoritesList(
    navController: NavController,
    exercises: List<Exercise>,
    onRemoveFavorite: (Exercise) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(exercises, key = { it.id }) { exercise ->
            ExerciseCard(
                exercise = exercise,
                isFavorite = true,
                navController = navController,
                onFavoriteClick = { onRemoveFavorite(exercise) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SavedRoutinesList(
    navController: NavController,
    routines: List<FullRoutine>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(routines, key = { it.routine.id }) { routine ->
            RoutineCard(
                routine = routine,
                onClick = {
                    navController.navigate("routine_detail/${routine.routine.id}")
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun RoutineCard(
    routine: FullRoutine,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = routine.routine.name,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = routine.routine.objective,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Días: ${routine.days.size} • Duración: ${routine.routine.durationWeeks} semanas",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun EmptyContentView(title: String, message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = AppTheme.TextColor
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.TextColor.copy(alpha = 0.7f)
        )
    }
}