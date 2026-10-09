package com.example.myciclo.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myciclo.R
import com.example.myciclo.ui.theme.Medidas
import com.example.myciclo.ui.theme.MyCicloBorder
import com.example.myciclo.ui.theme.MyCicloPrimary
import com.example.myciclo.ui.theme.MyCicloPrimarySoft
import com.example.myciclo.ui.theme.MyCicloSurface
import com.example.myciclo.ui.theme.MyCicloText

// Chip de 40 dp. Seleccionado = borde de 2 dp + fondo suave + ✓,
// así no depende solo del color. El cambio de color se anima en 150 ms.
// Para período o EVA se cambian colorSeleccion y fondoSeleccion.
@Composable
fun ChipMyCiclo(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colorSeleccion: Color = MyCicloPrimary,
    fondoSeleccion: Color = MyCicloPrimarySoft
) {
    val colorFondo by animateColorAsState(
        targetValue = if (seleccionado) fondoSeleccion else MyCicloSurface,
        animationSpec = tween(150),
        label = "fondoChip"
    )
    val colorBorde by animateColorAsState(
        targetValue = if (seleccionado) colorSeleccion else MyCicloBorder,
        animationSpec = tween(150),
        label = "bordeChip"
    )

    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        label = {
            Text(
                text = texto,
                style = MaterialTheme.typography.labelMedium
            )
        },
        leadingIcon = if (seleccionado) {
            {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        } else {
            null
        },
        modifier = modifier.height(Medidas.altoChip),
        shape = CircleShape,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = colorFondo,
            labelColor = MyCicloText,
            selectedContainerColor = colorFondo,
            selectedLabelColor = colorSeleccion,
            selectedLeadingIconColor = colorSeleccion
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = seleccionado,
            borderColor = colorBorde,
            selectedBorderColor = colorBorde,
            borderWidth = 1.dp,
            selectedBorderWidth = 2.dp
        )
    )
}
