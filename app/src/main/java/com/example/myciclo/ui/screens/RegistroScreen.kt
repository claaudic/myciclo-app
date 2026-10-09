package com.example.myciclo.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myciclo.R
import com.example.myciclo.data.room.Flujo
import com.example.myciclo.viewmodel.RegistroViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    registroViewModel: RegistroViewModel,
    onVolver: () -> Unit,
    onGuardado: () -> Unit
) {

    // El estado vive en el ViewModel; la pantalla solo lo muestra.
    val uiState by registroViewModel.uiState.collectAsState()

    val fechaActual = LocalDate.now().format(
        DateTimeFormatter.ofPattern(
            "EEEE d 'de' MMMM",
            Locale("es", "CL")
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Registro del día",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = fechaActual.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase() else it.toString()
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text("← Volver")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // PERÍODO
            SectionCard(
                titulo = "Período y flujo"
            ) {

                Text(
                    text = "¿Comenzó tu período hoy?",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    FilterChip(
                        selected = uiState.comenzoPeriodo == true,
                        onClick = {
                            registroViewModel.onComenzoPeriodoChange(true)
                        },
                        label = {
                            Text("Sí")
                        }
                    )

                    FilterChip(
                        selected = uiState.comenzoPeriodo == false,
                        onClick = {
                            registroViewModel.onComenzoPeriodoChange(false)
                        },
                        label = {
                            Text("No")
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Flujo",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    ChoiceChip(
                        texto = "Sin",
                        seleccionado = uiState.flujo == Flujo.SIN_FLUJO,
                        onClick = {
                            registroViewModel.onFlujoChange(Flujo.SIN_FLUJO)
                        }
                    )

                    ChoiceChip(
                        texto = "Leve",
                        seleccionado = uiState.flujo == Flujo.LEVE,
                        onClick = {
                            registroViewModel.onFlujoChange(Flujo.LEVE)
                        }
                    )

                    ChoiceChip(
                        texto = "Medio",
                        seleccionado = uiState.flujo == Flujo.MEDIO,
                        onClick = {
                            registroViewModel.onFlujoChange(Flujo.MEDIO)
                        }
                    )

                    ChoiceChip(
                        texto = "Alto",
                        seleccionado = uiState.flujo == Flujo.ALTO,
                        onClick = {
                            registroViewModel.onFlujoChange(Flujo.ALTO)
                        }
                    )
                }

                MensajeError(error = uiState.errorFlujo)
            }

            // EMOCIONES
            SectionCard(
                titulo = "¿Cómo te sientes?"
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ChoiceChip(
                        "Tranquila",
                        uiState.emocion == "Tranquila"
                    ) {
                        registroViewModel.onEmocionChange("Tranquila")
                    }

                    ChoiceChip(
                        "Feliz",
                        uiState.emocion == "Feliz"
                    ) {
                        registroViewModel.onEmocionChange("Feliz")
                    }

                    ChoiceChip(
                        "Sensible",
                        uiState.emocion == "Sensible"
                    ) {
                        registroViewModel.onEmocionChange("Sensible")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ChoiceChip(
                        "Irritable",
                        uiState.emocion == "Irritable"
                    ) {
                        registroViewModel.onEmocionChange("Irritable")
                    }

                    ChoiceChip(
                        "Ansiosa",
                        uiState.emocion == "Ansiosa"
                    ) {
                        registroViewModel.onEmocionChange("Ansiosa")
                    }

                    ChoiceChip(
                        "Triste",
                        uiState.emocion == "Triste"
                    ) {
                        registroViewModel.onEmocionChange("Triste")
                    }
                }
            }

            // ENERGÍA
            SectionCard(
                titulo = "Energía"
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ChoiceChip(
                        "Baja",
                        uiState.energia == "Baja"
                    ) {
                        registroViewModel.onEnergiaChange("Baja")
                    }

                    ChoiceChip(
                        "Normal",
                        uiState.energia == "Normal"
                    ) {
                        registroViewModel.onEnergiaChange("Normal")
                    }

                    ChoiceChip(
                        "Alta",
                        uiState.energia == "Alta"
                    ) {
                        registroViewModel.onEnergiaChange("Alta")
                    }
                }
            }

            // SÍNTOMAS
            SectionCard(
                titulo = "Síntomas"
            ) {

                CampoTexto(
                    valor = uiState.sintomas,
                    onCambio = { registroViewModel.onSintomasChange(it) },
                    etiqueta = "Síntomas físicos",
                    ejemplo = "Ej: dolor abdominal, cansancio...",
                    error = uiState.errorSintomas,
                )
            }

            // OTROS SÍNTOMAS (en la base de datos se guarda como "biomarcador")
            SectionCard(
                titulo = "Otros síntomas · Opcional"
            ) {

                CampoTexto(
                    valor = uiState.biomarcador,
                    onCambio = { registroViewModel.onBiomarcadorChange(it) },
                    etiqueta = "Otro síntoma",
                    ejemplo = "Ej: temperatura, dolor de espalda",
                    error = uiState.errorBiomarcador,
                )
            }

            // NOTAS
            SectionCard(
                titulo = "Notas"
            ) {

                CampoTexto(
                    valor = uiState.notas,
                    onCambio = { registroViewModel.onNotasChange(it) },
                    etiqueta = "Nota personal",
                    ejemplo = "¿Quieres agregar algo sobre tu día?",
                    error = uiState.errorNotas,
                    modifier = Modifier.height(140.dp)
                )
            }

            MensajeError(error = uiState.errorGeneral)

            Button(
                onClick = {
                    registroViewModel.guardar(onGuardado)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Guardar registro",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun SectionCard(
    titulo: String,
    contenido: @Composable () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            contenido()
        }
    }
}

@Composable
private fun ChoiceChip(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {

    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        label = {
            Text(texto)
        }
    )
}

// Campo de texto que se pinta en rojo, con ícono y mensaje, si tiene error.
@Composable
private fun CampoTexto(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    ejemplo: String,
    error: String?,
    modifier: Modifier = Modifier
) {

    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = {
            Text(etiqueta)
        },
        placeholder = {
            Text(ejemplo)
        },
        isError = error != null,
        trailingIcon = {
            if (error != null) {
                Icon(
                    painter = painterResource(R.drawable.ic_error),
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        },
        supportingText = {
            if (error != null) {
                Text(error)
            }
        },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp)
    )
}

// Mensaje de error con ícono. AnimatedVisibility lo hace aparecer y desaparecer suavemente.
@Composable
private fun MensajeError(error: String?) {

    AnimatedVisibility(visible = error != null) {
        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_error),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = error.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
