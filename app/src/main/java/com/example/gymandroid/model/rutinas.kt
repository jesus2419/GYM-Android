package com.example.gymandroid.model

data class Routine(
    val id: Int,
    val name: String,
    val objective: String,           // Ej: "Hipertrofia", "Pérdida de grasa"
    val level: String,               // Ej: "Principiante", "Intermedio"
    val durationWeeks: Int,
    val frequencyPerWeek: Int,       // Días por semana
    val description: String
)

data class RoutineDay(
    val id: Int,
    val routineId: Int,              // Relación con Routine
    val dayOfWeek: Int?,            // 1=Lunes, ..., 7=Domingo (null si es flexible)
    val name: String,               // Ej: "Pecho y Tríceps"
    val order: Int                  // Orden dentro de la rutina
)

data class RoutineDayExercise(
    val id: Int,
    val routineDayId: Int,          // Relación con RoutineDay
    val exerciseId: Int,            // Relación con Exercise
    val sets: Int,
    val reps: Int,
    val restSeconds: Int,
    val order: Int                  // Orden del ejercicio en el día
)



data class FullRoutine(val routine: Routine, val days: List<FullRoutineDay>)
data class FullRoutineDay(val routineDay: RoutineDay, val exercises: List<RoutineDayExerciseWithDetails>)
data class RoutineDayExerciseWithDetails(val routineDayExercise: RoutineDayExercise, val exercise: Exercise)


val dummyRoutines = listOf(
    FullRoutine(
        routine = Routine(1, "Hipertrofia 3 días", "Ganar masa muscular", "Intermedio", 6, 3, "Rutina para hipertrofia dividida en tren superior e inferior"),
        days = listOf(
            FullRoutineDay(
                routineDay = RoutineDay(1, 1, 1, "Pecho y Tríceps", 1),
                exercises = listOf(
                    RoutineDayExerciseWithDetails(
                        routineDayExercise = RoutineDayExercise(1, 1, 1, 4, 12, 60, 1),
                        exercise = Exercise(20, "Press de banca", "Pecho", "4x12", "", "Ejercicio básico para el pecho.")
                    ),
                    RoutineDayExerciseWithDetails(
                        routineDayExercise = RoutineDayExercise(2, 1, 2, 3, 10, 60, 2),
                        exercise = Exercise(21, "Fondos", "Tríceps", "3x10", "", "Enfocado en tríceps y pecho.")
                    )
                )
            ),
            FullRoutineDay(
                routineDay = RoutineDay(2, 1, 3, "Piernas", 2),
                exercises = listOf(
                    RoutineDayExerciseWithDetails(
                        routineDayExercise = RoutineDayExercise(3, 2, 3, 4, 8, 90, 1),
                        exercise = Exercise(22, "Sentadilla", "Piernas", "4x8", "", "Ejercicio compuesto para tren inferior.")
                    )
                )
            )
        )
    )
)

