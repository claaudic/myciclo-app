package com.example.myciclo.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface RegistroDiarioDao {

    // Si ya existe un registro para esa fecha, se reemplaza (corregir el registro del día).
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(registro: RegistroDiarioEntity): Long

    @Delete
    suspend fun eliminar(registro: RegistroDiarioEntity)

    @Query("SELECT * FROM registros_diarios WHERE fecha = :fecha")
    fun obtenerPorFecha(fecha: LocalDate): Flow<RegistroDiarioEntity?>

    // Rango inclusivo, útil para el calendario y el historial.
    @Query("SELECT * FROM registros_diarios WHERE fecha BETWEEN :desde AND :hasta ORDER BY fecha")
    fun obtenerEntre(desde: LocalDate, hasta: LocalDate): Flow<List<RegistroDiarioEntity>>

    @Query("SELECT * FROM registros_diarios ORDER BY fecha DESC")
    fun obtenerTodos(): Flow<List<RegistroDiarioEntity>>
}
