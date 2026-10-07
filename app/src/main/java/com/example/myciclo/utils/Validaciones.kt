package com.example.myciclo.utils

import java.time.LocalDate

fun esFechaFutura(fecha: LocalDate, hoy: LocalDate = LocalDate.now()): Boolean =
    fecha.isAfter(hoy)

// Un registro (diario o EVA) no puede tener fecha futura.
fun validarFechaRegistro(fecha: LocalDate, hoy: LocalDate = LocalDate.now()): Boolean =
    !esFechaFutura(fecha, hoy)

// Devuelve un mensaje de error para mostrar en pantalla, o null si la fecha es válida.
fun validarInicioPeriodo(
    fecha: LocalDate,
    iniciosRegistrados: List<LocalDate>,
    hoy: LocalDate = LocalDate.now()
): String? = when {
    esFechaFutura(fecha, hoy) -> "La fecha de inicio del período no puede ser futura."
    fecha in iniciosRegistrados -> "Ya registraste un período que comienza ese día."
    else -> null
}
