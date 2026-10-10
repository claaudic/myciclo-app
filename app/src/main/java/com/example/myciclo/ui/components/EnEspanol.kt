package com.example.myciclo.ui.components

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import java.util.Locale

// Muestra el contenido en español de Chile aunque el teléfono esté en otro idioma.
// Se usa con el DatePicker: así los meses y días salen en español
// y la semana empieza el lunes.
@Composable
fun EnEspanol(contenido: @Composable () -> Unit) {
    val configuracionActual = LocalConfiguration.current

    val configuracionEspanol = remember(configuracionActual) {
        Configuration(configuracionActual).apply {
            setLocale(Locale("es", "CL"))
        }
    }

    CompositionLocalProvider(LocalConfiguration provides configuracionEspanol) {
        contenido()
    }
}
