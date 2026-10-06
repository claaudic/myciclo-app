package com.example.myciclo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.myciclo.ui.screens.InicioScreen
import com.example.myciclo.ui.screens.RegistroScreen
import com.example.myciclo.ui.theme.MyCicloTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            MyCicloTheme {

                var pantalla by remember {
                    mutableStateOf("inicio")
                }

                when (pantalla) {

                    "inicio" -> {
                        InicioScreen(
                            onNuevoRegistro = {
                                pantalla = "registro"
                            }
                        )
                    }

                    "registro" -> {
                        RegistroScreen(
                            onVolver = {
                                pantalla = "inicio"
                            },
                            onGuardar = {
                                pantalla = "inicio"
                            }
                        )
                    }
                }
            }
        }
    }
}