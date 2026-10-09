package com.example.myciclo.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myciclo.ui.theme.MyCicloGold
import com.example.myciclo.ui.theme.MyCicloText
import com.example.myciclo.ui.theme.MyCicloTextSecondary

@Composable
fun BienvenidaScreen(
    onComenzar: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                start = 22.dp,
                end = 22.dp,
                top = 24.dp,
                bottom = 22.dp
            )
    ) {

        // CABECERA
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "myCiclo",
                fontSize = 28.sp,
                fontWeight = FontWeight.Normal,
                color = MyCicloText
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "HECHO EN CHILE",
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                color = MyCicloGold
            )
        }

        Spacer(
            modifier = Modifier.height(48.dp)
        )

        // ILUSTRACIÓN
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            CicloVisual()
        }

        Spacer(
            modifier = Modifier.height(34.dp)
        )

        // TÍTULO
        Text(
            text = "Conoce tu ciclo,\na tu manera",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 32.sp,
            lineHeight = 39.sp,
            fontWeight = FontWeight.Normal,
            color = MyCicloText,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // DESCRIPCIÓN
        Text(
            text = "Un espacio para escucharte, registrar lo que\nsientes y reconocer tus propios patrones.",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = MyCicloTextSecondary,
            textAlign = TextAlign.Center
        )

        // Empuja el botón hacia abajo
        Spacer(
            modifier = Modifier.weight(1f)
        )

        // BOTÓN PRINCIPAL
        Button(
            onClick = onComenzar,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MyCicloGold,
                contentColor = MyCicloText
            )
        ) {

            Text(
                text = "Comenzar",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // PRIVACIDAD
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {

            Text(
                text = "♡",
                color = MyCicloGold,
                fontSize = 16.sp
            )

            Spacer(
                modifier = Modifier.size(8.dp)
            )

            Text(
                text = "Tu información personal es privada y permanece en tu dispositivo. Sin crear una cuenta.",
                modifier = Modifier.weight(1f),
                fontSize = 11.sp,
                lineHeight = 16.sp,
                color = MyCicloTextSecondary
            )
        }
    }
}

@Composable
private fun CicloVisual() {

    Box(
        modifier = Modifier.size(245.dp),
        contentAlignment = Alignment.Center
    ) {

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {

            val center = Offset(
                x = size.width / 2,
                y = size.height / 2
            )

            // CÍRCULO EXTERIOR
            drawCircle(
                color = MyCicloGold.copy(alpha = 0.20f),
                radius = size.width * 0.46f,
                center = center,
                style = Stroke(
                    width = 1.2.dp.toPx()
                )
            )

            // SEGUNDO CÍRCULO
            drawCircle(
                color = MyCicloGold.copy(alpha = 0.28f),
                radius = size.width * 0.37f,
                center = center,
                style = Stroke(
                    width = 1.2.dp.toPx()
                )
            )

            // FONDO CENTRAL
            drawCircle(
                color = Color.White.copy(alpha = 0.75f),
                radius = size.width * 0.28f,
                center = center
            )

            // PUNTO DORADO
            drawCircle(
                color = MyCicloGold,
                radius = 6.dp.toPx(),
                center = Offset(
                    x = size.width * 0.86f,
                    y = size.height * 0.28f
                )
            )

            // PUNTO SECUNDARIO
            drawCircle(
                color = MyCicloTextSecondary.copy(alpha = 0.65f),
                radius = 4.dp.toPx(),
                center = Offset(
                    x = size.width * 0.07f,
                    y = size.height * 0.76f
                )
            )

            // HOJA IZQUIERDA
            val hojaIzquierda = Path().apply {

                moveTo(
                    size.width * 0.50f,
                    size.height * 0.57f
                )

                cubicTo(
                    size.width * 0.39f,
                    size.height * 0.58f,
                    size.width * 0.31f,
                    size.height * 0.50f,
                    size.width * 0.29f,
                    size.height * 0.40f
                )

                cubicTo(
                    size.width * 0.41f,
                    size.height * 0.40f,
                    size.width * 0.49f,
                    size.height * 0.47f,
                    size.width * 0.50f,
                    size.height * 0.57f
                )

                close()
            }

            drawPath(
                path = hojaIzquierda,
                color = MyCicloGold,
                style = Stroke(
                    width = 2.dp.toPx()
                )
            )

            // NERVADURA
            drawLine(
                color = MyCicloGold,
                start = Offset(
                    x = size.width * 0.30f,
                    y = size.height * 0.42f
                ),
                end = Offset(
                    x = size.width * 0.50f,
                    y = size.height * 0.57f
                ),
                strokeWidth = 1.3.dp.toPx()
            )

            // HOJA DERECHA
            val hojaDerecha = Path().apply {

                moveTo(
                    size.width * 0.51f,
                    size.height * 0.49f
                )

                cubicTo(
                    size.width * 0.51f,
                    size.height * 0.36f,
                    size.width * 0.60f,
                    size.height * 0.28f,
                    size.width * 0.69f,
                    size.height * 0.22f
                )

                cubicTo(
                    size.width * 0.70f,
                    size.height * 0.36f,
                    size.width * 0.62f,
                    size.height * 0.45f,
                    size.width * 0.51f,
                    size.height * 0.49f
                )

                close()
            }

            drawPath(
                path = hojaDerecha,
                color = MyCicloGold,
                style = Stroke(
                    width = 2.dp.toPx()
                )
            )

            drawLine(
                color = MyCicloGold,
                start = Offset(
                    x = size.width * 0.52f,
                    y = size.height * 0.47f
                ),
                end = Offset(
                    x = size.width * 0.67f,
                    y = size.height * 0.26f
                ),
                strokeWidth = 1.3.dp.toPx()
            )
        }

        // FRASE PEQUEÑA
        Text(
            text = "CADA CICLO ES TUYO",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(50.dp)
                )
                .padding(
                    horizontal = 14.dp,
                    vertical = 5.dp
                ),
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium,
            color = MyCicloGold
        )
    }
}