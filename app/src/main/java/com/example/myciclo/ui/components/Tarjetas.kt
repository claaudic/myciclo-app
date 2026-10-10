package com.example.myciclo.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myciclo.ui.theme.Medidas
import com.example.myciclo.ui.theme.MyCicloBorder
import com.example.myciclo.ui.theme.MyCicloSurface

// Tarjeta base de la app: blanca, con borde suave y esquinas de 22 dp.
// El fondo se puede cambiar (crema para el resumen, lavanda para EVA, etc.).
// El relleno interior es de 18 dp; se puede achicar si el contenido es ancho (calendario).
@Composable
fun TarjetaMyCiclo(
    modifier: Modifier = Modifier,
    colorFondo: Color = MyCicloSurface,
    colorBorde: Color = MyCicloBorder,
    relleno: Dp = 18.dp,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Medidas.radioTarjeta),
        colors = CardDefaults.cardColors(containerColor = colorFondo),
        border = BorderStroke(1.dp, colorBorde)
    ) {
        Column(
            modifier = Modifier.padding(relleno),
            content = contenido
        )
    }
}
