package com.example.gymandroid.view



import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gymandroid.model.Exercise
import com.example.gymandroid.ui.theme.AppTheme
import com.example.gymandroid.viewmodel.HomeViewModel

@Composable
fun ExerciseCard(
    exercise: Exercise,
    isFavorite: Boolean,
    onFavoriteClick: (Exercise) -> Unit,
    navController: NavController,
    isLiked: Boolean = false,
    modifier: Modifier = Modifier
) {
    // Observamos el estado de la animación
    val viewModel: HomeViewModel = viewModel()
    val likedExercise by viewModel.likedExercise.collectAsState()
    val isCurrentlyLiked = likedExercise?.id == exercise.id

    // Animación de like
    val scale by animateFloatAsState(
        targetValue = if (isCurrentlyLiked) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = 0.4f,
            stiffness = Spring.StiffnessLow
        )
    )

    Column(
        modifier = modifier
            .width(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
            .clickable {
                navController.navigate("exercise_detail/${exercise.id}")
            }
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
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = exercise.muscle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )

        Text(
            text = exercise.repsOrTime,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { onFavoriteClick(exercise) },
                modifier = Modifier.scale(scale)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (isFavorite || isLiked) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    }
                )
            }
        }
    }
}