package com.example.gymandroid.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.gymandroid.model.Exercise
import com.example.gymandroid.model.FullRoutine
import com.example.gymandroid.model.FullRoutineDay
import com.example.gymandroid.model.Routine
import com.example.gymandroid.model.RoutineDay
import com.example.gymandroid.model.RoutineDayExercise
import com.example.gymandroid.model.RoutineDayExerciseWithDetails
import com.example.gymandroid.ui.theme.AppTheme
import com.example.gymandroid.viewmodel.HomeViewModel
import com.example.gymandroid.viewmodel.routineViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineScreen(
    navController: NavController,
    routine: FullRoutine
) {
    Scaffold(

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Encabezado de la rutina
            RoutineHeader(routine = routine, navController)

            // Lista de días con ejercicios
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(routine.days) { day ->
                    RoutineDayCard(day = day)
                }
            }
        }
    }
}

@Composable
private fun RoutineHeader(routine: FullRoutine, navController: NavController) {

    val viewModel: routineViewModel = viewModel()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = routine.routine.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = routine.routine.description,
                style = MaterialTheme.typography.bodyMedium
            )


            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoChip(
                    label = "Nivel",
                    value = routine.routine.level
                )
                InfoChip(
                    label = "Duración",
                    value = "${routine.routine.durationWeeks} semanas"
                )
                InfoChip(
                    label = "Frecuencia",
                    value = "${routine.routine.frequencyPerWeek} días/semana"
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { /* Acción ir al perfil */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Entrenador",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ver Entrenador")
                }

                Button(
                    onClick = { if(viewModel.deleteroutine(routine)){
                        navController.navigate("favorites")

                    } },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar rutina",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Eliminar")
                }
            }
        }
    }
}

@Composable
private fun InfoChip(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun RoutineDayCard(day: FullRoutineDay) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                text = day.routineDay.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            day.exercises.sortedBy { it.routineDayExercise.order }.forEach { exercise ->
                ExerciseItem(exercise = exercise)
            }
        }
    }
}

@Composable
private fun ExerciseItem(exercise: RoutineDayExerciseWithDetails) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Imagen del ejercicio
        AsyncImage(
            model = exercise.exercise.imageUrl,
            contentDescription = exercise.exercise.title,
            modifier = Modifier.size(60.dp)
        )

        // Detalles del ejercicio
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = exercise.exercise.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = exercise.exercise.muscle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        // Series y repeticiones
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${exercise.routineDayExercise.sets} x ${exercise.routineDayExercise.reps}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${exercise.routineDayExercise.restSeconds}s descanso",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
    Divider(modifier = Modifier.padding(horizontal = 16.dp))
}

