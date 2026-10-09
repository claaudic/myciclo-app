package com.example.myciclo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myciclo.data.MyCicloRepository
import com.example.myciclo.data.room.AppDatabase
import com.example.myciclo.data.room.Flujo
import com.example.myciclo.data.room.RegistroDiarioEntity
import com.example.myciclo.utils.MAX_BIOMARCADOR
import com.example.myciclo.utils.MAX_NOTAS
import com.example.myciclo.utils.MAX_SINTOMAS
import com.example.myciclo.utils.validarFlujo
import com.example.myciclo.utils.validarLargoTexto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

// Datos del formulario y sus mensajes de error (null = sin error).
data class RegistroUiState(
    val comenzoPeriodo: Boolean? = null,
    val flujo: Flujo? = null,
    val emocion: String? = null,
    val energia: String? = null,
    val sintomas: String = "",
    val biomarcador: String = "",
    val notas: String = "",
    val errorFlujo: String? = null,
    val errorSintomas: String? = null,
    val errorBiomarcador: String? = null,
    val errorNotas: String? = null,
    val errorGeneral: String? = null
)

class RegistroViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MyCicloRepository(AppDatabase.getInstance(application))

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState

    fun onComenzoPeriodoChange(valor: Boolean) {
        _uiState.value = _uiState.value.copy(comenzoPeriodo = valor)
    }

    fun onFlujoChange(valor: Flujo) {
        _uiState.value = _uiState.value.copy(flujo = valor)
    }

    fun onEmocionChange(valor: String) {
        _uiState.value = _uiState.value.copy(emocion = valor)
    }

    fun onEnergiaChange(valor: String) {
        _uiState.value = _uiState.value.copy(energia = valor)
    }

    fun onSintomasChange(valor: String) {
        _uiState.value = _uiState.value.copy(sintomas = valor)
    }

    fun onBiomarcadorChange(valor: String) {
        _uiState.value = _uiState.value.copy(biomarcador = valor)
    }

    fun onNotasChange(valor: String) {
        _uiState.value = _uiState.value.copy(notas = valor)
    }

    // Valida el formulario. Si no hay errores, guarda en Room y llama a onGuardado.
    fun guardar(onGuardado: () -> Unit) {
        val estado = _uiState.value

        val hayDatos = estado.comenzoPeriodo != null || estado.flujo != null ||
                estado.emocion != null || estado.energia != null ||
                estado.sintomas.isNotBlank() || estado.biomarcador.isNotBlank() ||
                estado.notas.isNotBlank()

        val errorGeneral = if (hayDatos) null else "Registra al menos un dato antes de guardar."
        val errorFlujo = validarFlujo(estado.comenzoPeriodo, estado.flujo)
        val errorSintomas = validarLargoTexto(estado.sintomas, MAX_SINTOMAS)
        val errorBiomarcador = validarLargoTexto(estado.biomarcador, MAX_BIOMARCADOR)
        val errorNotas = validarLargoTexto(estado.notas, MAX_NOTAS)

        _uiState.value = estado.copy(
            errorGeneral = errorGeneral,
            errorFlujo = errorFlujo,
            errorSintomas = errorSintomas,
            errorBiomarcador = errorBiomarcador,
            errorNotas = errorNotas
        )

        if (errorGeneral != null || errorFlujo != null || errorSintomas != null ||
            errorBiomarcador != null || errorNotas != null
        ) {
            return
        }

        viewModelScope.launch {
            val hoy = LocalDate.now()

            // Si el período comenzó hoy, se registra el inicio de un ciclo nuevo.
            if (estado.comenzoPeriodo == true) {
                repository.registrarInicioPeriodo(hoy)
            }

            repository.guardarRegistroDiario(
                RegistroDiarioEntity(
                    fecha = hoy,
                    flujo = estado.flujo,
                    emociones = listOfNotNull(estado.emocion),
                    sintomas = if (estado.sintomas.isBlank()) emptyList() else listOf(estado.sintomas),
                    energia = estado.energia,
                    biomarcador = estado.biomarcador.ifBlank { null },
                    notas = estado.notas.ifBlank { null }
                )
            )

            onGuardado()
        }
    }
}
