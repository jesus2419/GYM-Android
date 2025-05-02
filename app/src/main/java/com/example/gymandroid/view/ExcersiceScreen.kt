package com.example.gymandroid.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.gymandroid.R
import com.example.gymandroid.model.dummyCategories
import com.example.gymandroid.ui.theme.AppTheme

import com.example.gymandroid.viewmodel.HomeViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(
    exerciseId: Int,
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {

    val categories by viewModel.categories.collectAsState()




    val exercise = categories
        .flatMap { it.exercises }
        .firstOrNull { it.id == exerciseId }

    // Si no se encuentra el ejercicio, mostrar error
    if (exercise == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.BackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Text("Ejercicio no encontrado", color = Color.Blue)
            Log.e("ERROR interno", "Error con el objeto seleccionado: $exerciseId")
        }
        return
    }

    val favoriteExercises = viewModel.favoriteExercises.collectAsState().value
    val isFavorite = favoriteExercises.contains(exerciseId)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(exercise.title) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = AppTheme.PrimaryColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.toggleFavorite(exercise) },
                containerColor = AppTheme.LikeColor,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorito"
                )
            }
        },
        containerColor = AppTheme.BackgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Imagen de portada
            /*
            if (exercise.imageUrl == "") {
                Image(
                    painter = rememberAsyncImagePainter(exercise.imageUrl),
                    contentDescription = exercise.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            }

             */
            Image(
                painter = rememberAsyncImagePainter(exercise.imageUrl),
                contentDescription = exercise.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )


            // Información del ejercicio
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = exercise.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = AppTheme.TextColor,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Grupo de información
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                        contentColor = AppTheme.TextColor
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Grupo Músculo
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = "Músculo",
                                tint = AppTheme.PrimaryColor,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Músculo principal",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = AppTheme.TextColor.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = exercise.muscle,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = AppTheme.TextColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Grupo Series/Reps
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Repeat,
                                contentDescription = "Repeticiones",
                                tint = AppTheme.PrimaryColor,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Series y repeticiones",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = AppTheme.TextColor.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = exercise.repsOrTime,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = AppTheme.TextColor
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Descripción (puedes añadir este campo a tu data class si lo necesitas)
                Text(
                    text = "Descripción detallada del ejercicio...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextColor,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

