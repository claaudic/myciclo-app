package com.example.myciclo.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface RegistroEvaDao {

    @Insert
    suspend fun insertar(registro: RegistroEvaEntity): Long

    @Update
    suspend fun actualizar(registro: RegistroEvaEntity)

    @Delete
    suspend fun eliminar(registro: RegistroEvaEntity)

    // Observaciones con fechaHora en [desde, hasta). Para un día: desde = 00:00 del día,
    // hasta = 00:00 del día siguiente.
    @Query("SELECT * FROM registros_eva WHERE fechaHora >= :desde AND fechaHora < :hasta ORDER BY fechaHora")
    fun obtenerEntre(desde: LocalDateTime, hasta: LocalDateTime): Flow<List<RegistroEvaEntity>>

    @Query("SELECT * FROM registros_eva WHERE resultado = 'CON_HELECHOS' ORDER BY fechaHora")
    fun obtenerConHelechos(): Flow<List<RegistroEvaEntity>>

    @Query("SELECT * FROM registros_eva ORDER BY fechaHora DESC")
    fun obtenerTodos(): Flow<List<RegistroEvaEntity>>
}
