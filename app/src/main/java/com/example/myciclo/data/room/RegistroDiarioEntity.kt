package com.example.myciclo.data.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

// Registro general del día, independiente de EVA. Hay uno por fecha.
// Todos los campos son opcionales: omitir datos no invalida el registro.
@Entity(
    tableName = "registros_diarios",
    indices = [Index(value = ["fecha"], unique = true)]
)
data class RegistroDiarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fecha: LocalDate,
    val flujo: Flujo? = null,
    val emociones: List<String> = emptyList(),
    val sintomas: List<String> = emptyList(),
    val energia: String? = null,
    val biomarcador: String? = null,
    val notas: String? = null
)
