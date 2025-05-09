package com.example.gymandroid.model

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.gymandroid.model.ExerciseRepository.getExerciseById

// Constantes para la base de datos
private const val DATABASE_NAME = "gym_db"
private const val DATABASE_VERSION = 2
private const val TABLE_FAVORITES = "favorites"

// Columnas de la tabla favorites
private const val COL_ID = "id"
private const val COL_TITLE = "title"
private const val COL_MUSCLE = "muscle"
private const val COL_REPS_OR_TIME = "reps_or_time"
private const val COL_IMAGE_URL = "image_url"
private const val COL_TIMESTAMP = "timestamp"

private const val TABLE_ROUTINES = "routines"
private const val TABLE_ROUTINE_DAYS = "routine_days"
private const val TABLE_ROUTINE_EXERCISES = "routine_exercises"

// Columnas para rutinas
private const val COL_TRAINER_ID = "trainer_id"
private const val COL_OBJECTIVE = "objective"
private const val COL_LEVEL = "level"
private const val COL_DURATION_WEEKS = "duration_weeks"
private const val COL_FREQUENCY_PER_WEEK = "frequency_per_week"
private const val COL_DESCRIPTION = "description"
private const val COL_ROUTINE_ID = "routine_id"
private const val COL_DAY_OF_WEEK = "day_of_week"
private const val COL_DAY_ID = "day_id"
private const val COL_EXERCISE_ID = "exercise_id"
private const val COL_SETS = "sets"
private const val COL_REPS = "reps"
private const val COL_REST_SECONDS = "rest_seconds"
private const val COL_NAMEROUTINE = "name_routine"
private const val COL_NAMEROUTINEDAY = "name_routineday"
private const val COL_ORDERROUTINEDAY = "order_routineday"
private const val COL_ORDERROUTINEXCERSICE = "order_excersiceroutine"


class ExerciseDBHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    init {
        checkAndCreateTables()
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Tabla de favoritos (existente)
        val createFavoritesTable = """
        CREATE TABLE $TABLE_FAVORITES (
            $COL_ID INTEGER PRIMARY KEY,
            $COL_TITLE TEXT NOT NULL,
            $COL_MUSCLE TEXT NOT NULL,
            $COL_REPS_OR_TIME TEXT NOT NULL,
            $COL_IMAGE_URL TEXT NOT NULL,
            $COL_TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP
        )
    """.trimIndent()

        // Tabla de rutinas (nueva)
        val createRoutinesTable = """
        CREATE TABLE $TABLE_ROUTINES (
            $COL_ID INTEGER PRIMARY KEY,
            $COL_TRAINER_ID INTEGER,
            $COL_NAMEROUTINE TEXT NOT NULL,
            $COL_OBJECTIVE TEXT NOT NULL,
            $COL_LEVEL TEXT NOT NULL,
            $COL_DURATION_WEEKS INTEGER,
            $COL_FREQUENCY_PER_WEEK INTEGER,
            $COL_DESCRIPTION TEXT
        )
    """.trimIndent()

        // Tabla de días de rutina (nueva)
        val createDaysTable = """
        CREATE TABLE $TABLE_ROUTINE_DAYS (
            $COL_ID INTEGER PRIMARY KEY,
            $COL_ROUTINE_ID INTEGER NOT NULL,
            $COL_DAY_OF_WEEK INTEGER,
            $COL_NAMEROUTINEDAY TEXT NOT NULL,
            $COL_ORDERROUTINEDAY INTEGER NOT NULL,
            FOREIGN KEY($COL_ROUTINE_ID) REFERENCES $TABLE_ROUTINES($COL_ID) ON DELETE CASCADE
        )
    """.trimIndent()

        // Tabla de ejercicios de rutina (nueva)
        val createExercisesTable = """
        CREATE TABLE $TABLE_ROUTINE_EXERCISES (
            $COL_ID INTEGER PRIMARY KEY,
            $COL_DAY_ID INTEGER NOT NULL,
            $COL_EXERCISE_ID INTEGER NOT NULL,
            $COL_SETS INTEGER NOT NULL,
            $COL_REPS INTEGER NOT NULL,
            $COL_REST_SECONDS INTEGER NOT NULL,
            $COL_ORDERROUTINEXCERSICE INTEGER NOT NULL,
            FOREIGN KEY($COL_DAY_ID) REFERENCES $TABLE_ROUTINE_DAYS($COL_ID) ON DELETE CASCADE
        )
    """.trimIndent()

        db.execSQL(createFavoritesTable)
        db.execSQL(createRoutinesTable)
        db.execSQL(createDaysTable)
        db.execSQL(createExercisesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Elimina todas las tablas existentes
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ROUTINES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ROUTINE_DAYS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ROUTINE_EXERCISES")

        // Vuelve a crear las tablas
        onCreate(db)
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }


