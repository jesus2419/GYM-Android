package com.example.gymandroid.view

import androidx.compose.ui.graphics.Color
import com.example.gymandroid.model.Exercise
import com.example.gymandroid.model.dummyCategories
import com.example.gymandroid.ui.theme.AppTheme


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymandroid.viewmodel.HomeViewModel


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val categories by viewModel.categories.collectAsState()
    val favorites by viewModel.favoriteExercises.collectAsState()
    val likedExercise by viewModel.likedExercise.collectAsState()




    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.BackgroundColor)
            .padding(8.dp)
    ) {
        items(categories.size) { categoryIndex ->
            val category = categories[categoryIndex]

            Text(
                text = category.name,
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(category.exercises.size) { exerciseIndex ->
                    val exercise = category.exercises[exerciseIndex]
                    ExerciseCard(
                        exercise = exercise,
                        isFavorite = favorites.contains(exercise.id),
                        onFavoriteClick = { viewModel.toggleFavorite(it) },
                        isLiked = likedExercise?.id == exercise.id
                    )
                }
            }
        }
    }
}



