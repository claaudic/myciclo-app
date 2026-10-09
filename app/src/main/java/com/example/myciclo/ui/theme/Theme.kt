package com.example.myciclo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Solo tema claro y sin color dinámico: la app siempre usa la paleta de myCiclo,
// aunque el teléfono tenga otros colores de sistema.
private val MyCicloColorScheme = lightColorScheme(

    primary = MyCicloPrimary,
    onPrimary = Color.White,

    primaryContainer = MyCicloPrimarySoft,
    onPrimaryContainer = MyCicloPrimary,

    secondary = MyCicloPink,
    onSecondary = Color.White,

    secondaryContainer = MyCicloPinkSoft,
    onSecondaryContainer = MyCicloPinkText,

    tertiary = MyCicloEva,
    onTertiary = Color.White,

    tertiaryContainer = MyCicloEvaSoft,
    onTertiaryContainer = MyCicloEva,

    background = MyCicloBackground,
    onBackground = MyCicloText,

    surface = MyCicloSurface,
    onSurface = MyCicloText,

    surfaceVariant = MyCicloPrimarySoft,
    onSurfaceVariant = MyCicloTextSecondary,

    surfaceContainerLowest = MyCicloSurface,
    surfaceContainerLow = MyCicloSurface,
    surfaceContainer = MyCicloSurface,
    surfaceContainerHigh = MyCicloSurface,
    surfaceContainerHighest = MyCicloPrimarySoft,

    outline = MyCicloBorder,
    outlineVariant = MyCicloBorder,

    error = MyCicloError,
    onError = Color.White,
    errorContainer = MyCicloErrorSoft,
    onErrorContainer = MyCicloError
)

@Composable
fun MyCicloTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MyCicloColorScheme,
        typography = Typography,
        content = content
    )
}