    fun checkAndCreateTables() {
        val db = writableDatabase
        try {
            // Verifica si la tabla routines existe
            val cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='$TABLE_ROUTINES'", null)
            val exists = cursor.count > 0
            cursor.close()

            if (!exists) {
                // Crea las tablas que faltan
                onCreate(db)
            }
        } catch (e: Exception) {
            Log.e("DB_ERROR", "Error checking tables", e)
        } finally {
            db.close()
        }
    }


    // Resultado detallado de la operación
    sealed class SaveResult {
        object Success : SaveResult()
        object Updated : SaveResult()
        data class Error(val exception: Exception) : SaveResult()
    }

    // Añadir ejercicio a favoritos con verificación explícita
    fun addFavorite(exercise: Exercise): SaveResult {
        val db = this.writableDatabase
        return try {
            val values = ContentValues().apply {
                put(COL_ID, exercise.id)
                put(COL_TITLE, exercise.title)
                put(COL_MUSCLE, exercise.muscle)
                put(COL_REPS_OR_TIME, exercise.repsOrTime)
                put(COL_IMAGE_URL, exercise.imageUrl)
            }

            val id = db.insertWithOnConflict(
                TABLE_FAVORITES,
                null,
                values,
                SQLiteDatabase.CONFLICT_IGNORE
            )

            if (id == -1L) {
                // Ya existía, actualizamos
                val rowsAffected = db.update(
                    TABLE_FAVORITES,
                    values,
                    "$COL_ID = ?",
                    arrayOf(exercise.id.toString())
                )
                if (rowsAffected > 0) {
                    Log.d("DB_SUCCESS", "Ejercicio ${exercise.id} actualizado en favoritos")
                    SaveResult.Updated
                } else {
                    throw Exception("No se pudo actualizar el ejercicio existente")
                }
            } else {
                Log.d("DB_SUCCESS", "Ejercicio ${exercise.id} añadido a favoritos con ID: $id")
                SaveResult.Success
            }
        } catch (e: Exception) {
            Log.e("DB_ERROR", "Error al guardar favorito: ${e.message}", e)
            SaveResult.Error(e)
        } finally {
            db.close()
        }
    }

    // Función mejorada para eliminar con verificación
    fun removeFavorite(exerciseId: Int): Boolean {
        val db = this.writableDatabase
        return try {
            val rowsDeleted = db.delete(
                TABLE_FAVORITES,
                "$COL_ID = ?",
                arrayOf(exerciseId.toString())
            )
            if (rowsDeleted > 0) {
                Log.d("DB_SUCCESS", "Ejercicio $exerciseId eliminado de favoritos")
                true
            } else {
                Log.w("DB_WARNING", "No se encontró el ejercicio $exerciseId para eliminar")
                false
            }
        } catch (e: Exception) {
            Log.e("DB_ERROR", "Error al eliminar favorito: ${e.message}", e)
            false
        } finally {
            db.close()
        }
    }

