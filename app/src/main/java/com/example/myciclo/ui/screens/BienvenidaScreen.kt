package com.example.myciclo.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myciclo.R
import com.example.myciclo.ui.components.BotonPrincipal
import com.example.myciclo.ui.components.LogoMyCiclo
import com.example.myciclo.ui.theme.Medidas
import com.example.myciclo.ui.theme.MyCicloBorder
import com.example.myciclo.ui.theme.MyCicloGold
import com.example.myciclo.ui.theme.MyCicloGoldSoft
import com.example.myciclo.ui.theme.MyCicloGoldText
import com.example.myciclo.ui.theme.MyCicloPink
import com.example.myciclo.ui.theme.MyCicloPrimary
import com.example.myciclo.ui.theme.MyCicloPrimarySoft
import com.example.myciclo.ui.theme.MyCicloTextSecondary
import com.example.myciclo.ui.theme.MyCicloTheme

// Violeta claro del arco de la ilustración (solo decorativo)
private val VioletaClaro = Color(0xFFB8A6DE)

@Composable
fun BienvenidaScreen(
    onComenzar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = Medidas.margenLateral, vertical = 24.dp)
    ) {

        // CABECERA: logo y "HECHO EN CHILE"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LogoMyCiclo()

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "HECHO EN CHILE",
                style = MaterialTheme.typography.labelSmall,
                color = MyCicloGoldText,
                modifier = Modifier
                    .background(MyCicloGoldSoft, CircleShape)
                    .border(BorderStroke(1.dp, MyCicloGold.copy(alpha = 0.4f)), CircleShape)
                    .padding(horizontal = 14.dp, vertical = 7.dp)
            )
        }

        // ILUSTRACIÓN: ocupa el espacio que sobra, así se achica en pantallas bajas
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            IlustracionCiclo(
                modifier = Modifier
                    .widthIn(max = 260.dp)
                    .aspectRatio(1f)
            )
        }

        // TÍTULO
        Text(
            text = "Conoce tu ciclo,\na tu manera",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.displaySmall,
            color = MyCicloPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // DESCRIPCIÓN
        Text(
            text = "Registra cómo te sientes, observa tus patrones y, si quieres, acompáñalo con EVA.",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge,
            color = MyCicloTextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        BotonPrincipal(
            texto = "Comenzar",
            onClick = onComenzar
        )

        Spacer(modifier = Modifier.height(Medidas.separacion))

        // PRIVACIDAD
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_candado),
                contentDescription = null,
                tint = MyCicloTextSecondary,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Tus datos se guardan solo en este teléfono. No necesitas crear una cuenta.",
                style = MaterialTheme.typography.bodySmall,
                color = MyCicloTextSecondary
            )
        }
    }
}

// Ilustración del ciclo hecha con Canvas (sin imágenes):
// un círculo fino con un arco dorado, un anillo con arcos rosado y violeta,
// y al centro un círculo crema con la gota del período.
@Composable
private fun IlustracionCiclo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centro = Offset(size.width / 2, size.height / 2)
            val radioExterior = size.width * 0.48f
            val radioAnillo = size.width * 0.37f
            val grosorAnillo = size.width * 0.06f

            // Círculo exterior fino
            drawCircle(
                color = MyCicloBorder,
                radius = radioExterior,
                center = centro,
                style = Stroke(width = 1.5.dp.toPx())
            )

            // Arco dorado de arriba hacia la derecha, con un punto al final
            dibujarArco(MyCicloGold, radioExterior, -90f, 90f, 2.dp.toPx())
            drawCircle(
                color = MyCicloGold,
                radius = 5.dp.toPx(),
                center = Offset(centro.x + radioExterior, centro.y)
            )

            // Anillo morado suave y sus dos arcos
            drawCircle(
                color = MyCicloPrimarySoft,
                radius = radioAnillo,
                center = centro,
                style = Stroke(width = grosorAnillo)
            )
            dibujarArco(MyCicloPink, radioAnillo, -92f, 60f, grosorAnillo)
            dibujarArco(VioletaClaro, radioAnillo, 25f, 85f, grosorAnillo)

            // Círculo crema del centro
            drawCircle(
                color = MyCicloGoldSoft,
                radius = size.width * 0.25f,
                center = centro
            )
        }

        Icon(
            painter = painterResource(R.drawable.ic_gota),
            contentDescription = null,
            tint = MyCicloPink,
            modifier = Modifier.fillMaxSize(0.24f)
        )
    }
}

// Dibuja un arco con puntas redondas. Los ángulos parten en las 3 en punto (0°)
// y avanzan como el reloj; -90° es arriba.
private fun DrawScope.dibujarArco(
    color: Color,
    radio: Float,
    inicio: Float,
    recorrido: Float,
    grosor: Float
) {
    drawArc(
        color = color,
        startAngle = inicio,
        sweepAngle = recorrido,
        useCenter = false,
        topLeft = Offset(size.width / 2 - radio, size.height / 2 - radio),
        size = Size(radio * 2, radio * 2),
        style = Stroke(width = grosor, cap = StrokeCap.Round)
    )
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun VistaPreviaBienvenida() {
    MyCicloTheme {
        BienvenidaScreen(onComenzar = {})
    }
}
