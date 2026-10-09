package com.example.myciclo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myciclo.ui.theme.Medidas
import com.example.myciclo.ui.theme.MyCicloBackground
import com.example.myciclo.ui.theme.MyCicloGoldSoft
import com.example.myciclo.ui.theme.MyCicloGoldText
import com.example.myciclo.ui.theme.MyCicloPinkSoft
import com.example.myciclo.ui.theme.MyCicloPinkText
import com.example.myciclo.ui.theme.MyCicloTextSecondary
import com.example.myciclo.ui.theme.MyCicloTheme

// Vista previa de todos los componentes (pestaña "Split" o "Design" en Android Studio).
// Sirve para revisar el sistema visual sin abrir el emulador.
@Preview(showBackground = true, heightDp = 900)
@Composable
fun VistaPreviaComponentes() {
    MyCicloTheme {
        Column(
            modifier = Modifier
                .background(MyCicloBackground)
                .padding(Medidas.margenLateral),
            verticalArrangement = Arrangement.spacedBy(Medidas.separacion)
        ) {
            Text(
                text = "Conoce tu ciclo",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Título de pantalla",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            BotonPrincipal(texto = "Guardar registro", onClick = {})
            BotonPrincipal(texto = "Guardar registro", onClick = {}, habilitado = false)
            BotonPrincipal(texto = "Guardando...", onClick = {}, cargando = true)
            BotonSecundario(texto = "Repetir foto", onClick = {})

            Row(horizontalArrangement = Arrangement.spacedBy(Medidas.separacion)) {
                ChipMyCiclo(texto = "Feliz", seleccionado = false, onClick = {})
                ChipMyCiclo(texto = "Tranquila", seleccionado = true, onClick = {})
                ChipMyCiclo(
                    texto = "Leve",
                    seleccionado = true,
                    onClick = {},
                    colorSeleccion = MyCicloPinkText,
                    fondoSeleccion = MyCicloPinkSoft
                )
            }

            TarjetaMyCiclo {
                Text(
                    text = "TU CICLO HOY",
                    style = MaterialTheme.typography.labelSmall,
                    color = MyCicloGoldText
                )
                Text(
                    text = "Día 12 de tu ciclo",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Último período: 26 de septiembre",
                    style = MaterialTheme.typography.bodySmall,
                    color = MyCicloTextSecondary
                )
            }

            TarjetaMyCiclo(colorFondo = MyCicloGoldSoft, colorBorde = MyCicloGoldSoft) {
                Text(
                    text = "Tu registro de hoy",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            MensajeError(error = "Elige la intensidad del flujo.")
            MensajeExito(mensaje = "Registro guardado")
        }
    }
}
