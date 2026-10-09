package com.example.myciclo.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

const val TOTAL_PASOS = 5

data class OnboardingUiState(
    val paso: Int = 1,
    // Paso 1: lo que la usuaria quiere conocer (puede elegir varias opciones)
    val objetivos: Set<String> = emptySet()
) {
    // Regla de cada paso para habilitar "Continuar".
    val puedeContinuar: Boolean
        get() = when (paso) {
            1 -> objetivos.isNotEmpty()
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
