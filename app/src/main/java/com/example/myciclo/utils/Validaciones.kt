package com.example.myciclo.utils

import com.example.myciclo.data.room.Flujo
import java.time.LocalDate

// Largo máximo de los campos de texto del registro diario.
const val MAX_SINTOMAS = 100
const val MAX_BIOMARCADOR = 50
const val MAX_NOTAS = 300

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

// Registro diario: los textos son opcionales, pero no pueden pasar del máximo.
fun validarLargoTexto(texto: String, maximo: Int): String? =
    if (texto.length > maximo) "Máximo $maximo caracteres (llevas ${texto.length})." else null

// Si el período comenzó hoy hay sangrado, así que el flujo es obligatorio y no puede ser "Sin".
fun validarFlujo(comenzoPeriodo: Boolean?, flujo: Flujo?): String? = when {
    comenzoPeriodo == true && flujo == null -> "Indica el flujo de tu período."
    comenzoPeriodo == true && flujo == Flujo.SIN_FLUJO -> "Si comenzó tu período, el flujo no puede ser \"Sin\"."
    else -> null
}
