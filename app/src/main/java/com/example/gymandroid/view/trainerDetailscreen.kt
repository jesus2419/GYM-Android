package com.example.gymandroid.view


import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.gymandroid.model.Category
import com.example.gymandroid.model.Exercise
import com.example.gymandroid.model.SocialLink
import com.example.gymandroid.model.Trainer
import com.example.gymandroid.model.dummyRoutines
import com.example.gymandroid.model.dummyTrainers
import com.example.gymandroid.viewmodel.TrainerViewModel

// Para mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// Para StateFlow
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainerDetailScreen(trainerId: Int, navController1: NavController) {
    val trainer = dummyTrainers.first { it.id == trainerId }
    val tabTitles = listOf("Perfil", "Rutinas")
    var selectedTabIndex by remember { mutableStateOf(0) }

    val viewModel: TrainerViewModel = viewModel()




    // Load routines when the screen is first displayed
    LaunchedEffect(trainerId) {
        viewModel.loadTrainerRoutines(trainerId)
    }

    // Para mutableStateOf
    val trainerRoutines by viewModel.trainerRoutines
    val isLoading by viewModel.isLoading


    Scaffold(
        topBar = {} // Dejamos vacío porque lo maneja el DynamicTopBar
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Contenedor para las imágenes (portada + perfil superpuesto)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Altura total del contenedor
            ) {
                // Imagen de portada
                AsyncImage(
                    model = trainer.coverImageUrl,
                    contentDescription = "Portada de ${trainer.name}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )

                // Imagen de perfil superpuesta
                AsyncImage(
                    model = trainer.profileImageUrl,
                    contentDescription = "Foto de ${trainer.name}",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                        .align(Alignment.BottomCenter), // Alineamos arriba del Box contenedor
                    contentScale = ContentScale.Crop
                )
            }

            // Tabs
            TabRow(selectedTabIndex) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            // Contenido de las tabs
            when (selectedTabIndex) {
                0 -> ProfileTabContent(trainer)
                1 -> Box(modifier = Modifier.weight(1f)) {
                    when {
                        isLoading -> LoadingView()
                        trainerRoutines.isEmpty() -> EmptyView(message = "Este entrenador no tiene rutinas")
                        else -> RoutineListScreen(routines = trainerRoutines, navController1)
                    }
                }
                /*
                1 -> Box(modifier = Modifier.weight(1f)) {
                    RoutineListScreen(routines = dummyRoutines, navController1)

                }

                 */
            }
        }
    }
}

@Composable
fun ProfileTabContent(trainer: Trainer) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${trainer.name} ${trainer.lastName}",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = trainer.schedule
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = trainer.description,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Redes Sociales",
            modifier = Modifier.align(Alignment.Start)
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        Column {
            trainer.socialLinks.forEach { link ->
                SocialLinkItem(link)
            }
        }
    }
}

@Composable
fun SocialLinkItem(link: SocialLink) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { /* Open link */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = when (link.platform) {
                "Instagram" -> Icons.Default.Person
                "Facebook" -> Icons.Default.Facebook
                "YouTube" -> Icons.Default.PlayArrow
                else -> Icons.Default.Link
            },
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(text = link.platform)
    }
}


@Composable
fun CategoryItem(category: Category, navController1: NavController) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),

    ) {
        Column {
            Text(
                text = category.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            )

            Column {
                category.exercises.forEach { exercise ->



                    ExerciseItem(exercise, navController1)
                    if (exercise != category.exercises.last()) {
                        Divider()
                    }


                }
            }
        }
    }
}

@Composable
fun ExerciseItem(exercise: Exercise, navController1: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable{ navController1.navigate("exercise_detail/${exercise.id}")},
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = exercise.imageUrl,
            contentDescription = exercise.title,
            modifier = Modifier.size(80.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = exercise.title,
            )
            Text(
                text = exercise.muscle,

            )
        }

        Text(
            text = exercise.repsOrTime,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LoadingView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text("Cargando rutinas...")
    }
}

@Composable
fun EmptyView(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Vacío",
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}