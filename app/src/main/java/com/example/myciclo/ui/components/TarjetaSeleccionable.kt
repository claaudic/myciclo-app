package com.example.myciclo.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.myciclo.R
import com.example.myciclo.ui.theme.Medidas
import com.example.myciclo.ui.theme.MyCicloBorder
import com.example.myciclo.ui.theme.MyCicloPrimary
import com.example.myciclo.ui.theme.MyCicloPrimarySoft
import com.example.myciclo.ui.theme.MyCicloSurface
import com.example.myciclo.ui.theme.MyCicloText
import com.example.myciclo.ui.theme.MyCicloTextSecondary

// Tarjeta que se marca y desmarca al tocarla (selección múltiple).
// Seleccionada = borde morado de 2 dp + fondo morado suave + casilla con ✓.
// Los colores cambian con una animación de 150 ms.
@Composable
fun TarjetaSeleccionable(
    titulo: String,
    subtitulo: String,
    icono: Int,
    seleccionada: Boolean,
    onClick: () -> Unit,
    colorIcono: Color = MyCicloPrimary
) {
    val colorFondo by animateColorAsState(
        targetValue = if (seleccionada) MyCicloPrimarySoft else MyCicloSurface,
        animationSpec = tween(150),
        label = "fondoTarjeta"
    )
    val colorBorde by animateColorAsState(
        targetValue = if (seleccionada) MyCicloPrimary else MyCicloBorder,
        animationSpec = tween(150),
        label = "bordeTarjeta"
    )
    val forma = RoundedCornerShape(Medidas.radioTarjeta)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(colorFondo)
            .border(if (seleccionada) 2.dp else 1.dp, colorBorde, forma)
            // toggleable: se comporta como una casilla (también para TalkBack)
            .toggleable(
                value = seleccionada,
                onValueChange = { onClick() },
                role = Role.Checkbox
            )
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícono dentro de un cuadrado redondeado
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(if (seleccionada) MyCicloSurface else MyCicloPrimarySoft),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icono),
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                color = MyCicloText
            )
            Text(
                text = subtitulo,
                style = MaterialTheme.typography.bodySmall,
                color = MyCicloTextSecondary
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        CasillaCheck(marcada = seleccionada)
    }
}

// Casilla cuadrada: morada con ✓ si está marcada, solo borde si no.
@Composable
private fun CasillaCheck(marcada: Boolean) {
    val forma = RoundedCornerShape(8.dp)

    Box(
        modifier = Modifier
            .size(26.dp)
            .clip(forma)
            .background(if (marcada) MyCicloPrimary else MyCicloSurface)
            .border(1.5.dp, if (marcada) MyCicloPrimary else MyCicloBorder, forma),
        contentAlignment = Alignment.Center
    ) {
        if (marcada) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
