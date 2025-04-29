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

@Composable
fun HomeScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.BackgroundColor)
            .padding(8.dp)
    ) {
        items(dummyCategories.size) { categoryIndex ->
            val category = dummyCategories[categoryIndex]

            Text(
                text = category.name,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = AppTheme.TextColor,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(category.exercises.size) { exerciseIndex ->
                    ExerciseCard(category.exercises[exerciseIndex])
                }
            }
        }
    }
}

@Composable
fun ExerciseCard(exercise: Exercise) {
    var liked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .width(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(exercise.imageUrl),
            contentDescription = exercise.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = exercise.title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = AppTheme.TextColor
        )

        Text(
            text = exercise.muscle,
            style = MaterialTheme.typography.bodySmall,
            color = AppTheme.TextColor
        )

        Text(
            text = exercise.repsOrTime,
            style = MaterialTheme.typography.bodySmall,
            color = AppTheme.TextColor
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { liked = !liked }) {
                Icon(
                    imageVector = if (liked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (liked) AppTheme.LikeColor else AppTheme.TextColor
                )
            }
        }
    }
}
