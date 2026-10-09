package com.example.myciclo.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myciclo.ui.theme.MyCicloBorder
import com.example.myciclo.ui.theme.MyCicloPrimary

// Barra de progreso del onboarding: un segmento por paso.
// Los segmentos hasta el paso actual se pintan morados (animado en 300 ms).
@Composable
fun BarraPasos(
    pasoActual: Int,
    totalPasos: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (paso in 1..totalPasos) {
            val color by animateColorAsState(
                targetValue = if (paso <= pasoActual) MyCicloPrimary else MyCicloBorder,
                animationSpec = tween(300),
                label = "segmento$paso"
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(5.dp)
                    .background(color, CircleShape)
            )
        }
    }
}
