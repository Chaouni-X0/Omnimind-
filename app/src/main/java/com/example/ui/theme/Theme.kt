package com.example.omnimind.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.omnimind.data.prefs.AppTheme

private val OmniMindDarkScheme = darkColorScheme(
    primary = ElectricCyan,
    onPrimary = VoidBlack,
    secondary = AmberAccent,
    onSecondary = VoidBlack,
    tertiary = SignalGreen,
    background = VoidBlack,
    onBackground = RawWhite,
    surface = SurfaceDark,
    onSurface = RawWhite,
    surfaceVariant = DeepNavy,
    onSurfaceVariant = DimText,
    outline = SteelBorder,
    outlineVariant = MutedGrey,
    error = SignalRed,
    onError = RawWhite
)

@Composable
fun OmniMindTheme(
    appTheme: AppTheme? = null,
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = OmniMindDarkScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
