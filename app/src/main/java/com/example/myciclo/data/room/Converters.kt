package com.example.myciclo.data.room

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Room solo guarda tipos simples, por eso las fechas y listas se convierten a texto.
// Las fechas usan formato ISO (2026-10-06 / 2026-10-06T08:10:00), que se ordena
// correctamente como texto y permite filtrar por rangos en las consultas.
class Converters {

    private val formatoFechaHora = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    @TypeConverter
    fun fechaATexto(fecha: LocalDate?): String? = fecha?.toString()

    @TypeConverter
    fun textoAFecha(texto: String?): LocalDate? = texto?.let { LocalDate.parse(it) }

    @TypeConverter
    fun fechaHoraATexto(fechaHora: LocalDateTime?): String? = fechaHora?.format(formatoFechaHora)

    @TypeConverter
    fun textoAFechaHora(texto: String?): LocalDateTime? =
        texto?.let { LocalDateTime.parse(it, formatoFechaHora) }

    @TypeConverter
    fun listaATexto(lista: List<String>): String = lista.joinToString(SEPARADOR)

    @TypeConverter
    fun textoALista(texto: String): List<String> =
        if (texto.isBlank()) emptyList() else texto.split(SEPARADOR)

    companion object {
        const val SEPARADOR = "|"
    }
}
