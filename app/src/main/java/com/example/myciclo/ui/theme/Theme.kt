package com.example.myciclo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MyCicloColorScheme = lightColorScheme(

    primary = MyCicloPrimary,
    onPrimary = Color.White,

    primaryContainer = MyCicloPrimarySoft,
    onPrimaryContainer = MyCicloText,

    secondary = MyCicloPink,
    onSecondary = Color.White,

    secondaryContainer = MyCicloPinkSoft,
    onSecondaryContainer = MyCicloText,

    tertiary = MyCicloGold,
    onTertiary = Color.White,

    tertiaryContainer = MyCicloGoldSoft,
    onTertiaryContainer = MyCicloText,

    background = MyCicloBackground,
    onBackground = MyCicloText,

    surface = MyCicloSurface,
    onSurface = MyCicloText,

    surfaceVariant = MyCicloSurfaceSoft,
    onSurfaceVariant = MyCicloTextSecondary,

    surfaceContainerLowest = MyCicloSurface,
    surfaceContainerLow = MyCicloSurfaceSoft,
    surfaceContainer = MyCicloSurfaceSoft,
    surfaceContainerHigh = MyCicloSurfaceSoft,
    surfaceContainerHighest = MyCicloPrimarySoft,

    outline = MyCicloBorder,
    outlineVariant = MyCicloBorder,

    error = MyCicloError,
    onError = Color.White
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