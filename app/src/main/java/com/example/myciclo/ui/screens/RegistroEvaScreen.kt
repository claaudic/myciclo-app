package com.example.myciclo.ui.screens

import androidx.compose.runtime.Composable

@Composable
fun RegistroEvaScreen(
    onVolver: () -> Unit
) {
    PlaceholderScreen(
        titulo = "Registro EVA",
        descripcion = "Fotografía de la muestra y resultado con o sin helechos (opcional).",
        onVolver = onVolver
    )
}
