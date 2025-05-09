package com.example.gymandroid.model

data class Routine(
    val id: Int,
    val id_trainer: Int,
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
        routine = Routine(1, 1, "Hipertrofia 3 días", "Ganar masa muscular", "Intermedio", 6, 3, "Rutina para hipertrofia dividida en tren superior e inferior"),
        days = listOf(
            FullRoutineDay(
                routineDay = RoutineDay(1, 1, 1, "Pecho y Tríceps", 1),
                exercises = listOf(
                    RoutineDayExerciseWithDetails(
                        routineDayExercise = RoutineDayExercise(1, 1, 1, 4, 12, 60, 1),
                        exercise = ExerciseRepository.getCategories()[0].exercises[0] // Press de Banca 0
                    ),
                    RoutineDayExerciseWithDetails(
                        routineDayExercise = RoutineDayExercise(2, 1, 2, 3, 10, 60, 2),
                        exercise = ExerciseRepository.getCategories()[0].exercises[1] // Press de Banca 1
                    )
                )
            ),
            FullRoutineDay(
                routineDay = RoutineDay(2, 1, 3, "Piernas", 2),
                exercises = listOf(
                    RoutineDayExerciseWithDetails(
                        routineDayExercise = RoutineDayExercise(3, 2, 3, 4, 8, 90, 1),
                        exercise = ExerciseRepository.getCategories()[1].exercises[0] // Sentadilla 0
                    )
                )
            )
        )
    ),
    FullRoutine(
        routine = Routine(2, 2, "Fuerza 4 días", "Aumentar fuerza máxima", "Avanzado", 8, 4, "Rutina de fuerza con progresión lineal"),
        days = listOf(
            FullRoutineDay(
                routineDay = RoutineDay(3, 2, 1, "Press y Hombros", 1),
                exercises = listOf(
                    RoutineDayExerciseWithDetails(
                        routineDayExercise = RoutineDayExercise(4, 3, 4, 5, 5, 120, 1),
                        exercise = ExerciseRepository.getCategories()[0].exercises[2] // Press de Banca 2
                    ),
                    RoutineDayExerciseWithDetails(
                        routineDayExercise = RoutineDayExercise(5, 3, 5, 4, 6, 90, 2),
                        exercise = ExerciseRepository.getCategories()[0].exercises[3] // Press de Banca 3
                    )
                )
            ),
            FullRoutineDay(
                routineDay = RoutineDay(4, 2, 2, "Dominadas y Remo", 2),
                exercises = listOf(
                    RoutineDayExerciseWithDetails(
                        routineDayExercise = RoutineDayExercise(6, 4, 6, 4, 8, 75, 1),
                        exercise = ExerciseRepository.getCategories()[2].exercises[0] // Peso Muerto 0
                    )
                )
            )
        )
    ),
    FullRoutine(
        routine = Routine(3, 3, "Full Body Principiante", "Aprendizaje de patrones", "Principiante", 4, 3, "Rutina completa para aprender los ejercicios básicos"),
        days = listOf(
            FullRoutineDay(
                routineDay = RoutineDay(5, 3, 1, "Full Body A", 1),
                exercises = listOf(
                    RoutineDayExerciseWithDetails(
                        routineDayExercise = RoutineDayExercise(7, 5, 7, 3, 10, 60, 1),
                        exercise = ExerciseRepository.getCategories()[2].exercises[1] // Peso Muerto 1
                    ),
                    RoutineDayExerciseWithDetails(
                        routineDayExercise = RoutineDayExercise(8, 5, 8, 3, 12, 45, 2),
                        exercise = ExerciseRepository.getCategories()[0].exercises[4] // Press de Banca 4
                    )
                )
            ),
            FullRoutineDay(
                routineDay = RoutineDay(6, 3, 2, "Full Body B", 2),
                exercises = listOf(
                    RoutineDayExerciseWithDetails(
                        routineDayExercise = RoutineDayExercise(9, 6, 9, 3, 10, 60, 1),
                        exercise = ExerciseRepository.getCategories()[1].exercises[1] // Sentadilla 1
                    ),
                    RoutineDayExerciseWithDetails(
                        routineDayExercise = RoutineDayExercise(10, 6, 10, 3, 12, 45, 2),
                        exercise = ExerciseRepository.getCategories()[1].exercises[2] // Sentadilla 2
                    )
                )
            )
        )
    )
)
