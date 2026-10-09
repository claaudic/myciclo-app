package com.example.myciclo.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myciclo.ui.theme.Medidas
import com.example.myciclo.ui.theme.MyCicloDisabled
import com.example.myciclo.ui.theme.MyCicloDisabledText
import com.example.myciclo.ui.theme.MyCicloPrimary

// Botón morado de 56 dp y bordes redondos. Tiene tres estados:
// normal, deshabilitado (gris, mientras falte algo) y cargando (ruedita + texto).
@Composable
fun BotonPrincipal(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true,
    cargando: Boolean = false
) {
    Button(
        onClick = {
            if (!cargando) {
                onClick()
            }
        },
        enabled = habilitado,
        modifier = modifier
            .fillMaxWidth()
            .height(Medidas.altoBoton),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MyCicloPrimary,
            contentColor = Color.White,
            disabledContainerColor = MyCicloDisabled,
            disabledContentColor = MyCicloDisabledText
        )
    ) {
        if (cargando) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(10.dp))
        }

        Text(
            text = texto,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

// Botón blanco con borde morado, para acciones de segundo nivel
// (por ejemplo "No lo recuerdo" o "Repetir foto").
@Composable
fun BotonSecundario(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        enabled = habilitado,
        modifier = modifier
            .fillMaxWidth()
            .height(Medidas.altoBoton),
        shape = CircleShape,
        border = BorderStroke(1.5.dp, MyCicloPrimary),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MyCicloPrimary
        )
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