    // Verificar si un ejercicio es favorito
    fun isFavorite(exerciseId: Int): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_FAVORITES,
            arrayOf(COL_ID),
            "$COL_ID = ?",
            arrayOf(exerciseId.toString()),
            null, null, null, null
        )

        val isFavorite = cursor.count > 0
        cursor.close()
        db.close()
        return isFavorite
    }

    // Obtener todos los favoritos
    @SuppressLint("Range")
    fun getAllFavorites(): List<Exercise> {
        val favorites = mutableListOf<Exercise>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_FAVORITES ORDER BY $COL_TIMESTAMP DESC",
            null
        )

        try {
            if (cursor.moveToFirst()) {
                do {
                    favorites.add(
                        Exercise(
                            id = cursor.getInt(cursor.getColumnIndex(COL_ID)),
                            title = cursor.getString(cursor.getColumnIndex(COL_TITLE)),
                            muscle = cursor.getString(cursor.getColumnIndex(COL_MUSCLE)),
                            repsOrTime = cursor.getString(cursor.getColumnIndex(COL_REPS_OR_TIME)),
                            imageUrl = cursor.getString(cursor.getColumnIndex(COL_IMAGE_URL))
                        )
                    )
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e("DB_ERROR", "Error getting favorites", e)
        } finally {
            cursor.close()
            db.close()
        }
        return favorites
    }

    // Obtener los últimos N favoritos
    @SuppressLint("Range")
    fun getRecentFavorites(limit: Int = 5): List<Exercise> {
        val favorites = mutableListOf<Exercise>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_FAVORITES ORDER BY $COL_TIMESTAMP DESC LIMIT $limit",
            null
        )

        try {
            if (cursor.moveToFirst()) {
                do {
                    favorites.add(
                        Exercise(
                            id = cursor.getInt(cursor.getColumnIndex(COL_ID)),
                            title = cursor.getString(cursor.getColumnIndex(COL_TITLE)),
                            muscle = cursor.getString(cursor.getColumnIndex(COL_MUSCLE)),
                            repsOrTime = cursor.getString(cursor.getColumnIndex(COL_REPS_OR_TIME)),
                            imageUrl = cursor.getString(cursor.getColumnIndex(COL_IMAGE_URL))
                        )
                    )
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e("DB_ERROR", "Error getting recent favorites", e)
        } finally {
            cursor.close()
            db.close()
        }
        return favorites
    }

    // Dentro de tu DatabaseHelper (ExerciseDBHandler)
    fun deleteAllData() : Boolean {
        val db = writableDatabase
        try {
            // Elimina todos los registros de todas las tablas
            db.execSQL("DELETE FROM $TABLE_FAVORITES")
            // Si tienes más tablas, añade líneas similares:
            // db.execSQL("DELETE FROM otra_tabla")


        } catch (e: Exception) {
            Log.e("DB_ERROR", "Error delete favorites", e)
            return false
        }
        finally {
            db.close()
        }
        return true
    }

    companion object {
        @Volatile
        private var INSTANCE: ExerciseDBHandler? = null

        fun getInstance(context: Context): ExerciseDBHandler {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ExerciseDBHandler(context.applicationContext).also { INSTANCE = it }
            }
        }
    }



    // Resultado de operación para rutinas
    sealed class RoutineSaveResult {
        object Success : RoutineSaveResult()
        data class Error(val exception: Exception) : RoutineSaveResult()
    }

    // Guardar rutina completa
    fun saveFullRoutine(routine: FullRoutine): RoutineSaveResult {
        val db = writableDatabase
        return try {
            db.beginTransaction()

            // 1. Guardar la rutina principal
            val routineValues = ContentValues().apply {
                put(COL_ID, routine.routine.id)
                put(COL_TRAINER_ID, routine.routine.id_trainer)
                put(COL_NAMEROUTINE, routine.routine.name)
                put(COL_OBJECTIVE, routine.routine.objective)
                put(COL_LEVEL, routine.routine.level)
                put(COL_DURATION_WEEKS, routine.routine.durationWeeks)
                put(COL_FREQUENCY_PER_WEEK, routine.routine.frequencyPerWeek)
                put(COL_DESCRIPTION, routine.routine.description)
            }

            db.insertWithOnConflict(TABLE_ROUTINES, null, routineValues, SQLiteDatabase.CONFLICT_REPLACE)

            // 2. Guardar los días de la rutina
            routine.days.forEach { day ->
                val dayValues = ContentValues().apply {
                    put(COL_ID, day.routineDay.id)
                    put(COL_ROUTINE_ID, routine.routine.id)
                    put(COL_DAY_OF_WEEK, day.routineDay.dayOfWeek)
                    put(COL_NAMEROUTINEDAY, day.routineDay.name)
                    put(COL_ORDERROUTINEDAY, day.routineDay.order)
                }

                db.insertWithOnConflict(TABLE_ROUTINE_DAYS, null, dayValues, SQLiteDatabase.CONFLICT_REPLACE)

                // 3. Guardar los ejercicios de cada día
                day.exercises.forEach { exercise ->
                    val exerciseValues = ContentValues().apply {
                        put(COL_ID, exercise.routineDayExercise.id)
                        put(COL_DAY_ID, day.routineDay.id)
                        put(COL_EXERCISE_ID, exercise.routineDayExercise.exerciseId)
                        put(COL_SETS, exercise.routineDayExercise.sets)
                        put(COL_REPS, exercise.routineDayExercise.reps)
                        put(COL_REST_SECONDS, exercise.routineDayExercise.restSeconds)
                        put(COL_ORDERROUTINEXCERSICE, exercise.routineDayExercise.order)
                    }

                    db.insertWithOnConflict(TABLE_ROUTINE_EXERCISES, null, exerciseValues, SQLiteDatabase.CONFLICT_REPLACE)
                }
            }

            db.setTransactionSuccessful()
            RoutineSaveResult.Success
        } catch (e: Exception) {
            Log.e("DB_ERROR", "Error saving routine", e)
            RoutineSaveResult.Error(e)
        } finally {
            db.endTransaction()
        }
    }

    fun deleteRoutineById(routineId: Int): Boolean {
        val db = writableDatabase
        return try {
            db.beginTransaction()

            // Al tener ON DELETE CASCADE, basta con eliminar la rutina principal
            val rowsDeleted = db.delete(
                TABLE_ROUTINES,
                "$COL_ID = ?",
                arrayOf(routineId.toString())
            )

            db.setTransactionSuccessful()
            rowsDeleted > 0
        } catch (e: Exception) {
            Log.e("DB_ERROR", "Error al eliminar rutina $routineId", e)
            false
        } finally {
            db.endTransaction()
            db.close()
        }
    }


    // Obtener todas las rutinas guardadas
    @SuppressLint("Range")
    fun getAllSavedRoutines(): List<FullRoutine> {
        val routines = mutableListOf<FullRoutine>()
        val db = readableDatabase

        try {
            val routinesCursor = db.rawQuery("SELECT * FROM $TABLE_ROUTINES", null)

            if (routinesCursor.moveToFirst()) {
                do {
                    val routineId = routinesCursor.getInt(routinesCursor.getColumnIndex(COL_ID))

                    val routine = Routine(
                        id = routineId,
                        id_trainer = routinesCursor.getInt(routinesCursor.getColumnIndex(COL_TRAINER_ID)),
                        name = routinesCursor.getString(routinesCursor.getColumnIndex(COL_NAMEROUTINE)),
                        objective = routinesCursor.getString(routinesCursor.getColumnIndex(COL_OBJECTIVE)),
                        level = routinesCursor.getString(routinesCursor.getColumnIndex(COL_LEVEL)),
                        durationWeeks = routinesCursor.getInt(routinesCursor.getColumnIndex(COL_DURATION_WEEKS)),
                        frequencyPerWeek = routinesCursor.getInt(routinesCursor.getColumnIndex(COL_FREQUENCY_PER_WEEK)),
                        description = routinesCursor.getString(routinesCursor.getColumnIndex(COL_DESCRIPTION))
                    )

                    val days = mutableListOf<FullRoutineDay>()
                    val daysCursor = db.rawQuery(
                        "SELECT * FROM $TABLE_ROUTINE_DAYS WHERE $COL_ROUTINE_ID = ? ORDER BY $COL_ORDERROUTINEDAY",
                        arrayOf(routineId.toString())
                    )

                    if (daysCursor.moveToFirst()) {
                        do {
                            val dayId = daysCursor.getInt(daysCursor.getColumnIndex(COL_ID))

                            val routineDay = RoutineDay(
                                id = dayId,
                                routineId = routineId,
                                dayOfWeek = daysCursor.getInt(daysCursor.getColumnIndex(COL_DAY_OF_WEEK)),
                                name = daysCursor.getString(daysCursor.getColumnIndex(COL_NAMEROUTINEDAY)),
                                order = daysCursor.getInt(daysCursor.getColumnIndex(COL_ORDERROUTINEDAY))
                            )

                            val exercises = mutableListOf<RoutineDayExerciseWithDetails>()
                            val exercisesCursor = db.rawQuery(
                                "SELECT * FROM $TABLE_ROUTINE_EXERCISES WHERE $COL_DAY_ID = ? ORDER BY $COL_ORDERROUTINEXCERSICE",
                                arrayOf(dayId.toString())
                            )

                            if (exercisesCursor.moveToFirst()) {
                                do {
                                    val routineDayExercise = RoutineDayExercise(
                                        id = exercisesCursor.getInt(exercisesCursor.getColumnIndex(COL_ID)),
                                        routineDayId = dayId,
                                        exerciseId = exercisesCursor.getInt(exercisesCursor.getColumnIndex(COL_EXERCISE_ID)),
                                        sets = exercisesCursor.getInt(exercisesCursor.getColumnIndex(COL_SETS)),
                                        reps = exercisesCursor.getInt(exercisesCursor.getColumnIndex(COL_REPS)),
                                        restSeconds = exercisesCursor.getInt(exercisesCursor.getColumnIndex(COL_REST_SECONDS)),
                                        order = exercisesCursor.getInt(exercisesCursor.getColumnIndex(COL_ORDERROUTINEXCERSICE))
                                    )

                                    // Obtener el ejercicio completo de la tabla de ejercicios/favoritos
                                    val exerciseDetails = getExerciseById(routineDayExercise.exerciseId)
                                    val exercise = exerciseDetails ?: Exercise(
                                        id = routineDayExercise.exerciseId,
                                        title = "Ejercicio ${routineDayExercise.exerciseId}",
                                        muscle = "Desconocido",
                                        repsOrTime = "${routineDayExercise.sets}x${routineDayExercise.reps}",
                                        imageUrl = "",
                                        description = ""
                                    )

                                    exercises.add(RoutineDayExerciseWithDetails(routineDayExercise, exercise))
                                } while (exercisesCursor.moveToNext())
                            }
                            exercisesCursor.close()

                            days.add(FullRoutineDay(routineDay, exercises))
                        } while (daysCursor.moveToNext())
                    }
                    daysCursor.close()

                    routines.add(FullRoutine(routine, days))
                } while (routinesCursor.moveToNext())
            }
            routinesCursor.close()
        } catch (e: Exception) {
            Log.e("DB_ERROR", "Error getting routines", e)
        } finally {
            db.close()
        }

        return routines
    }

}

