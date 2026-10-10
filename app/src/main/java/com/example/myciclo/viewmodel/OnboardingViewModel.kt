package com.example.myciclo.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myciclo.utils.esFechaFutura
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

const val TOTAL_PASOS = 5

data class OnboardingUiState(
    val paso: Int = 1,
    // Paso 1: lo que la usuaria quiere conocer (puede elegir varias opciones)
    val objetivos: Set<String> = emptySet(),
    // Paso 2: inicio del último período. Queda en null si eligió "No lo recuerdo".
    val fechaUltimoPeriodo: LocalDate? = null,
    val noRecuerdaPeriodo: Boolean = false
) {
    // Regla de cada paso para habilitar "Continuar".
    val puedeContinuar: Boolean
        get() = when (paso) {
            1 -> objetivos.isNotEmpty()
            2 -> fechaUltimoPeriodo != null
            else -> true
        }
}

class OnboardingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState

    // Si la opción ya estaba elegida se quita; si no, se agrega.
    fun onObjetivoClick(objetivo: String) {
        val objetivos = _uiState.value.objetivos
        val nuevos = if (objetivo in objetivos) {
            objetivos - objetivo
        } else {
            objetivos + objetivo
        }
        _uiState.value = _uiState.value.copy(objetivos = nuevos)
    }

    // El calendario ya bloquea las fechas futuras; aquí se valida de nuevo
    // para que la regla no dependa solo de la pantalla.
    fun onFechaUltimoPeriodoChange(fecha: LocalDate?) {
        if (fecha != null && esFechaFutura(fecha)) {
            return
        }
        _uiState.value = _uiState.value.copy(
            fechaUltimoPeriodo = fecha,
            noRecuerdaPeriodo = false
        )
    }

    // "No lo recuerdo": se sigue sin fecha. Hoy mostrará el estado "Sin período".
    fun onNoRecuerdoPeriodo() {
        val estado = _uiState.value
        _uiState.value = estado.copy(
            fechaUltimoPeriodo = null,
            noRecuerdaPeriodo = true,
            paso = estado.paso + 1
        )
    }

    // Avanza al siguiente paso. En el último paso termina el onboarding.
    fun continuar(onTerminar: () -> Unit) {
        val estado = _uiState.value
        if (!estado.puedeContinuar) {
            return
        }
        if (estado.paso == TOTAL_PASOS) {
            onTerminar()
        } else {
            _uiState.value = estado.copy(paso = estado.paso + 1)
        }
    }

    fun pasoAnterior() {
        val estado = _uiState.value
        if (estado.paso > 1) {
            _uiState.value = estado.copy(paso = estado.paso - 1)
        }
    }
}
