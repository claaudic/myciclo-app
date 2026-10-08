package com.example.myciclo.utils

import java.time.LocalDate
import java.time.temporal.ChronoUnit

// Reglas del ciclo como funciones puras (sin Room ni Android), fáciles de probar.
// No se asume una duración de 28 días ni se predice el día de ovulación.

// La ventana EVA dura 8 días contando el día del primer helecho como día 1.
const val DIAS_VENTANA_EVA = 8

// Día 1 = día de inicio del período. Devuelve null si la fecha consultada
// es anterior al inicio del período.
fun calcularDiaDelCiclo(
    fechaInicioPeriodo: LocalDate,
    fechaConsultada: LocalDate = LocalDate.now()
): Int? {
    if (fechaConsultada.isBefore(fechaInicioPeriodo)) return null
    return ChronoUnit.DAYS.between(fechaInicioPeriodo, fechaConsultada).toInt() + 1
}

// Busca a qué ciclo pertenece una fecha: el inicio de período más reciente
// que no sea posterior a esa fecha.
fun buscarInicioDelCiclo(iniciosPeriodo: List<LocalDate>, fecha: LocalDate): LocalDate? =
    iniciosPeriodo.filter { !it.isAfter(fecha) }.maxOrNull()

// RN-01: el ciclo termina el día anterior al siguiente sangrado.
// Si no hay siguiente inicio, el ciclo sigue en curso (null).
fun calcularFinDelCiclo(siguienteInicio: LocalDate?): LocalDate? =
    siguienteInicio?.minusDays(1)

// Duración en días de un ciclo terminado.
fun calcularDuracionCiclo(inicio: LocalDate, siguienteInicio: LocalDate): Int =
    ChronoUnit.DAYS.between(inicio, siguienteInicio).toInt()

data class VentanaEva(
    val inicio: LocalDate,
    val fin: LocalDate
) {
    fun contiene(fecha: LocalDate): Boolean = !fecha.isBefore(inicio) && !fecha.isAfter(fin)
}

// RN-03: la ventana EVA comienza con el primer registro CON_HELECHOS del ciclo
// y dura DIAS_VENTANA_EVA días. No se extiende más allá del fin del ciclo.
// Devuelve null si en el ciclo aún no hay observaciones con helechos.
fun determinarVentanaEva(
    fechasConHelechos: List<LocalDate>,
    inicioCiclo: LocalDate,
    siguienteInicio: LocalDate? = null
): VentanaEva? {
    val primerHelecho = fechasConHelechos
        .filter { !it.isBefore(inicioCiclo) && (siguienteInicio == null || it.isBefore(siguienteInicio)) }
        .minOrNull() ?: return null

    var fin = primerHelecho.plusDays(DIAS_VENTANA_EVA - 1L)
    val finCiclo = calcularFinDelCiclo(siguienteInicio)
    if (finCiclo != null && fin.isAfter(finCiclo)) fin = finCiclo

    return VentanaEva(primerHelecho, fin)
}
