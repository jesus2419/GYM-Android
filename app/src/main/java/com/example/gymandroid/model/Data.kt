package com.example.gymandroid.model

import java.time.LocalDate


// Modelos de datos
data class Trainer(
    val id: Int,
    val name: String,
    val lastName: String,
    val profileImageUrl: String,
    val schedule: String,
    val coverImageUrl: String = "https://static.filehorse.com/screenshots/developer-tools/oracle-sql-developer-screenshot-02.png",
    val description: String = "Entrenador profesional con 10 años de experiencia",
    val socialLinks: List<SocialLink> = listOf(
        SocialLink("Instagram", "https://instagram.com"),
        SocialLink("Facebook", "https://facebook.com"),
        SocialLink("YouTube", "https://youtube.com")
    )
)


data class payday(
    val id: Int,
    val date: LocalDate,          // Fecha de pago
    val isSelected: Boolean = false // Para resaltar la fecha seleccionada

)

val dummypayday = listOf(
    payday(
        id = 1,
        date = LocalDate.now()
    ))

data class SocialLink(
    val platform: String,
    val url: String
)

data class Exercise(
    val id: Int,
    val title: String,
    val muscle: String,
    val repsOrTime: String,
    val imageUrl: String = "",
    val description: String = "Descripción detallada del ejercicio..."
)
data class Category(
    val name: String,
    val exercises: List<Exercise>
)

/*
val dummyCategories = listOf(
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


 */



val dummyCategories = listOf(
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
                id = i,
                title = "Sentadilla $i",
                muscle = "Cuádriceps",
                repsOrTime = "3x15"

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
                repsOrTime = "3x10"
            )
        }
    )
)





// Datos de ejemplo
val dummyTrainers = listOf(
    Trainer(
        id = 1,
        name = "Carlos",
        lastName = "Gómez",
        profileImageUrl = "https://randomuser.me/api/portraits/men/1.jpg",
        schedule = "Lunes a Viernes: 8:00 - 18:00"
    ),
    Trainer(
        id = 2,
        name = "Ana",
        lastName = "Martínez",
        profileImageUrl = "https://randomuser.me/api/portraits/women/1.jpg",
        schedule = "Martes a Sábado: 7:00 - 15:00"
    ),
    Trainer(
        id = 3,
        name = "Luis",
        lastName = "Rodríguez",
        profileImageUrl = "https://randomuser.me/api/portraits/men/2.jpg",
        schedule = "Lunes a Jueves: 10:00 - 20:00"
    )
)



