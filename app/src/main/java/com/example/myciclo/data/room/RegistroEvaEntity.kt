package com.example.myciclo.data.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

// Observación EVA. La fecha y hora NO es única: puede haber varias
// observaciones en un mismo día.
@Entity(
    tableName = "registros_eva",
    indices = [Index(value = ["fechaHora"])]
)
data class RegistroEvaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fechaHora: LocalDateTime,
    val resultado: ResultadoEva,
    val fotoUri: String? = null
)
