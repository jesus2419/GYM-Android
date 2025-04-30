package com.example.gymandroid.view


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gymandroid.R
import com.example.gymandroid.model.Exercise
import com.example.gymandroid.ui.theme.AppTheme
import com.example.gymandroid.viewmodel.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(

    viewModel: FavoritesViewModel = viewModel(factory = FavoritesViewModel.Factory)
) {
    val favorites by viewModel.favorites.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(

    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp),
                    color = AppTheme.PrimaryColor
                )
            } else {
                if (favorites.isEmpty()) {
                    EmptyFavoritesView()
                } else {
                    FavoritesList(
                        exercises = favorites,
                        onRemoveFavorite = { viewModel.removeFavorite(it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoritesList(
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
                isFavorite = true, // Siempre true porque estamos en favoritos
                onFavoriteClick = { onRemoveFavorite(exercise) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun EmptyFavoritesView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No tienes ejercicios favoritos",
            style = MaterialTheme.typography.titleMedium,
            color = AppTheme.TextColor
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Presiona el corazón en los ejercicios para guardarlos aquí",
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.TextColor.copy(alpha = 0.7f)
        )
    }
}