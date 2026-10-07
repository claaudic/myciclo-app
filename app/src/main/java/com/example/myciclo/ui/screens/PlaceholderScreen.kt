package com.example.myciclo.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Pantalla provisoria para comprobar la navegación. Cada pantalla nueva
// la reemplaza por su diseño real cuando se implemente.
@Composable
fun PlaceholderScreen(
    titulo: String,
    descripcion: String,
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        TextButton(onClick = onVolver) {
            Text("← Volver")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = descripcion)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Pantalla en construcción",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}
