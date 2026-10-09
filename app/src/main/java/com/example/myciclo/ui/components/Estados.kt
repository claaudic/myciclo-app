package com.example.myciclo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myciclo.R
import com.example.myciclo.ui.theme.MyCicloError
import com.example.myciclo.ui.theme.MyCicloSuccess

// Mensaje de error junto al campo: ícono + texto rojo.
// Aparece y desaparece con AnimatedVisibility (200 ms). Si error es null no se ve.
@Composable
fun MensajeError(error: String?) {
    MensajeConIcono(
        mensaje = error,
        icono = R.drawable.ic_error,
        color = MyCicloError
    )
}

// Mensaje de éxito: ícono ✓ + texto verde.
@Composable
fun MensajeExito(mensaje: String?) {
    MensajeConIcono(
        mensaje = mensaje,
        icono = R.drawable.ic_check,
        color = MyCicloSuccess
    )
}

@Composable
private fun MensajeConIcono(
    mensaje: String?,
    icono: Int,
    color: Color
) {
    AnimatedVisibility(
        visible = mensaje != null,
        enter = expandVertically(tween(200)) + fadeIn(tween(200)),
        exit = shrinkVertically(tween(200)) + fadeOut(tween(200))
    ) {
        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icono),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = mensaje.orEmpty(),
                color = color,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
