package com.example.myciclo.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myciclo.R
import com.example.myciclo.ui.components.BarraPasos
import com.example.myciclo.ui.components.BotonPrincipal
import com.example.myciclo.ui.components.LogoMyCiclo
import com.example.myciclo.ui.components.TarjetaSeleccionable
import com.example.myciclo.ui.theme.Medidas
import com.example.myciclo.ui.theme.MyCicloEva
import com.example.myciclo.ui.theme.MyCicloPrimary
import com.example.myciclo.ui.theme.MyCicloTextSecondary
import com.example.myciclo.viewmodel.OnboardingViewModel
import com.example.myciclo.viewmodel.TOTAL_PASOS

@Composable
fun OnboardingScreen(
    onboardingViewModel: OnboardingViewModel,
    onTerminar: () -> Unit
) {
    val uiState by onboardingViewModel.uiState.collectAsState()

    // El botón "atrás" del teléfono vuelve al paso anterior (desde el paso 2 en adelante).
    BackHandler(enabled = uiState.paso > 1) {
        onboardingViewModel.pasoAnterior()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = Medidas.margenLateral, vertical = 24.dp)
    ) {

        // CABECERA: logo y "PASO X DE 5"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LogoMyCiclo(tamano = 26.sp)

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "PASO ${uiState.paso} DE $TOTAL_PASOS",
                style = MaterialTheme.typography.labelSmall,
                color = MyCicloTextSecondary
            )
        }

        Spacer(modifier = Modifier.height(Medidas.separacion))

        BarraPasos(pasoActual = uiState.paso, totalPasos = TOTAL_PASOS)

        Spacer(modifier = Modifier.height(32.dp))

        // CONTENIDO DEL PASO: el paso nuevo entra desde la derecha (300 ms).
        // Al volver atrás, entra desde la izquierda.
        AnimatedContent(
            targetState = uiState.paso,
            transitionSpec = {
                val direccion = if (targetState > initialState) 1 else -1
                (slideInHorizontally(tween(300)) { ancho -> ancho * direccion } + fadeIn(tween(300)))
                    .togetherWith(
                        slideOutHorizontally(tween(300)) { ancho -> -ancho * direccion } + fadeOut(tween(300))
                    )
            },
            modifier = Modifier.weight(1f),
            label = "pasoOnboarding"
        ) { paso ->
            when (paso) {
                1 -> PasoObjetivos(
                    objetivos = uiState.objetivos,
                    onObjetivoClick = { onboardingViewModel.onObjetivoClick(it) }
                )
                else -> PasoPendiente(paso = paso)
            }
        }

        Spacer(modifier = Modifier.height(Medidas.separacion))

        BotonPrincipal(
            texto = if (uiState.paso == TOTAL_PASOS) "Comenzar a usar myCiclo" else "Continuar",
            habilitado = uiState.puedeContinuar,
            onClick = { onboardingViewModel.continuar(onTerminar) }
        )
    }
}

// PASO 1: ¿Qué te gustaría conocer de tu ciclo? (selección múltiple)
@Composable
private fun PasoObjetivos(
    objetivos: Set<String>,
    onObjetivoClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        TituloPaso(
            pregunta = "¿Qué te gustaría conocer de tu ciclo?",
            ayuda = "Puedes elegir más de una opción."
        )

        TarjetaSeleccionable(
            titulo = "Conocer mis patrones",
            subtitulo = "Cómo cambian tus ciclos en el tiempo",
            icono = R.drawable.ic_patrones,
            seleccionada = "Conocer mis patrones" in objetivos,
            onClick = { onObjetivoClick("Conocer mis patrones") }
        )

        TarjetaSeleccionable(
            titulo = "Registrar cómo me siento",
            subtitulo = "Emociones, síntomas y energía",
            icono = R.drawable.ic_emocion,
            seleccionada = "Registrar cómo me siento" in objetivos,
            onClick = { onObjetivoClick("Registrar cómo me siento") }
        )

        TarjetaSeleccionable(
            titulo = "Observar con EVA",
            subtitulo = "Opcional · requiere el dispositivo",
            icono = R.drawable.ic_eva,
            seleccionada = "Observar con EVA" in objetivos,
            onClick = { onObjetivoClick("Observar con EVA") },
            colorIcono = MyCicloEva
        )
    }
}

// Pregunta grande del paso y texto de ayuda debajo.
@Composable
private fun TituloPaso(
    pregunta: String,
    ayuda: String
) {
    Column {
        Text(
            text = pregunta,
            style = MaterialTheme.typography.headlineMedium,
            color = MyCicloPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = ayuda,
            style = MaterialTheme.typography.bodyLarge,
            color = MyCicloTextSecondary
        )
    }
}

// Los pasos 2 a 5 se implementan en sus propias tarjetas de Trello.
@Composable
private fun PasoPendiente(paso: Int) {
    TituloPaso(
        pregunta = "Paso $paso",
        ayuda = "Próximamente."
    )
}
