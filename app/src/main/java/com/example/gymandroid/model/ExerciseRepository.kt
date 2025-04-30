package com.example.gymandroid.model


object ExerciseRepository {
    fun getCategories(): List<Category> = listOf(
        Category(
            name = "Pecho",
            exercises = List(5) { i ->
                Exercise(
                    id = i,
                    title = "Press de Banca $i",
                    muscle = "Pectoral",
                    repsOrTime = "4x12",
                    imageUrl = "https://www.projectcubicle.com/wp-content/uploads/2024/03/SQL-Developers.png"
                )
            }
        ),
        Category(
            name = "Piernas",
            exercises = List(5) { i ->
                Exercise(
                    id = i + 5, // IDs únicos
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
                    id = i + 10, // IDs únicos
                    title = "Peso Muerto $i",
                    muscle = "Espalda baja",
                    repsOrTime = "3x10",
                    imageUrl = "https://via.placeholder.com/300x200"
                )
            }
        )
    )
}