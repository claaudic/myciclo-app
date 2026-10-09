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
import com.example.myciclo.ui.screens.LoginScreen
import com.example.myciclo.ui.screens.OnboardingScreen
import com.example.myciclo.ui.screens.RegistroEvaScreen
import com.example.myciclo.ui.screens.RegistroScreen
import com.example.myciclo.viewmodel.InicioViewModel
import com.example.myciclo.viewmodel.OnboardingViewModel
import com.example.myciclo.viewmodel.RegistroViewModel
import com.example.myciclo.ui.screens.BienvenidaScreen
object Rutas {
    const val BIENVENIDA = "bienvenida"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val INICIO = "inicio"
    const val REGISTRO_DIARIO = "registro_diario"
    const val REGISTRO_EVA = "registro_eva"
    const val CALENDARIO = "calendario"
    const val HISTORIAL = "historial"
    const val APRENDER = "aprender"
}

@Composable
fun Navigation(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = Rutas.BIENVENIDA
    ){
        //BIENVENIDA
        composable(Rutas.BIENVENIDA) {
            BienvenidaScreen(
                onComenzar = {
                    navController.navigate(Rutas.ONBOARDING)
                }
            )
        }

        // ONBOARDING (5 pasos en una misma pantalla)
        composable(Rutas.ONBOARDING) {

            val onboardingViewModel: OnboardingViewModel = viewModel()

            OnboardingScreen(
                onboardingViewModel = onboardingViewModel,
                onTerminar = {
                    // Al terminar se quitan Bienvenida y Onboarding del historial:
                    // con "atrás" desde Inicio no se vuelve al onboarding.
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.BIENVENIDA) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        // LOGIN
        composable(Rutas.LOGIN) {
            LoginScreen(
                onIngresar = {
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.LOGIN) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // INICIO
        composable(Rutas.INICIO) {

            val inicioViewModel: InicioViewModel = viewModel()
            val uiState by inicioViewModel.uiState.collectAsState()

            InicioScreen(
                diaDelCiclo = uiState.diaDelCiclo,
                onNuevoRegistro = {
                    navController.navigate(Rutas.REGISTRO_DIARIO)
                },
                onRegistroEva = {
                    navController.navigate(Rutas.REGISTRO_EVA)
                },
                onCalendario = {
                    navController.navigate(Rutas.CALENDARIO)
                },
                onHistorial = {
                    navController.navigate(Rutas.HISTORIAL)
                },
                onAprender = {
                    navController.navigate(Rutas.APRENDER)
                }
            )
        }

        // REGISTRO DIARIO
        composable(Rutas.REGISTRO_DIARIO) {

            val registroViewModel: RegistroViewModel = viewModel()

            RegistroScreen(
                registroViewModel = registroViewModel,
                onVolver = {
                    navController.popBackStack()
                },
                onGuardado = {
                    // Vuelve a Inicio (aunque se presione "Guardar" dos veces).
                    navController.popBackStack(Rutas.INICIO, inclusive = false)
                }
            )
        }

        // REGISTRO EVA
        composable(Rutas.REGISTRO_EVA) {
            RegistroEvaScreen(
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // CALENDARIO
        composable(Rutas.CALENDARIO) {
            CalendarioScreen(
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // HISTORIAL
        composable(Rutas.HISTORIAL) {
            HistorialScreen(
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // APRENDER
        composable(Rutas.APRENDER) {
            AprenderScreen(
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}