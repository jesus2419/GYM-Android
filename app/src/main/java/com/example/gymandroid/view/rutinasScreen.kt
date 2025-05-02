package com.example.gymandroid.view

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.gymandroid.model.Category
import com.example.gymandroid.model.Exercise


@Composable
fun RoutineListScreen(routines: List<FullRoutine>, navController1: NavController) {
    var selectedRoutine by remember { mutableStateOf<FullRoutine?>(null) }

    if (selectedRoutine == null) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            items(routines) { routine ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { selectedRoutine = routine },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(routine.routine.name, style = MaterialTheme.typography.titleLarge)
                        Text(routine.routine.objective, style = MaterialTheme.typography.bodyMedium)
                        Text("Nivel: ${routine.routine.level}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    } else {
        RoutineDetailScreen(routine = selectedRoutine!!, onBack = { selectedRoutine = null }, navController1)
    }
}

@Composable
fun RoutineDetailScreen(routine: FullRoutine, onBack: () -> Unit, navController1: NavController) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(routine.routine.name, style = MaterialTheme.typography.headlineMedium)
        Text(routine.routine.description, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(8.dp))
        Button(onClick = onBack) {
            Text("Volver")
        }

        Spacer(Modifier.height(16.dp))
        LazyColumn {
            items(routine.days) { day ->
                Text(day.routineDay.name, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 8.dp))
                day.exercises.sortedBy { it.routineDayExercise.order }.forEach { e ->

                    ExerciseItem1(e.exercise, navController1)

                    /*

                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(e.exercise.title, style = MaterialTheme.typography.bodyLarge)
                            Text("Musculo: ${e.exercise.muscle}", style = MaterialTheme.typography.bodySmall)
                            Text("Series: ${e.routineDayExercise.sets} x ${e.routineDayExercise.reps}", style = MaterialTheme.typography.bodySmall)
                            Text("Descanso: ${e.routineDayExercise.restSeconds} segundos", style = MaterialTheme.typography.bodySmall)
                        }
                    }

                     */
                }
            }
        }
    }
}


@Composable
fun ExerciseItem1(exercise: Exercise, navController1: NavController) {
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

