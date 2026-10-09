package com.example.myciclo

import com.example.myciclo.viewmodel.OnboardingViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

// Pruebas del Paso 1 del onboarding: la regla vive en el ViewModel, no en la pantalla.
class OnboardingViewModelTest {

    @Test
    fun paso1_sinSeleccion_noPuedeContinuar() {
        val viewModel = OnboardingViewModel()

        assertFalse(viewModel.uiState.value.puedeContinuar)
    }

    @Test
    fun paso1_conUnaSeleccion_puedeContinuar() {
        val viewModel = OnboardingViewModel()

        viewModel.onObjetivoClick("Conocer mis patrones")

        assertTrue(viewModel.uiState.value.puedeContinuar)
    }

    @Test
    fun paso1_tocarDosVeces_quitaLaSeleccion() {
        val viewModel = OnboardingViewModel()

        viewModel.onObjetivoClick("Observar con EVA")
        viewModel.onObjetivoClick("Observar con EVA")

        assertTrue(viewModel.uiState.value.objetivos.isEmpty())
    }

    @Test
    fun continuar_sinSeleccion_noAvanza() {
        val viewModel = OnboardingViewModel()

        viewModel.continuar(onTerminar = {})

        assertEquals(1, viewModel.uiState.value.paso)
    }

    @Test
    fun continuar_conSeleccion_avanzaAlPaso2() {
        val viewModel = OnboardingViewModel()

        viewModel.onObjetivoClick("Registrar cómo me siento")
        viewModel.continuar(onTerminar = {})

        assertEquals(2, viewModel.uiState.value.paso)
    }
}
