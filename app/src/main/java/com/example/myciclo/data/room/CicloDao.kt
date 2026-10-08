package com.example.myciclo.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CicloDao {

    // Si ya existe un ciclo con esa fecha de inicio, no se duplica.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(ciclo: CicloEntity): Long

    @Update
    suspend fun actualizar(ciclo: CicloEntity)

    @Delete
    suspend fun eliminar(ciclo: CicloEntity)

    // Del más reciente al más antiguo.
    @Query("SELECT * FROM ciclos ORDER BY fechaInicio DESC")
    fun obtenerTodos(): Flow<List<CicloEntity>>

    @Query("SELECT * FROM ciclos ORDER BY fechaInicio DESC LIMIT 1")
    fun obtenerUltimo(): Flow<CicloEntity?>
}
