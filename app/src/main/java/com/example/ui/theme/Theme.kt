package com.example.omnimind.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.omnimind.data.prefs.AppTheme

private val ManusColorScheme = darkColorScheme(
    primary = ManusElectricBlue,
    secondary = ManusPurpleAccent,
    tertiary = ManusElectricBlue,
    background = ManusBlack,
    surface = ManusSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = ManusTextPrimary,
    onSurface = ManusTextPrimary,
    outline = ManusBorder,
    surfaceVariant = ManusDeepGrey,
    onSurfaceVariant = ManusTextSecondary
)

private val DarkColorScheme = ManusColorScheme

private val LightColorScheme = lightColorScheme(
    primary = ManusElectricBlue,
    secondary = ManusPurpleAccent,
    background = Color.White,
    surface = Color(0xFFF5F5F5),
    onBackground = Color.Black,
    onSurface = Color.Black
)

private val ObsidianScheme = ManusColorScheme

private val AuroraScheme = lightColorScheme(
    primary = AuroraPrimary,
    secondary = AuroraSecondary,
    background = AuroraBackground,
    surface = AuroraSurface,
    onBackground = AuroraOnSurface,
    onSurface = AuroraOnSurface
)

private val EmberScheme = darkColorScheme(
    primary = EmberPrimary,
    secondary = EmberSecondary,
    background = EmberBackground,
    surface = EmberSurface,
    onBackground = EmberOnSurface,
    onSurface = EmberOnSurface
)

@Composable
fun OmniMindTheme(
    appTheme: AppTheme? = null,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when (appTheme) {
        AppTheme.OBSIDIAN -> ObsidianScheme
        AppTheme.AURORA -> AuroraScheme
        AppTheme.EMBER -> EmberScheme
        null -> if (darkTheme) DarkColorScheme else LightColorScheme
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
