package com.example.myciclo.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myciclo.data.room.ResultadoEva
import com.example.myciclo.ui.components.BotonPrincipal
import com.example.myciclo.ui.components.CamaraEva
import com.example.myciclo.ui.components.TarjetaMyCiclo
import com.example.myciclo.ui.theme.Medidas
import com.example.myciclo.ui.theme.MyCicloBorder
import com.example.myciclo.ui.theme.MyCicloEva
import com.example.myciclo.ui.theme.MyCicloEvaSoft
import com.example.myciclo.ui.theme.MyCicloPrimaryDark
import com.example.myciclo.ui.theme.MyCicloTextSecondary
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// Modelo visual: los datos reales vendrán del ViewModel cuando integremos Room.
data class ObservacionEvaUi(val hora: String, val resultado: ResultadoEva)

@Composable
fun RegistroEvaScreen(
    onVolver: () -> Unit,
    onGuardar: ((ResultadoEva, String?) -> Unit)? = null,
    observacionesHoy: List<ObservacionEvaUi> = emptyList()
) {
    var seleccion by rememberSaveable { mutableStateOf<String?>(null) }
    var rutaFoto by rememberSaveable { mutableStateOf<String?>(null) }
    val resultado = when (seleccion) {
        "CON_HELECHOS" -> ResultadoEva.CON_HELECHOS
        "SIN_HELECHOS" -> ResultadoEva.SIN_HELECHOS
        else -> null
    }
    val fecha = LocalDate.now().format(
        DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", Locale.forLanguageTag("es-CL"))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            // Respetar las barras del sistema y el recorte de la cámara frontal.
            .safeDrawingPadding()
            // Permitir llegar a los controles y al aviso inferior en pantallas pequeñas.
            .verticalScroll(rememberScrollState())
            .padding(
                start = Medidas.margenLateral,
                end = Medidas.margenLateral,
                top = 16.dp,
                bottom = 32.dp
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onVolver) { Text("← Volver", color = MyCicloEva) }
            Column(modifier = Modifier.weight(1f)) {
                Text("Registro EVA", style = MaterialTheme.typography.headlineSmall)
                Text(fecha, style = MaterialTheme.typography.bodySmall, color = MyCicloTextSecondary)
            }
            Surface(shape = RoundedCornerShape(50), color = MyCicloEvaSoft) {
                Text(
                    "Opcional",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    color = MyCicloEva,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Muestra", "Secado", "Fotografía").forEachIndexed { index, paso ->
                Surface(
                    modifier = Modifier.weight(1f),
                    color = if (index == 2) MyCicloEvaSoft else Color.White,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, if (index == 2) MyCicloEva else MyCicloBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("${index + 1}", color = MyCicloEva, fontWeight = FontWeight.Bold)
                        Text(paso, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        Text(
            "Coloca una muestra de saliva y espera a que esté completamente seca " +
                "(aproximadamente una hora) antes de fotografiarla.",
            style = MaterialTheme.typography.bodySmall,
            color = MyCicloTextSecondary
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = MyCicloPrimaryDark
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Vista previa · Cámara frontal", color = Color.White)
                CamaraEva(onFotoSeleccionada = { rutaFoto = it })
            }
        }

        Text("¿Qué observas?", style = MaterialTheme.typography.titleLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OpcionEva(
                texto = "Con helechos",
                seleccionada = resultado == ResultadoEva.CON_HELECHOS,
                onClick = { seleccion = "CON_HELECHOS" },
                modifier = Modifier.weight(1f)
            )
            OpcionEva(
                texto = "Sin helechos",
                seleccionada = resultado == ResultadoEva.SIN_HELECHOS,
                onClick = { seleccion = "SIN_HELECHOS" },
                modifier = Modifier.weight(1f)
            )
        }

        TarjetaMyCiclo {
            Text("Registros de hoy", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(10.dp))
            if (observacionesHoy.isEmpty()) {
                Text("Sin observaciones hoy", style = MaterialTheme.typography.bodyMedium)
                Text("Puedes guardar varias en un mismo día.",
                    style = MaterialTheme.typography.bodySmall, color = MyCicloTextSecondary)
            } else {
                observacionesHoy.forEach { registro ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(registro.hora)
                        Text(if (registro.resultado == ResultadoEva.CON_HELECHOS)
                            "Con helechos" else "Sin helechos", color = MyCicloEva)
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }

        if (resultado == null) {
            Text("Selecciona «Con helechos» o «Sin helechos» para guardar.",
                style = MaterialTheme.typography.bodySmall, color = MyCicloTextSecondary)
        }
        BotonPrincipal(
            texto = "Guardar observación",
            onClick = { resultado?.let { onGuardar?.invoke(it, rutaFoto) } },
            habilitado = resultado != null && onGuardar != null
        )
        Text(
            "EVA no es un método anticonceptivo y no confirma la ovulación.",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            color = MyCicloTextSecondary
        )
    }
}

@Composable
private fun OpcionEva(
    texto: String,
    seleccionada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(82.dp),
        shape = RoundedCornerShape(18.dp),
        color = if (seleccionada) MyCicloEvaSoft else Color.White,
        border = BorderStroke(if (seleccionada) 2.dp else 1.dp,
            if (seleccionada) MyCicloEva else MyCicloBorder)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(if (seleccionada) "✓" else "○", color = MyCicloEva)
            Text(texto, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
