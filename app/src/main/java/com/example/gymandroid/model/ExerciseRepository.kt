package com.example.gymandroid.model


object ExerciseRepository {
    fun getExerciseById(id: Int): Exercise? {
        return getCategories().flatMap { it.exercises }.firstOrNull { it.id == id }
    }
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

    fun getCategories2(): List<Category> = listOf(
        Category(
            name = "Pecho",
            exercises = listOf(
                Exercise(
                    id = 1,
                    title = "Press de Banca 1",
                    muscle = "Pectoral",
                    repsOrTime = "4x12",
                    imageUrl = "https://www.projectcubicle.com/wp-content/uploads/2024/03/SQL-Developers.png"
                ),
                Exercise(
                    id = 2,
                    title = "Press de Banca 2",
                    muscle = "Pectoral",
                    repsOrTime = "4x12",
                    imageUrl = "https://www.projectcubicle.com/wp-content/uploads/2024/03/SQL-Developers.png"
                ),
                Exercise(
                    id = 3,
                    title = "Press de Banca 3",
                    muscle = "Pectoral",
                    repsOrTime = "4x12",
                    imageUrl = "https://www.projectcubicle.com/wp-content/uploads/2024/03/SQL-Developers.png"
                )
            )

        ),
        Category(
            name = "Piernas",
            exercises = listOf(
                Exercise(
                    id = 4,
                    title = "Sentadilla 4",
                    muscle = "Cuádriceps",
                    repsOrTime = "3x15"

                ),
                Exercise(
                    id = 5,
                    title = "Sentadilla 5",
                    muscle = "Cuádriceps",
                    repsOrTime = "3x15"

                ),
                Exercise(
                    id = 6,
                    title = "Sentadilla 6",
                    muscle = "Cuádriceps",
                    repsOrTime = "3x15"

                )

            )
        ),
        Category(
            name = "Espalda",
            exercises = listOf(
                Exercise(
                    id = 7,
                    title = "Peso Muerto 7",
                    muscle = "Espalda baja",
                    repsOrTime = "3x10"
                ),
                Exercise(
                    id = 8,
                    title = "Peso Muerto 8",
                    muscle = "Espalda baja",
                    repsOrTime = "3x10"
                ),
                Exercise(
                    id = 9,
                    title = "Peso Muerto 9",
                    muscle = "Espalda baja",
                    repsOrTime = "3x10"
                )
            )
        )
    )

}