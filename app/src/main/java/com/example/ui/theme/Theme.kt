package com.example.omnimind.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.omnimind.data.prefs.AppTheme

/**
 * OmniMind Industrial Theme Engine
 * Strictly follows the Brutal Minimalism / Cyber-Industrial aesthetic.
 */
private val IndustrialColorScheme = darkColorScheme(
    primary = SignalGreen,
    secondary = RawWhite,
    tertiary = SignalGreen,
    background = VoidBlack,
    surface = IndustrialGrey,
    onPrimary = VoidBlack,
    onSecondary = VoidBlack,
    onBackground = RawWhite,
    onSurface = RawWhite,
    outline = SteelBorder,
    surfaceVariant = VoidBlack,
    onSurfaceVariant = GhostGrey,
    error = SignalRed
)

private val DarkColorScheme = IndustrialColorScheme

private val LightColorScheme = lightColorScheme(
    primary = SignalGreen,
    secondary = VoidBlack,
    background = RawWhite,
    surface = Color(0xFFEEEEEE),
    onBackground = VoidBlack,
    onSurface = VoidBlack
)

@Composable
fun OmniMindTheme(
    appTheme: AppTheme? = null,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // We commit to the Industrial aesthetic regardless of the selected legacy theme
    val colorScheme = IndustrialColorScheme
    
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
