package com.example.myciclo.ui.screens

import androidx.compose.runtime.Composable

@Composable
fun CalendarioScreen(
    onVolver: () -> Unit
) {
    PlaceholderScreen(
        titulo = "Calendario",
        descripcion = "Registros del mes, día a día.",
        onVolver = onVolver
    )
}
