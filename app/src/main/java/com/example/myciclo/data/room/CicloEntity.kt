package com.example.myciclo.data.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

// Un ciclo comienza el primer día de sangrado (inicio del período).
// No se guarda la fecha de fin ni el día del ciclo: se calculan en utils
// a partir de la fecha de inicio del ciclo siguiente.
@Entity(
    tableName = "ciclos",
    indices = [Index(value = ["fechaInicio"], unique = true)]
)
data class CicloEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fechaInicio: LocalDate
)
