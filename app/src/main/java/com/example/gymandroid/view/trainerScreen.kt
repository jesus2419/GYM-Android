package com.example.gymandroid.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.gymandroid.model.Trainer
import com.example.gymandroid.model.dummyTrainers
import androidx.compose.foundation.lazy.items


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainersScreen(onTrainerClick: (Int) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Entrenadores") },
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(dummyTrainers, key = { it.id }) { trainer ->
                TrainerItem(
                    trainer = trainer,
                    onClick = { onTrainerClick(trainer.id) }
                )
            }
        }

    }
}

@Composable
fun TrainerItem(trainer: Trainer, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = trainer.profileImageUrl,
                contentDescription = "Foto de ${trainer.name}",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "${trainer.name} ${trainer.lastName}",
                )
                Text(
                    text = trainer.schedule,

                )
            }
        }
    }
}