package com.example.gymandroid.view

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gymandroid.model.FullRoutine

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.gymandroid.model.Exercise
import com.example.gymandroid.model.ExerciseDBHandler
import com.example.gymandroid.model.FullRoutineDay
import com.example.gymandroid.model.Routine
import com.example.gymandroid.model.RoutineDay
import com.example.gymandroid.model.RoutineDayExercise
import com.example.gymandroid.model.RoutineDayExerciseWithDetails
import com.example.gymandroid.ui.theme.GymTheme
import kotlinx.coroutines.delay


@Composable
fun RoutineListScreen(
    routines: List<FullRoutine>,
    navController: NavController,
    context: Context = LocalContext.current
) {
    var selectedRoutine by remember { mutableStateOf<FullRoutine?>(null) }

    if (selectedRoutine == null) {
        RoutineListContent(routines = routines, onRoutineSelected = { selectedRoutine = it })
    } else {
        RoutineDetailScreen(
            routine = selectedRoutine!!,
            onBack = { selectedRoutine = null },
            navController = navController,
            context = context
        )
    }
}

@Composable
private fun RoutineListContent(
    routines: List<FullRoutine>,
    onRoutineSelected: (FullRoutine) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)
        .background(MaterialTheme.colorScheme.background)
    ) {
        items(routines) { routine ->
            RoutineCard(routine = routine, onClick = { onRoutineSelected(routine) })
        }
    }
}

@Composable
private fun RoutineCard(routine: FullRoutine, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = routine.routine.name,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = routine.routine.objective,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Nivel: ${routine.routine.level}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Duración: ${routine.routine.durationWeeks} semanas",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Frecuencia: ${routine.routine.frequencyPerWeek} días/semana",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun RoutineDetailScreen(
    routine: FullRoutine,
    onBack: () -> Unit,
    navController: NavController,
    context: Context
) {
    val dbHandler = remember { ExerciseDBHandler.getInstance(context) }
    var showSaveSuccess by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).background(MaterialTheme.colorScheme.surface)
    ) {
        // Header de la rutina
        Text(
            text = routine.routine.name,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,

            )
        Text(
            text = routine.routine.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 4.dp),
            color = MaterialTheme.colorScheme.onSurface,

            )

        Spacer(modifier = Modifier.height(8.dp))

        // Botones de acción
        ActionButtons(
            onBack = onBack,
            onSave = {
                when (dbHandler.saveFullRoutine(routine)) {
                    is ExerciseDBHandler.RoutineSaveResult.Success -> {
                        showSaveSuccess = true
                    }
                    is ExerciseDBHandler.RoutineSaveResult.Error -> {
                        // Podrías mostrar un mensaje de error aquí
                    }
                }
            }
        )

        // Mensaje de éxito
        if (showSaveSuccess) {
            SaveSuccessMessage(onDismiss = { showSaveSuccess = false })
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de días y ejercicios
        RoutineDaysList(routine.days, navController)
    }
}

@Composable
private fun ActionButtons(onBack: () -> Unit, onSave: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = onBack) {
            Text("Volver")
        }

        Button(
            onClick = onSave,

        ) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = "Guardar rutina",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Guardar Rutina")
        }
    }
}

@Composable
private fun SaveSuccessMessage(onDismiss: () -> Unit) {
    Text(
        text = "¡Rutina guardada en tu biblioteca!",
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(8.dp)
    )

    LaunchedEffect(Unit) {
        delay(2000)
        onDismiss()
    }
}

@Composable
private fun RoutineDaysList(days: List<FullRoutineDay>, navController: NavController) {
    LazyColumn {
        items(days) { day ->
            Column {
                Text(
                    text = day.routineDay.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                day.exercises.sortedBy { it.routineDayExercise.order }.forEach { exercise ->
                    ExerciseItem(
                        exercise = exercise,
                        navController = navController
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun ExerciseItem(
    exercise: RoutineDayExerciseWithDetails,
    navController: NavController
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .background(MaterialTheme.colorScheme.surface)
                .clickable {
                    navController.navigate("exercise_detail/${exercise.exercise.id}")
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = exercise.exercise.imageUrl,
                contentDescription = exercise.exercise.title,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exercise.exercise.title,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = exercise.exercise.muscle,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "${exercise.routineDayExercise.sets} x ${exercise.routineDayExercise.reps} repeticiones",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Descanso: ${exercise.routineDayExercise.restSeconds} segundos",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

