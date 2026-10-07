package com.example.myciclo.ui.screens

import androidx.compose.runtime.Composable

@Composable
fun AprenderScreen(
    onVolver: () -> Unit
) {
    PlaceholderScreen(
        titulo = "Aprender",
        descripcion = "Videos, guías y preguntas frecuentes.",
        onVolver = onVolver
    )
}
