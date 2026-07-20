package com.example.omnimind.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.example.omnimind.data.prefs.AppTheme

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

private val ObsidianScheme = darkColorScheme(
    primary = ObsidianPrimary,
    secondary = ObsidianSecondary,
    background = ObsidianBackground,
    surface = ObsidianSurface,
    onBackground = ObsidianOnSurface,
    onSurface = ObsidianOnSurface
)

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

/**
 * تُطبّق إحدى سمات العلامة الثلاث (Obsidian / Aurora / Ember) المختارة أثناء الإعداد الأولي.
 * إن لم يتم تمرير [appTheme] يتم الرجوع إلى سمة Material الافتراضية بحسب النظام.
 */
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
