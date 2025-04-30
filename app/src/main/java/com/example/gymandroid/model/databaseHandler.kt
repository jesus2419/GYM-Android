package com.example.gymandroid.model

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

// Constantes para la base de datos
private const val DATABASE_NAME = "gym_db"
private const val DATABASE_VERSION = 1
private const val TABLE_FAVORITES = "favorites"

// Columnas de la tabla favorites
private const val COL_ID = "id"
private const val COL_TITLE = "title"
private const val COL_MUSCLE = "muscle"
private const val COL_REPS_OR_TIME = "reps_or_time"
private const val COL_IMAGE_URL = "image_url"
private const val COL_TIMESTAMP = "timestamp"

class ExerciseDBHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_FAVORITES (
                $COL_ID INTEGER PRIMARY KEY,
                $COL_TITLE TEXT NOT NULL,
                $COL_MUSCLE TEXT NOT NULL,
                $COL_REPS_OR_TIME TEXT NOT NULL,
                $COL_IMAGE_URL TEXT NOT NULL,
                $COL_TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITES")
        onCreate(db)
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
}

