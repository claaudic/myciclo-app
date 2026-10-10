package com.example.myciclo

import com.example.myciclo.viewmodel.OnboardingViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

// Pruebas del onboarding: las reglas viven en el ViewModel, no en la pantalla.
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

    // Deja el ViewModel en el paso 2 para las pruebas siguientes.
    private fun viewModelEnPaso2(): OnboardingViewModel {
        val viewModel = OnboardingViewModel()
        viewModel.onObjetivoClick("Conocer mis patrones")
        viewModel.continuar(onTerminar = {})
        return viewModel
    }

    @Test
    fun paso2_sinFecha_noPuedeContinuar() {
        val viewModel = viewModelEnPaso2()

        assertFalse(viewModel.uiState.value.puedeContinuar)
    }

    @Test
    fun paso2_conFechaPasada_puedeContinuar() {
        val viewModel = viewModelEnPaso2()

        viewModel.onFechaUltimoPeriodoChange(LocalDate.now().minusDays(10))

        assertTrue(viewModel.uiState.value.puedeContinuar)
    }

    @Test
    fun paso2_fechaFutura_seIgnora() {
        val viewModel = viewModelEnPaso2()

        viewModel.onFechaUltimoPeriodoChange(LocalDate.now().plusDays(1))

        assertNull(viewModel.uiState.value.fechaUltimoPeriodo)
        assertFalse(viewModel.uiState.value.puedeContinuar)
    }

    @Test
    fun paso2_noLoRecuerdo_avanzaSinFecha() {
        val viewModel = viewModelEnPaso2()

        viewModel.onNoRecuerdoPeriodo()

        assertEquals(3, viewModel.uiState.value.paso)
        assertNull(viewModel.uiState.value.fechaUltimoPeriodo)
        assertTrue(viewModel.uiState.value.noRecuerdaPeriodo)
    }
}
