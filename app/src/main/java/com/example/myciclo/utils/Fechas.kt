package com.example.myciclo.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

// Funciones para mostrar y convertir fechas. No dependen de la pantalla.

private val formatoFechaLarga = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", Locale("es", "CL"))

// Ejemplo: 2026-09-26 -> "Sábado 26 de septiembre"
fun formatearFechaLarga(fecha: LocalDate): String =
    fecha.format(formatoFechaLarga).replaceFirstChar { it.uppercase() }

// El DatePicker de Material trabaja con milisegundos en hora UTC.
// Estas dos funciones convierten entre ese formato y LocalDate.
fun fechaAMillis(fecha: LocalDate): Long =
    fecha.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

fun millisAFecha(millis: Long): LocalDate =
    Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
