package com.example.gymandroid.model


data class Exercise(
    val id: Int,
    val title: String,
    val muscle: String,
    val repsOrTime: String,
    val imageUrl: String
)

data class Category(
    val name: String,
    val exercises: List<Exercise>
)

val dummyCategories = listOf(
    Category(
        name = "Pecho",
        exercises = List(5) { i ->
            Exercise(
                id = i,
                title = "Press de Banca $i",
                muscle = "Pectoral",
                repsOrTime = "4x12",
                imageUrl = "https://via.placeholder.com/300x200"
            )
        }
    ),
    Category(
        name = "Piernas",
        exercises = List(5) { i ->
            Exercise(
                id = i,
                title = "Sentadilla $i",
                muscle = "Cuádriceps",
                repsOrTime = "3x15",
                imageUrl = "https://via.placeholder.com/300x200"
            )
        }
    ),
    Category(
        name = "Espalda",
        exercises = List(5) { i ->
            Exercise(
                id = i,
                title = "Peso Muerto $i",
                muscle = "Espalda baja",
                repsOrTime = "3x10",
                imageUrl = "https://via.placeholder.com/300x200"
            )
        }
    )
)
