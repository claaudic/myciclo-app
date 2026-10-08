package com.example.myciclo.ui.screens

import androidx.compose.runtime.Composable

@Composable
fun HistorialScreen(
    onVolver: () -> Unit
) {
    PlaceholderScreen(
        titulo = "Historial",
        descripcion = "Comparación entre ciclos.",
        onVolver = onVolver
    )
}
