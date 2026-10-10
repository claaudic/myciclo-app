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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myciclo.R
import com.example.myciclo.ui.components.BarraPasos
import com.example.myciclo.ui.components.BotonPrincipal
import com.example.myciclo.ui.components.EnEspanol
import com.example.myciclo.ui.components.LogoMyCiclo
import com.example.myciclo.ui.components.TarjetaMyCiclo
import com.example.myciclo.ui.components.TarjetaSeleccionable
import com.example.myciclo.ui.theme.Medidas
import com.example.myciclo.ui.theme.MyCicloDisabledText
import com.example.myciclo.ui.theme.MyCicloEva
import com.example.myciclo.ui.theme.MyCicloPink
import com.example.myciclo.ui.theme.MyCicloPinkSoft
import com.example.myciclo.ui.theme.MyCicloPinkText
import com.example.myciclo.ui.theme.MyCicloPrimary
import com.example.myciclo.ui.theme.MyCicloSurface
import com.example.myciclo.ui.theme.MyCicloText
import com.example.myciclo.ui.theme.MyCicloTextSecondary
import com.example.myciclo.utils.fechaAMillis
import com.example.myciclo.utils.formatearFechaLarga
import com.example.myciclo.utils.millisAFecha
import com.example.myciclo.viewmodel.OnboardingViewModel
import com.example.myciclo.viewmodel.TOTAL_PASOS
import java.time.LocalDate

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

        // CABECERA: logo (paso 1) o flecha para volver (pasos 2 a 5), y "PASO X DE 5"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (uiState.paso == 1) {
                LogoMyCiclo(tamano = 26.sp)
            } else {
                IconButton(
                    onClick = { onboardingViewModel.pasoAnterior() },
                    modifier = Modifier.offset(x = (-12).dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_atras),
                        contentDescription = "Volver al paso anterior",
                        tint = MyCicloPrimary
                    )
                }
            }

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
                // EnEspanol: el calendario sale en español aunque el teléfono esté en otro idioma
                2 -> EnEspanol {
                    PasoUltimoPeriodo(
                        fecha = uiState.fechaUltimoPeriodo,
                        onFechaChange = { onboardingViewModel.onFechaUltimoPeriodoChange(it) }
                    )
                }
                else -> PasoPendiente(paso = paso)
            }
        }

        Spacer(modifier = Modifier.height(Medidas.separacion))

        BotonPrincipal(
            texto = if (uiState.paso == TOTAL_PASOS) "Comenzar a usar myCiclo" else "Continuar",
            habilitado = uiState.puedeContinuar,
            onClick = { onboardingViewModel.continuar(onTerminar) }
        )

        // Solo en el paso 2: permite seguir sin fecha
        if (uiState.paso == 2) {
            TextButton(
                onClick = { onboardingViewModel.onNoRecuerdoPeriodo() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Medidas.areaTactil)
            ) {
                Text(
                    text = "No lo recuerdo",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MyCicloPrimary
                )
            }
        }
    }
}

// PASO 2: ¿Cuándo comenzó tu último período? (calendario sin fechas futuras)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasoUltimoPeriodo(
    fecha: LocalDate?,
    onFechaChange: (LocalDate?) -> Unit
) {
    val hoyMillis = fechaAMillis(LocalDate.now())

    // El calendario parte con la fecha guardada en el ViewModel (si existe)
    // y bloquea los días posteriores a hoy.
    val estadoCalendario = rememberDatePickerState(
        initialSelectedDateMillis = fecha?.let { fechaAMillis(it) },
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis <= hoyMillis

            override fun isSelectableYear(year: Int): Boolean = year <= LocalDate.now().year
        }
    )

    // Cada vez que la usuaria toca un día, se avisa al ViewModel.
    LaunchedEffect(estadoCalendario.selectedDateMillis) {
        onFechaChange(estadoCalendario.selectedDateMillis?.let { millisAFecha(it) })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "¿Cuándo comenzó tu último período?",
            style = MaterialTheme.typography.headlineMedium,
            color = MyCicloPrimary
        )

        TarjetaMyCiclo(relleno = 6.dp, contenido = {
            // Fecha elegida, en rosado (color del período)
            Row(
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
                    .background(MyCicloPinkSoft, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_gota),
                    contentDescription = null,
                    tint = MyCicloPinkText,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = if (fecha != null) formatearFechaLarga(fecha) else "Elige un día en el calendario",
                    style = MaterialTheme.typography.titleMedium,
                    color = MyCicloPinkText
                )
            }

            DatePicker(
                state = estadoCalendario,
                title = null,
                headline = null,
                showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    containerColor = MyCicloSurface,
                    selectedDayContainerColor = MyCicloPink,
                    selectedDayContentColor = Color.White,
                    todayContentColor = MyCicloPrimary,
                    todayDateBorderColor = MyCicloPrimary,
                    dayContentColor = MyCicloText,
                    disabledDayContentColor = MyCicloDisabledText,
                    weekdayContentColor = MyCicloTextSecondary,
                    navigationContentColor = MyCicloPrimary,
                    subheadContentColor = MyCicloText,
                    yearContentColor = MyCicloText,
                    selectedYearContainerColor = MyCicloPrimary
                )
            )
        })

        // Texto explicativo con ícono de información
        Row(verticalAlignment = Alignment.Top) {
            Icon(
                painter = painterResource(R.drawable.ic_info),
                contentDescription = null,
                tint = MyCicloTextSecondary,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Con esta fecha calculamos automáticamente el día de tu ciclo. No se pueden elegir fechas futuras.",
                style = MaterialTheme.typography.bodySmall,
                color = MyCicloTextSecondary
            )
        }
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
