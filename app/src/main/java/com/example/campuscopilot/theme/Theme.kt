package com.example.campuscopilot.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryDark,
    onPrimary = Color.White,
    primaryContainer = SurfaceTint,
    onPrimaryContainer = TextPrimary,
    secondary = PrimaryAccent,
    onSecondary = Color.White,
    background = SurfaceBackground,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceTint,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    outlineVariant = BorderSubtle
)

private val DarkColorScheme = darkColorScheme(
    primary = Color.White,
    onPrimary = PrimaryDark,
    primaryContainer = Color(0xFF1E293B),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF60A5FA),
    onSecondary = PrimaryDark,
    background = Color(0xFF0B0F17),
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF161E2E),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0xFF334155),
    outlineVariant = Color(0xFF1E293B)
)

@Composable
fun CampusCopilotTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
