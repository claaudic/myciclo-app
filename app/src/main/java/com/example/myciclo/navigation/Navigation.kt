package com.example.myciclo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myciclo.ui.screens.AprenderScreen
import com.example.myciclo.ui.screens.CalendarioScreen
import com.example.myciclo.ui.screens.HistorialScreen
import com.example.myciclo.ui.screens.InicioScreen
import com.example.myciclo.ui.screens.RegistroEvaScreen
import com.example.myciclo.ui.screens.RegistroScreen
import com.example.myciclo.viewmodel.InicioViewModel

object Rutas {
    const val INICIO = "inicio"
    const val REGISTRO_DIARIO = "registro_diario"
    const val REGISTRO_EVA = "registro_eva"
    const val CALENDARIO = "calendario"
    const val HISTORIAL = "historial"
    const val APRENDER = "aprender"
}

@Composable
fun Navigation(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Rutas.INICIO
    ) {

        composable(Rutas.INICIO) {
            val inicioViewModel: InicioViewModel = viewModel()
            val uiState by inicioViewModel.uiState.collectAsState()

            InicioScreen(
                diaDelCiclo = uiState.diaDelCiclo,
                onNuevoRegistro = { navController.navigate(Rutas.REGISTRO_DIARIO) },
                onRegistroEva = { navController.navigate(Rutas.REGISTRO_EVA) },
                onCalendario = { navController.navigate(Rutas.CALENDARIO) },
                onHistorial = { navController.navigate(Rutas.HISTORIAL) },
                onAprender = { navController.navigate(Rutas.APRENDER) }
            )
        }

        // Por ahora usa la pantalla existente del compañero; se adaptará
        // (sin Alto/Medio/Nulo ni día manual) al implementar el registro diario.
        composable(Rutas.REGISTRO_DIARIO) {
            RegistroScreen(
                onVolver = { navController.popBackStack() },
                onGuardar = { navController.popBackStack() }
            )
        }

        composable(Rutas.REGISTRO_EVA) {
            RegistroEvaScreen(onVolver = { navController.popBackStack() })
        }

        composable(Rutas.CALENDARIO) {
            CalendarioScreen(onVolver = { navController.popBackStack() })
        }

        composable(Rutas.HISTORIAL) {
            HistorialScreen(onVolver = { navController.popBackStack() })
        }

        composable(Rutas.APRENDER) {
            AprenderScreen(onVolver = { navController.popBackStack() })
        }
    }
}
