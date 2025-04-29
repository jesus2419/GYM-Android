package com.example.gymandroid.model


// Modelos de datos
data class Trainer(
    val id: Int,
    val name: String,
    val lastName: String,
    val profileImageUrl: String,
    val schedule: String,
    val coverImageUrl: String = "https://via.placeholder.com/800x300",
    val description: String = "Entrenador profesional con 10 años de experiencia",
    val socialLinks: List<SocialLink> = listOf(
        SocialLink("Instagram", "https://instagram.com"),
        SocialLink("Facebook", "https://facebook.com"),
        SocialLink("YouTube", "https://youtube.com")
    )
)

data class SocialLink(
    val platform: String,
    val url: String
)

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
