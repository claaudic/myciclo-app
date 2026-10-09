package com.example.myciclo.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.myciclo.ui.theme.MyCicloPink
import com.example.myciclo.ui.theme.MyCicloPrimary

// Logo de texto: "my" en rosado y "Ciclo" en morado, como en los mockups.
@Composable
fun LogoMyCiclo(tamano: TextUnit = 30.sp) {
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(color = MyCicloPink)) {
                append("my")
            }
            withStyle(SpanStyle(color = MyCicloPrimary)) {
                append("Ciclo")
            }
        },
        style = MaterialTheme.typography.headlineMedium,
        fontSize = tamano
    )
}
