package com.example.myciclo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    onVolver: () -> Unit,
    onGuardar: () -> Unit
) {

    var diaCiclo by remember { mutableStateOf("") }
    var patron by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }

    var menuAbierto by remember { mutableStateOf(false) }

    val fechaActual = LocalDate.now()
        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        TextButton(
            onClick = onVolver
        ) {
            Text("← Volver")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Nuevo registro",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Registra tu observación diaria"
        )

        Spacer(modifier = Modifier.height(25.dp))

        // FECHA
        OutlinedTextField(
            value = fechaActual,
            onValueChange = {},
            enabled = false,
            label = {
                Text("Fecha")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // DÍA DEL CICLO
        OutlinedTextField(
            value = diaCiclo,
            onValueChange = {
                diaCiclo = it
            },
            label = {
                Text("Día del ciclo")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ESTADO DE CRISTALIZACIÓN
        ExposedDropdownMenuBox(
            expanded = menuAbierto,
            onExpandedChange = {
                menuAbierto = !menuAbierto
            }
        ) {

            OutlinedTextField(
                value = patron,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text("Cristalización")
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = menuAbierto
                    )
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = menuAbierto,
                onDismissRequest = {
                    menuAbierto = false
                }
            ) {

                DropdownMenuItem(
                    text = {
                        Text("Alto")
                    },
                    onClick = {
                        patron = "Alto"
                        menuAbierto = false
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text("Medio")
                    },
                    onClick = {
                        patron = "Medio"
                        menuAbierto = false
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text("Nulo")
                    },
                    onClick = {
                        patron = "Nulo"
                        menuAbierto = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // NOTAS
        OutlinedTextField(
            value = notas,
            onValueChange = {
                notas = it
            },
            label = {
                Text("Síntomas o notas")
            },
            placeholder = {
                Text("Ej: dolor abdominal, cansancio...")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // FOTO
        OutlinedButton(
            onClick = {
                // Después conectaremos cámara/galería
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {

            Text("Agregar fotografía")
        }

        Spacer(modifier = Modifier.weight(1f))

        // GUARDAR
        Button(
            onClick = {

                if (
                    diaCiclo.isNotBlank() &&
                    patron.isNotBlank()
                ) {
                    onGuardar()
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(16.dp)
        ) {

            Text("Guardar registro")
        }
    }
}

